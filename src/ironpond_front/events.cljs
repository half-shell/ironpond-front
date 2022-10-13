(ns ironpond-front.events
  (:require
   [re-frame.core :as re-frame]
   [ironpond-front.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced
  [_ _]
  db/default-db))

(re-frame/reg-event-db
 ::set-card-selected
 (fn-traced
  [db [e player card-idx]]
  ;; We set the one matching the index as selected
  (assoc-in db [player :hand card-idx :selected] true)))

(re-frame/reg-event-db
 ::unselect-card-hand
 (fn-traced
  [db [e player]]
  (assoc-in db [player :hand]
            ;; NOTE: Does not look nice either.
            ;; How can we avoid relying on .indexOf?
            (reduce #(assoc-in %1 [(.indexOf %1 %2) :selected] false)
                    (get-in db [player :hand])
                    (get-in db [player :hand])))))

(re-frame/reg-event-db
 ::preview-card
 (fn-traced
  [db [e player idx]]
  (let [card (get-in db [player :hand idx])
        preview (:preview card)
        squares (when-not (nil? preview) (preview (:board db)))]
    (when-not (empty? squares)
      (let [square-idx (:idx (first squares))]
        ;; TODO: use reduce to handle all squares
        (assoc-in db [:board square-idx :playable] true))))))

(re-frame/reg-event-db
 ::reset-board
 (fn-traced
  [db]
  (assoc-in db [:board]
            ;; NOTE: This is **very** inneficient and takes quite a bit of time (~75ms).
            ;; Find a way to make is faster
            (reduce #(assoc-in %1 [(:id %2) :playable] false)
                    (:board db)
                    (:board db)))))
