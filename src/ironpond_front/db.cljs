(ns ironpond-front.db)

(defn init-board []
  (->>
   (vec (range 56))
   (map-indexed
    (fn [idx square]
      (let [color (cond
                    (and (>= idx 0) (<= idx 17)) "white"
                    (and (>= idx 39) (<= idx 55)) "black"
                    :else nil)
            type (cond
                   (or
                    (and (>= idx 8) (<= idx 15))
                    (and (>= idx 40) (<= idx 47))) :pawn
                   (or (= idx 0) (= idx 7) (= idx 48) (= idx 55)) :rook
                   (or (= idx 1) (= idx 6) (= idx 49) (= idx 54)) :knight
                   (or (= idx 2) (= idx 5) (= idx 50) (= idx 53)) :bishop
                   (or (= idx 3) (= idx 51)) :queen
                   (or (= idx 4) (= idx 52)) :king
                   :else nil)]
        (hash-map :color color :type type :idx idx))))))

(defn init-deck [] (->>
                    (vec (range 39))
                    (map-indexed
                     (fn [idx]
                       (hash-map :idx idx)))))

(defn init-hand [] (->>
                    (vec (range 5))
                    (map-indexed #(hash-map :idx % :selected false))
                    (vec)))
(def default-db
  {:name "ironpond"
   :board (init-board)
   :deck (init-deck)
   :white {:hand (init-hand)}
   :black {:hand (init-hand)}})
