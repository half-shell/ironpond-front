(ns ironpond-front.cards
  (:require
   [ironpond-front.moves :as moves]))

(defn get-image-url
  [card]
  (let [{idx :idx name :name} card]
    ;; FIXME: Remove when cards all all correctly initialized
    (when-not (nil? name)
        (apply str [
                    idx
                    "-"
                    (-> name
                        (clojure.string/replace #"\s" "_")
                        (clojure.string/upper-case))
                    ".png"]))))

;; NOTE: Cards should be a lookup table or some kind of big ass map
(def ninja
  {:idx 26
   :name "ninja"
   :description "Swap 2 of the opposing bishops."
   :selected false
   :fx (fn [] nil)
   :preview (fn
              [board player]
              (moves/find-pieces {:type :bishop
                                  :player (moves/opponent player)}
                                 board))})
