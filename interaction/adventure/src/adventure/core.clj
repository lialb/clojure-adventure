(ns adventure.core
  (:gen-class))

(def init-items
 {:banana {:desc "A yellow, long, curved fruit. You should probably peel it before eating."
           :name "A surprisingly unrotten banana" }
  :apple {:desc "A red piece of fruit. Looks very edible though"
           :name "An apple" }
  :redKey {:desc "A crimson key that looks very useful..."
           :name "A red key"}
  :redChest {:desc "A chest that is red in color. May contain something cool... "
             :name "A red chest"}
  :ID {:desc "An ID card that has the picture of an accomplished looking old guy"
       :name "An ID card"}
  :greenKey {:desc "A green key brimming with greeness. Seems very useful..."
             :name "A green key"}
  :greenChest {:desc "A green chest that is locked. I wonder what can open it..."
               :name "A chest that is green"}
  :orangeKey {:desc "An orange key that looks very useful..."
              :name "An orange key"}
  :door {:desc "A big locked door that leads somewhere useful... maybe"
         :name "A locked door"}
  :blueKey {:desc "A blue key that may or may not do something..."
            :name "A blue key"}
  :controlPanel {:desc "A control panel that seems to be operated with some type of authorization"
                 :name "A control panel"}
  })


(def init-map
  {:cell {:desc "You wake up in a dimly lit room. You look at your green hands, shackled to the floor. You remember your space ship being intercepted by the human race's elite SpaceX warships. You sigh, and using your alien strength, easily break out of the chains."
           :title "a drab cell"
           :dir {:south :sauna
                 :east :cafeteria } 
           :contents #{}}
   :sauna {:desc "You step into the sauana. It is blazingly hot due to climate change. You lose 3 hp"
              :title "a really hot room"
              :dir {:north :cell}
              :contents #{}}
   :cafeteria {:desc "You step into the cafeteria. It's a drab mess hall; rows of tables and benches litter the room. You could almost smell the terrible food that was once served here. Walking along the halls, you see a fridge."
              :title "the cafeteria"
              :dir {:west :cell
                    :south :bathroom
                    :east :armory}
              :contents #{:banana}}
   :bathroom {:desc "You open the door labelled with the silhouette of a human man. You walk in ... and the stench hits you HARD. You take 3 psychic damage from a horrible stench. You gag uncontrollably"
              :title "a dirty abandoned bathroom"
              :dir {:north :cafeteria}
              :contents #{}}
   :armory {:desc "You walk into the armory. On the northern wall, you see weapons that you couldn't possibly identify hanging across the wall. Military grade rifles along with hints of the second amendment."
              :title "your very generic armory filled with weapons"
              :dir {:west :cafeteria
                    :south :lounge
                    :east :hallway}
              :contents #{:redKey}}
   :lounge {:desc "You walk into the lounge. You see remnants of the once failed feng shui of mediocrity. Wow, you thought. You probably could've designed this room better."
              :title "a really REALLY boring room"
              :dir {:north :armory
                    :south :control
                    :east :security}
              :contents #{}}
   :security {:desc "You walk into a dark room. The lights go on. You see hundreds of monitors displaying CCTV of daily human interaction. Taking steps in, you somehow trip off the old security systems. You see a gatling descend from the ceiling. You receive 5 damage from getting shot. Huh, good thing the gatling gun ran out of bullets."
              :title "a dark room filled with monitors monitoring the daily lives of millions"
              :dir {:west :lounge}
              :contents #{}}
   :control {:desc "You walk into a room filled with desks and computers. You walk around, taking note that of a pedestal with a control panel in the very center."
              :title "a room with a lot of controls"
              :dir {:north :lounge
                    :east :corridor
                    :west :corridor2
                    :south :corridor3}
              :contents #{:controlPanel}}
   :corridor {:desc "You walk in to a corridor. Nothing much but a passageway"
              :title "a passageway that serves its purpose"
              :dir {:west :control
                    :east :pit}
              :contents #{}}
   :pit {:desc "You walk into a dark, dark room. You realize there's no floor. You fall. You're dead"
              :title "the always depressing pit filled sadness and death"
              :dir {:west :corridor}
              :contents #{}}
   :corridor2 {:desc "a bland corridor that has two entrances, one on each end"
              :title "another hallway"
              :dir {:west :storage
                    :east :control}
              :contents #{}}
   :storage {:desc "You look in the room. You see a chest that looks like it should be opened"
              :title "a room full of what could be interesting stuff"
              :dir {:east :control
                    :south :trash}
              :contents #{:chest}}
   :trash {:desc "You walk into a room, filled with flies and rats. This is where the human waste and perfectly good food go. You vomit, taking 2 damage"
              :title "a room waste the human waste goes"
              :dir {:north :storage}
              :contents #{}}
   :corridor3 {:desc "This is another room that serves its purpose, leading passengers into the next room and beyond."
              :title "the inbetween room that no one cares about"
              :dir {:west :general
                    :east :supplies
                    :north :control}
              :contents #{}}
   :general {:desc "You walk into a room decorated with baroque style with a rustic finish. Beautiful tapestry and paintings uniformly scatter the wall. You are amazed at the splendor that humans are capable of. This is where the leader must reside."
              :title "a beautiful room filled with remnants of the past leaders"
              :dir {:east :corridor3}
              :contents #{:greenKey}}
   :supplies {:desc "You walk into a room filled with cabinets, supplies, and old cigarette butts. Papers are scattered everywhere. You walk "
              :title "a room full of supplies and storage units"
              :dir {:west :corridor3
                    :east :chem}
              :contents #{:safe}}
   :chem {:desc "You walk into what seems like an old lab. There's broken glass from beakers and burets, literring the ground with shards. You carefully navigate the sharp glass around the room. You bump into a vile full of liquid, and it splashes on you. You take 4 damage from acid. You cry a little bit and leave."
              :title "a room where biological tests are conducted"
              :dir {:west :supplies}
              :contents #{}}
   :hallway {:desc "a long passage with doors into rooms on both sides of it"
              :title "an ordinary hallway"
              :dir {:west :armory
                    :east :gate}
              :contents #{}}
   :gate {:desc "You walk into a dimly lit room. There're two doors: a thick looking one, and a normal one that you've seen before."
              :title "a relatively uninteresting room with a gate"
              :dir {:west :hallway
                    :east :prison
                    :south :debriefing}
              :contents #{:door}}
   :prison {:desc "You walk into the the room full of cells. You see skeletons of humans and extra terrestrial life. Dried blood is splattered around the walls. The remains of creatures scatter the ground. You walk around and notice there's something... fresh. Out of the corner of your eye, you see a big, big alien come out from the shadows growling. You attempt to run, but as you flee you take 3 damage from mauling."
              :title "a room with cells... and more"
              :dir {:west :gate}
              :contents #{}}
   :debriefing {:desc "You walk in and see a long, long table. This was where"
              :title "the debriefing room"
              :dir {:north :gate
                    :south :pit
                    :east :barracks}
              :contents #{:blueKey}}
   :barracks {:desc "You walk into a room filled with many beds, duffel bags, and more. This is where they must've lived you thought. It doesn't look appealing to live here, cramped and sweaty."
              :title "a room with many beds and lost personal belongings"
              :dir {:west :debriefing
                    :south :vault}
              :contents #{}}
   :vault {:desc "You walk into the room and see a big massive steel reinforced door. You can almost smell fresh air..."
              :title "a room that has a big lock"
              :dir {:west :pit
                    :north :barracks
                    :south :helicopter}
              :contents #{}}
   :helicopter {:desc "You have made it to a helicopter pad! You somehow made it this far, and can finally smell the fresh boiling air. You take the helicopter and fly away."
              :title "a helicopter pad"
              :dir {:north :vault}
              :contents #{}}
   })


(defn test []
  (println
    ((init-map :vault
    )  :dir)
    
    
  )  
)

(def player
  { :location :cell
    :inventory #{}
    :hp 10
    :seen #{}})
(defn look []
    (println ((init-map (player :location)) :desc)) 
)

(defn getTitle [newRoom]
  (println (str "You are in "((init-map (keyword newRoom)) :title))

  )
)

(defn updateLocation [location]
  (def player (update player :location (keyword location) (keyword location))) 
  (getTitle location)
)
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

(defn quitGame []
  (do (println "Quitting Game")(System/exit 0))
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
  (println (str "Player is currently has " (player :hp) " hp and has " (player :inventory) " in their inventory and is currently at " (name (player :location)) "."))
)



(defn movePlayer
  [command]
  (let [dir (subs command 3)
       newRoom (((init-map (player :location)) :dir) (keyword dir))]
       ;(println newRoom)))
        (if (nil? newRoom) (println (str "Can't go " dir))
          (do (updateLocation newRoom) ))))

(defn starts-with?
  [string substr]
  (clojure.string/starts-with? (clojure.string/lower-case string) substr))

(defn parseCommand
  [command]
  (let [cleanCommand (clojure.string/lower-case command)]
    (cond
      (starts-with? command "help") (println "You asked for help!")
      (starts-with? command "status") (printPlayer)
      (starts-with? command "go") (movePlayer cleanCommand)
      (starts-with? command "look") (look)
      :else (println (str "I didn't understand: " command)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (getTitle "sauna")
  ; (println 
  ;   (((init-map (player :location)) :dir) :south))
  (loop []
    ;(println (player :location))
    (parseCommand (read-line))
    (recur))
  ;(println "Hello, World!")
  (printPlayer)
  (updateLocation "armory")
  (printPlayer)
  ;(println (player :location))
  (look)
  ;(reduceHealth 5)
  ;(addToInventory "key")
  ;(parseCommand "help me")
  ;(printPlayer)
  ;(removeFromInventory "key")
  ;(printPlayer)
  ;(test)
  ;(parseCommand (read-line))

  )

(-main)
