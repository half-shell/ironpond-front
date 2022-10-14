(ns ironpond-front.cards
  (:require
   [ironpond-front.moves :as moves]))

;; NOTE: Cards should be a lookup table or some kind of big ass map
(def ninja
  {:idx 26
   :name "ninja"
   :selected false
   :fx (fn [] nil)
   :preview (fn
              [board player]
              (moves/find-pieces {:type :bishop
                                  :player (moves/opponent player)}
                                 board))})
