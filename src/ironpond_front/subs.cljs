(ns ironpond-front.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::board
 (fn [db]
   (:board db)))

(re-frame/reg-sub
 ::white
 (fn [db]
   (:white db)))

(re-frame/reg-sub
 ::black
 (fn [db]
   (:black db)))

(re-frame/reg-sub
 ::card
 (fn [db [e [player card-idx]]]
   (get-in db [player :hand card-idx])))

(re-frame/reg-sub
 ::square
 (fn
   [db [e idx]]
   (get-in db [:board idx])))
