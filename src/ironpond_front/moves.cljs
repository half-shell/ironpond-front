(ns ironpond-front.moves)

(defn find-pieces [piece board]
  (let [keys [:player :type]]
    (->
     (filter
      #(= (keep piece keys) (some-> (:piece %) (keep keys)))
      board)
     (vec))))

(defn opponent
  [player]
  (if (= player :white) :black :white))
