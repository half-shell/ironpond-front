(ns ironpond-front.db)

(defn build-piece
  [player piece]
  (case piece
    :pawn (hash-map :player player :type piece :move (fn [] nil) :take (fn [] nil))
    :rook (hash-map :player player :type piece :move (fn [] nil) :take (fn [] nil))
    :knight (hash-map :player player :type piece :move (fn [] nil) :take (fn [] nil))
    :bishop (hash-map :player player :type piece :move (fn [] nil) :take (fn [] nil))
    :queen (hash-map :player player :type piece :move (fn [] nil) :take (fn [] nil))
    :king (hash-map :player player :type piece :move (fn [] nil) :take (fn [] nil))
    nil))

(defn get-squares [board fn]
  (filter fn board))

(defn queen-moves [current-id board]
  (let [filter-function (fn
                          [current-id square]
                          (let [id (:id square)]
                            (or
                             (= (mod (- current-id id) 9) 0)
                             (= (mod (- current-id id) 7) 0)
                             (= (mod (- id current-id) 9) 0)
                             (= (mod (- id current-id) 7) 0))))]
    (get-squares board (partial filter-function current-id))))

(defn get-pieces [board player piece] nil)

(defn init-board []
  (->> (range 64)
       (map-indexed (fn
                      [idx square]
                      (let [player (cond
                                     (and (>= idx 0) (<= idx 17)) "white"
                                     (and (>= idx 48) (<= idx 64)) "black"
                                     :else nil)
                            piece (cond
                                    (or
                                     (and (>= idx 8) (<= idx 15))
                                     (and (>= idx 48) (<= idx 55))) :pawn
                                    (or (= idx 0) (= idx 7) (= idx 56) (= idx 63)) :rook
                                    (or (= idx 1) (= idx 6) (= idx 57) (= idx 62)) :knight
                                    (or (= idx 2) (= idx 5) (= idx 58) (= idx 61)) :bishop
                                    (or (= idx 3) (= idx 59)) :queen
                                    (or (= idx 4) (= idx 60)) :king
                                    :else nil)]
                        (hash-map
                         :id idx
                         :piece (build-piece player piece)
                         :playable false))))
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
