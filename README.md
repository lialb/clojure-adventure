# clojure-adventure

This is the final project for CS 296 FA2019. This Clojure text-based adventure is made by Albert (Yiliang) Li, Theodore Li, and Matthew Pham (yiliang6, ttl2, and mdpham2 respectively).

## How To Play
Navigate to ```clojure-adventure/interaction/adventure/src/adventure/core.clj``` and run ```lein run``` to get started!


This adventure has quite a few rooms, so a paper and pencil would be helpful. Also, there is one room where you just die from entering :)

## Directions

Using the following commands, try your best to escape out of this old military base!
- help                   : lists the possible commands
- go <diretion>          : changes rooms in the given cardinal direction (north, east, south, west)
- status                 : prints out your current hp, inventory, and current location
- directions             : prints out the possible directions you can go from the current room
- items                  : prints out the current items in the room
- hp                     : prints out your current hp
- look                   : prints the description of the room
- grab <item>            : takes an item in the room
- use <item>             : uses an item
- tick                   : prints out the number of moves you have made
- quit                   : quits the game
