(ns ironpond-front.db
  (:require
   [ironpond-front.cards :as cards]
   [ironpond-front.moves :as moves]
   [ironpond-front.pieces :as pieces]))

(defn build-piece
  [player piece]
  (case piece
    :pawn (hash-map
           :player player
           :type piece
           :move moves/pawn-moves
           :take (fn [] nil))
    :rook (hash-map :player player :type piece :move (fn [] ()) :take (fn [] nil))
    :knight (hash-map :player player :type piece :move (fn [] ()) :take (fn [] nil))
    :bishop (hash-map :player player :type piece :move (fn [] ()) :take (fn [] nil))
    :queen (hash-map :player player :type piece :move moves/queen-moves :take (fn [] nil))
    :king (hash-map :player player :type piece :move (fn [] ()) :take (fn [] nil))
    nil))

(defn get-squares [board fn]
  (filter fn board))

(defn get-pieces [board player piece] nil)

(def pieces [:pawn :rook :knight :bishop :queen :king])

(defn init-board []
  (->> (range 64)
       (map-indexed
        (fn
          [idx _]
          (let [player
                (cond
                  (some
                   (partial = idx)
                   (mapcat #(pieces/home-squares % :black) pieces)) :black
                  :else :white)
                piece (cond
                        (some (partial = idx) (pieces/home-squares :pawn)) :pawn
                        (some (partial = idx) (pieces/home-squares :rook)) :rook
                        (some (partial = idx) (pieces/home-squares :knight)) :knight
                        (some (partial = idx) (pieces/home-squares :bishop)) :bishop
                        (some (partial = idx) (pieces/home-squares :queen)) :queen
                        (some (partial = idx) (pieces/home-squares :king)) :king
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

(def default-db
  {:name "ironpond"
   :board (init-board)
   :deck (init-deck)
   :white {:hand (vec (conj (init-hand) cards/ninja))}
   :black {:hand (vec (conj (init-hand) cards/ninja))}})
