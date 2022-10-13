(ns ironpond-front.views
  (:require
   [re-frame.core :as re-frame]
   [ironpond-front.events :as events]
   [ironpond-front.subs :as subs]))

(def square-size 64)
(def card-size 128)

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
       {:class (str
                "square "
                (if (even?
                     (+ idx
                        (when (or
                               (and (> idx 7) (< idx 16))
                               (and (> idx 23) (< idx 32))
                               (and (> idx 39) (< idx 48)))
                          1)))
                  "black"
                  "white")
        :style {
                :height square-size
                :width square-size
                :font-size square-size
                :text-align "center"}}
       (display-piece square)])))

(defn display-card [player idx _]
  (let [card (re-frame/subscribe [::subs/card [player idx]])]
    [:div.card
     ^{:key idx}
     {:style {:height card-size :width card-size :text-align "center"}
      :class (when (get @card :selected) "selected")
      :on-click #(do
                   (re-frame/dispatch [::events/unselect-card-hand player])
                   (re-frame/dispatch [::events/set-card-selected player idx]))}
     (str idx (get @card :selected))]))

(defn display-hand [player hand]
  (into
   [:div {:style {:display "flex"}}]
   (map-indexed #(display-card player %1 %2) hand)))

(defn main-panel []
  (let [board (re-frame/subscribe [::subs/board])
        black (re-frame/subscribe [::subs/white])
        white (re-frame/subscribe [::subs/black])]
    [:div {:style {:display "flex" :flex-direction "column"}}
     (display-hand :white (get @white :hand))
     (into [:div {:style {:display "flex"
                          :max-width (* square-size 8)
                          :flex-wrap "wrap"}}]
           (map #(display-square %) @board))
     (display-hand :black (get @black :hand))]))
