(ns ironpond-front.moves)

(defn find-pieces [piece board]
  (let [keys [:player :type]]
    (filterv
     #(= (keep piece keys) (some-> (:piece %) (keep keys)))
     board)))

(defn opponent
  [player]
  (if (= player :white) :black :white))

(defn queen-moves
  ;; NOTE: "current-square" is not great. Could use a bit of renaming.
  [current-square square]
  (let [current-id (:id current-square)
        id         (:id square)]
    (or
     (= (mod (- current-id id) 9) 0)
     (= (mod (- current-id id) 7) 0)
     (= (mod (- id current-id) 9) 0)
     (= (mod (- id current-id) 7) 0))))

(defn pawn-moves
  [current-square square]
  (let [{piece :piece current-id :id} current-square
        {id :id} square
        offset (if (= (:player piece) :white) 8 -8)]
    (= (+ current-id offset) id)))
