(ns ironpond-front.db)

(defn init-board []
  (->>
   (vec (range 63))
   (map-indexed
    (fn [idx square]
      (let [color (cond
                    (and (>= idx 9) (<= idx 17)) "white"
                    (and (<= idx 53) (>= idx 45)) "black")
            type (if
                  (or
                   (and (>= idx 9) (<= idx 17))
                   (and (<= idx 53) (>= idx 45))) "pon")]
        (hash-map :color color :type type :idx idx))))))

(defn init-deck [] (->>
                    (vec (range 39))
                    (map-indexed
                     (fn [idx]
                       (hash-map :idx idx)))))

(def default-db
  {:name "ironpond"
   :board (init-board)
   :deck (init-deck)})
