(ns learning-cljs.logic)

(def withdraw-zero (partial filter pos?))

(defn append-zeros [coll]
  (take 4 (concat coll (repeat 0))))

(def move-row-left
  (comp
    (partial map (partial apply +))
    (partial mapcat (partial partition-all 2))
    (partial partition-by identity)
    (partial withdraw-zero)))

(defn move-left [board]
  (map (comp append-zeros move-row-left) board))

(def reverse-board (partial map reverse))

(def move-right (comp
                  reverse-board
                  move-left
                  reverse-board))

(def transpose (partial apply map list))

(def move-up (comp
               transpose
               move-left
               transpose))

(def move-down (comp
                 transpose
                 move-right
                 transpose))

(defn empty-tile-index [board]
  [(first (rand-nth (filter #(zero? (second %)) (map-indexed vector (flatten board)))))])

(defn add-new-tile [board]
  (partition-all 4
                 (assoc-in (into [] (flatten board)) (empty-tile-index board) (rand-nth [2 4]))))

(defn game-over? [board]
  (and
    (not (some zero? (flatten board)))
    (every? #(= (count %) 4) (map dedupe board))
    (every? #(= (count %) 4) (map dedupe (transpose board)))))