(ns ironpond-front.pieces)

(defn white? [player] (= player :white))
(defn black? [player] (= player :black))

(defn home-squares
  ([type]
   (concat
    (home-squares type :white)
    (home-squares type :black)))

  ([type player]
   (let [ids (cond
               (= type :rook) [0 7]
               (= type :knight) [1 6]
               (= type :bishop) [2 5]
               (= type :queen) (if (black? player) [3] [4])
               (= type :king)  (if (black? player) [4] [3])
               (= type :pawn) [8 9 10 11 12 13 14 15]
               :else [])]
     (cond
       (black? player) (mapv (partial - 63) ids)
       (white? player) (mapv identity ids)
       :else []))))

(defn home-square?
  [player type]
  nil)
