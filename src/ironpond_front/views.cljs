(ns ironpond-front.views
  (:require
   [re-frame.core :as re-frame]
   [ironpond-front.subs :as subs]))

(def square-size 128)

(defn display-piece [square]
  (let [color (get square :color)
        type (get square :type)
        offsets {:base 9812 :color 6
                 :king 0 :queen 1 :rook 2 :bishop 3 :knight 4 :pawn 5}]
    (when-not (nil? type)
      (. js/String fromCodePoint
         (+ (get offsets :base)
            (get offsets type)
            (when (= color "black") (get offsets :color)))))))

(defn display-square [square]
  (let [idx (get square :idx)]
    (when-not (nil? square)
      [:div ^{:key (str idx)}
       {:style
        {:background-color
         (if (even?
              (+ idx
                 (when (or
                        (and (> idx 7) (< idx 16))
                        (and (> idx 23) (< idx 32))
                        (and (> idx 39) (< idx 48)))
                   1)))
           "orange"
           "white")
         :height square-size
         :width square-size
         :font-size 64
         :text-align "center"}}
       (display-piece square)])))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        board (re-frame/subscribe [::subs/board])]
    [:div
     [:h1 @name]
     (into [:div {:style {:display "flex"
                          :max-width (* square-size 8)
                          :flex-wrap "wrap"}}]
           (map #(display-square %) @board))]))
