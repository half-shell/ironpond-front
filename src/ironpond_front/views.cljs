(ns ironpond-front.views
  (:require
   [re-frame.core :as re-frame]
   [ironpond-front.subs :as subs]))

(def square-size 128)

(defn display-piece [square]
  (let [color (get square :color)]
    (when-not (nil? color)
      (if (= color "white")
        "\u2659"
        "\u265f"))))

(defn display-square [square]
  (let [idx (get square :idx)]
    (when-not (nil? square)
      [:div ^{:key (str idx)}
       {:style {:background-color (if (= (mod idx 2) 0) "white" "orange")
                :height square-size
                :width square-size
                :font-size 100
                :text-align "center"}}
       (display-piece square)])))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        board (re-frame/subscribe [::subs/board])]
    [:div
     [:h1 @name]
     (into [:div {:style {:display "flex"
                          :max-width (* square-size 9)
                          :flex-wrap "wrap"}}]
           (map #(display-square %) @board))]))
