(ns ironpond-front.events
  (:require
   [re-frame.core :as re-frame]
   [ironpond-front.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn get-selected-card-idx [hand]
  (first
   (keep-indexed #(when (get %2 :selected) %1) hand)))

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
  (let [card-idx (get-selected-card-idx (get-in db [player :hand] ))]
    ;; We set the one matching the index as selected
    (when-not (nil? card-idx)
      (assoc-in db [player :hand card-idx :selected] false)))))

(re-frame/reg-event-db
 ::reset-card-selection
 (fn-traced
  [db [player]]
  ;; We reset all selected cards
  (reduce #(assoc-in % [:selected] false) (get-in db [player :hand]))))
