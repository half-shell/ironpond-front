(ns ironpond-front.views
  (:require
   [re-frame.core :as re-frame]
   [ironpond-front.events :as events]
   [ironpond-front.subs :as subs]
   [ironpond-front.cards :as cards]))

(def square-size 64)
(def card-size 128)

(defn display-piece [square]
  (let [player (get-in square [:piece :player])
        type (get-in square [:piece :type])
        offsets {:base 9812 :color 6
                 :king 0 :queen 1 :rook 2 :bishop 3 :knight 4 :pawn 5}]
    (when-not (nil? type)
      (. js/String fromCodePoint
         (+ (:base offsets)
            (type offsets)
            (when (= player :black) (:color offsets)))))))

(defn display-square [square]
  (let [idx (:id square)
        square-sub (re-frame/subscribe [::subs/square idx])]
    (when-not (nil? square)
      [:div ^{:key (str idx)}
       {:class (str
                "square "
                (if (even?
                     (+ idx
                        (when (or
                               (and (> idx 7) (< idx 16))
                               (and (> idx 23) (< idx 32))
                               (and (> idx 39) (< idx 48))
                               (and (> idx 55) (< idx 64)))
                          1)))
                  "white"
                  "black")
                (when (:playable @square-sub) " playable"))
        :style {
                :height square-size
                :width square-size
                :font-size square-size
                :text-align "center"}
        :on-click #(do
                     (re-frame/dispatch [::events/reset-board])
                     (re-frame/dispatch [::events/preview-move idx]))}
       (display-piece square)])))

(defn display-card [player idx card]
  (let [card-index (:idx card)
        card-background-image (str "url(" (cards/get-image-url card) ")")
        card-sub (re-frame/subscribe [::subs/card [player idx]])]
    [:div.card
     ^{:key card-index}
     {
      :class (when (:selected @card-sub) "selected")
      :on-click #(do
                   (re-frame/dispatch [::events/reset-board])
                   ;; TODO: Ideally, we'd want to reset all hands so we can
                   ;; display only a single selected card
                   (re-frame/dispatch [::events/unselect-card-hand player])
                   (re-frame/dispatch [::events/set-card-selected player idx])
                   (re-frame/dispatch [::events/preview-card player idx]))}
     [:div.img {:style {
                        :height card-size
                        :width card-size
                        ;; TODO: This'll end up being dynamic depending on the card
                        ;; :background-image: card-background-image
                        }}]
     [:div.title
      {:style {:text-align "center"}}
      (str card-index (:selected @card-sub))]]))

(defn display-hand [player hand]
  (into
   [:div.hand {:style {:display "flex"}}]
   (map-indexed #(display-card player %1 %2) hand)))

(defn main-panel []
  (let [board (re-frame/subscribe [::subs/board])
        black (re-frame/subscribe [::subs/white])
        white (re-frame/subscribe [::subs/black])]
    [:div.game
     (display-hand :white (get @white :hand))
     (into [:div.board {:style {:max-width (* square-size 8)}}]
           (map #(display-square %) @board))
     (display-hand :black (get @black :hand))]))
