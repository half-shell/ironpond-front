(ns ironpond-front.moves)

(defn find-pieces [piece board]
  (let [keys [:player :type]]
    (filterv
     #(= (keep piece keys) (some-> (:piece %) (keep keys)))
     board)))

(defn opponent
  [player]
  (if (= player :white) :black :white))

(defn king-moves
  [current-square square]
  (let [current-id (:id current-square)
        id         (:id square)]
    (or
     (= (+ current-id 7) id)
     (= (+ current-id 8) id)
     (= (+ current-id 9) id)
     (= (- current-id 7) id)
     (= (- current-id 8) id)
     (= (- current-id 9) id)
     (= (+ current-id 1) id)
     (= (- current-id 1) id))))

(defn bishop-moves
  ;; NOTE: "current-square" is not great. Could use a bit of renaming.
  [current-square square]
  (let [current-id (:id current-square)
        id         (:id square)]
    (or
     ;; FIXME: This output results from all over the board.
     ;; We need to be able to find the board's limits
     (= (mod (- current-id id) 9) 0)
     (= (mod (- current-id id) 7) 0)
     (= (mod (- id current-id) 9) 0)
     (= (mod (- id current-id) 7) 0))))

(defn rook-moves
  [current-square square]
  (let [current-id (:id current-square)
        id         (:id square)]
    (or
     (= (mod (- current-id id) 8) 0)
     ;; FIXME: This obviously result in being truthy all the time.
     ;; We need to be able to find the board's limits
     (= (mod (- current-id id) 1) 0)
     (= (mod (- id current-id) 8) 0)
     (= (mod (- id current-id) 1) 0))))

(defn pawn-moves
  [current-square square]
  (let [{piece :piece current-id :id} current-square
        {id :id} square
        offset (if (= (:player piece) :white) 8 -8)]
    (= (+ current-id offset) id)))

(defn knight-moves
  [current-square square]
  (let [current-id (:id current-square)
        id         (:id square)]
    (or
     ;; FIXME: A couple are missing but it's fine for now
     (= (+ current-id 17) id)
     (= (- current-id 17) id)
     (= (+ current-id 15) id)
     (= (- current-id 15) id)
     (= (+ current-id 10) id)
     (= (- current-id 10) id)
     (= (+ current-id 6) id)
     (= (- current-id 6) id))))

(defn queen-moves
  [current-square square]
  (concat
   (bishop-moves current-square square)
   (rook-moves current-square square)))
