(ns adventure.core
  (:gen-class))

(def player
  { :location :foyer
    :inventory #{}
    :hp 10
    :lives 1
    :seen #{}})

(defn reduceHealth [dmg]
  (def player (update player :hp - dmg))
  (if (<= (player :hp) 0)
    (do (println "Game Over! You Died :(") (System/exit 0))
  )
)

(defn removeFromInventory [item]
  (def player 
    (assoc player :inventory (disj (player :inventory) item))
    )
)

(defn restoreHealth [health]
  (def player (update player :hp + health))
)

(defn addToInventory [item]
  (def player 
    (assoc player :inventory (conj (player :inventory) item))
    )
)

(defn printPlayer []
  (println (str "Player is currently has " (player :hp) " hp and has " (player :inventory) " in their inventory."))
)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;(println "Hello, World!")
  (printPlayer)
  (reduceHealth 5)
  (addToInventory "key")
  (printPlayer)
  (removeFromInventory "key")
  (printPlayer)
  )

(-main)