(ns learning-cljs.core
  (:require
    [reagent.core :as r]
    [learning-cljs.logic :as l]))

;; -------------------------
;; Views
(def initial-state {:board      [[0, 0, 0, 0]
                                 [0, 2, 0, 0]
                                 [0, 2, 0, 0]
                                 [0, 0, 0, 2]]
                    :game-over? false})

(defn update-state [board]
  (if (l/game-over? board)
    {:board board :game-over? true}
    {:board (l/add-new-tile board) :game-over? false})
  )

(defonce state (r/atom initial-state))

(defn create-tile [tile-value]
  [:div {:class (str "tile tile-" tile-value)}
   (str (if (zero? tile-value) "" tile-value))])

(defn render-row [index row]
  [:div {:class "row"
         :id    (str "row-" index)}
   (map create-tile row)])

(defn move-left [state]
  (update-state (l/move-left (:board state))))

(defn move-right [state]
  (update-state (l/move-right (:board state))))

(defn move-up [state]
  (update-state (l/move-up (:board state))))

(defn move-down [state]
  (update-state (l/move-down (:board state))))

(defn reset-game []
  (reset! state initial-state))

(defn game-over? [state]
  (when (:game-over? state)
    [:div {:class "game-over-container"}
     [:div {:class "game-over"} "GAME OVER"]
     [:button {:class "play-again" :on-click reset-game} "Play again ↻"]]))

(defn board []
  [:div {:class "board" :autofocus 1 :tabindex 1 :on-key-down
                #(case (.-which %)
                   38 (swap! state move-up)
                   40 (swap! state move-down)
                   37 (swap! state move-left)
                   39 (swap! state move-right)
                   nil)}
   (map-indexed render-row (:board @state))
   (game-over? @state)
   ])

(defn mount-root []
  (r/render [board] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

