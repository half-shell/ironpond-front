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
            ;; NOTE: Does not look nice either. What could be better?
            (reduce #(assoc-in %1 [(.indexOf %1 %2) :selected] false)
                    (get-in db [player :hand])
                    (get-in db [player :hand])))))

(re-frame/reg-event-db
 ::reset-card-selection
 (fn-traced
  [db [player]]
  ;; We reset all selected cards
  (reduce #(assoc-in % [:selected] false) (get-in db [player :hand]))))
