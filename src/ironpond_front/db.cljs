(ns ironpond-front.db)

(defn init-board []
  (->> (range 56)
       (map-indexed (fn
                      [idx square]
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
                        (hash-map :color color :type type :idx idx :playable false))))
       (vec)))

(defn init-deck [] (->> (range 39)
                        (map-indexed (fn [idx] (hash-map :idx idx)))
                        (vec)))

(defn init-hand [] (->> (range 5)
                        (map-indexed #(hash-map :idx % :selected false))
                        (vec)))

;; piece is a struct with :type and :color
(defn find-pieces [piece board]
  (let [color (get piece :color)
        type (get piece :type)]
    (filter #(and (= color (get % :color)) (= type (get % :type))) board)))

;; THIS IS THE FIRST CARD
(def ninja
  {:idx 26
   :name "ninja"
   :selected false
   :fx (fn [] nil)
   ;; should be a re-frame construct to change the db
   :preview (fn
              [board]
              (find-pieces {:type :bishop :color "black"} board))})

(def default-db
  {:name "ironpond"
   :board (init-board)
   :deck (init-deck)
   :white {:hand (vec (conj (init-hand) ninja))}
   :black {:hand (vec (conj (init-hand) ninja))}})
