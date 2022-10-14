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
  (assoc-in db [player :hand card-idx :selected] true)))

(re-frame/reg-event-db
 ::unselect-card-hand
 (fn-traced
  [db [e player]]
  (assoc-in db [player :hand]
            ;; FIXME: Does not look nice either.
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
        squares (when-not (nil? preview) (preview (:board db) player))]
    (when-not (empty? squares)
      (assoc-in db [:board]
                (reduce #(assoc-in %1 [(:id %2) :playable] true)
                        (:board db)
                        squares))))))

(re-frame/reg-event-db
 ::reset-board
 (fn-traced
  [db]
  (assoc-in db [:board]
            ;; NOTE: Not great. Can this be better?
            (reduce #(assoc-in %1 [(:id %2) :playable] false)
                    (:board db)
                    (:board db)))))

(re-frame/reg-event-db
 ::preview-move
 (fn-traced
  [db [e idx]]
  (let [square (get-in db [:board idx])
        moves (filterv #(apply (get-in square [:piece :move]) [square %]) (:board db))]
    (assoc-in db [:board]
              (reduce #(assoc-in %1 [(:id %2) :playable] true)
                      (:board db)
                      moves)))))
