(ns interaction.t-core
  (:use midje.sweet)
  (:require [interaction.core :as i]))

(def state {:vars {:x 10 :y 20}})

(facts "about `post-increment`"
  (fact "it increments a variable and returns the previous value."
        (i/post-increment state :x) => [{:vars {:x 11 :y 20}} 10]
        (i/post-increment state :y) => [{:vars {:x 10 :y 21}} 20]
        (i/post-increment state :z) => [{:vars {:x 10 :y 20 :z 1}} 0]
       )
)

(facts "about `lookup-var`"
  (fact "it returns the value for known variable and 0 otherwise."
        (i/lookup-var state :x) => [state 10]
        (i/lookup-var state :y) => [state 20]
        (i/lookup-var state :z) => [state 0]
       )
)

(facts "about `set-plus`"
  (fact "it evaluates known variables and returns the new value."
        (i/set-plus state :z 20 40) => [{:vars {:x 10 :y 20 :z 60}} 60]
        (i/set-plus state :x :y :y) => [{:vars {:x 40 :y 20}} 40]
        (i/set-plus state :y :x 10) => [{:vars {:x 10 :y 20}} 20]
       )
)

(facts "about `set-var`"
  (fact "it evaluates known variables and returns the new value."
        (i/set-var state :z 20) => [{:vars {:x 10 :y 20 :z 20}} 20]
        (i/set-var state :z :x) => [{:vars {:x 10 :y 20 :z 10}} 10]
        (i/set-var state :x :y) => [{:vars {:x 20 :y 20}} 20]
       )
)

(facts "about `there-is-a`"
  (fact "it remembers the given object."
        (i/there-is-a state :shoe) => [{:vars {:x 10 :y 20} :objects {:shoe []}} "There is a shoe."]
        (i/there-is-a state :key) => [{:vars {:x 10 :y 20} :objects {:key []}} "There is a key."]
       )
)

(facts "about `the-obj-is`"
  (fact "it remembers an adjective that applies to an object."
        (i/the-obj-is ((i/there-is-a state :shoe) 0) :shoe :blue) => [{:vars {:x 10 :y 20} :objects {:shoe [:blue]}} "The shoe is blue."]
        (i/the-obj-is ((i/there-is-a state :key) 0) :key :big) => [{:vars {:x 10 :y 20} :objects {:key [:big]}} "The key is big."]
       )
)


(facts "about `describe-obj`"
  (fact "it describes all adjs for the given object."
        (i/describe-obj  {:vars {:x 10} :objects {:shoe [:blue]}} :shoe) => [{:vars {:x 10}, :objects {:shoe [:blue]}} "The shoe is blue."]
        (i/describe-obj  {:vars {:x 10} :objects {:shoe [:blue :big :cute]}} :shoe) => [{:vars {:x 10}, :objects {:shoe [:blue :big :cute]}} "The shoe is blue." "The shoe is big." "The shoe is cute."]
        (i/describe-obj  {:vars {:x 10} :objects {:key [:blue :big :cute]}} :shoe) => [{:vars {:x 10}, :objects {:key [:blue :big :cute]}} "There is no shoe."]
       )
)


(facts "about `forget-obj`"
  (fact "it describes all adjs for the given object."
        (i/forget-obj  {:vars {:x 10} :objects {:shoe [:blue]}} :shoe) => [{:vars {:x 10}, :objects {}} "What shoe?"]
        (i/forget-obj  {:vars {:x 10} :objects {:shoe [:blue] :show [:big :nice]}} :show) => [{:vars {:x 10}, :objects {:shoe [:blue]}} "What show?"]
       )
)

(fact "about `canonicalize`"
  (fact "it strips out whitespaces, lowercases all words, and converts to a vector of keywords"
    (i/canonicalize "There is a spoon") => [:there :is :a :spoon])
    (i/canonicalize "The spoon is blue") => [:the :spoon :is :blue])

(fact "about `react`"
  (fact "apply correct variables to the correct function"a
    (i/react {:vars {:x 10} :runtime i/initial-env} [:postinc :x]) => [{:vars {:x 11} :runtime i/initial-env} 10]
    (i/react (assoc state :runtime i/initial-env) [:lookup :x]) => [(assoc state :runtime i/initial-env) 10] 
    (i/react (assoc state :runtime i/initial-env) [:set :x :to :y :plus :x]) => [(assoc {:vars {:x 30 :y 20}} :runtime i/initial-env) 30] 
    (i/react (assoc state :runtime i/initial-env) [:set :x :to :y :plus 10]) => [(assoc {:vars {:x 30 :y 20}} :runtime i/initial-env) 30] 
    (i/react (assoc state :runtime i/initial-env) [:set :y :to 10]) => [(assoc {:vars {:x 10 :y 10}} :runtime i/initial-env) 10] 
    (i/react {:vars {:x 10} :runtime i/initial-env} [:there :is :a :spoon]) => [{:vars {:x 10} :runtime i/initial-env :objects {:spoon []}} "There is a spoon."]
    (i/react {:vars {:x 10} :objects {:spoon []} :runtime i/initial-env} [:the :spoon :is :plastic]) => [{:vars {:x 10} :objects {:spoon [:plastic]} :runtime i/initial-env} "The spoon is plastic."]
    (i/react {:vars {:x 10} :objects {:spoon [:plastic :blue]} :runtime i/initial-env} [:describe :spoon]) => [{:vars {:x 10} :objects {:spoon [:plastic :blue]} :runtime i/initial-env} "The spoon is plastic." "The spoon is blue."]
    (i/react {:vars {:x 10} :objects {:spoon [:plastic :blue]} :runtime i/initial-env} [:forget :spoon]) => [{:vars {:x 10} :objects {} :runtime i/initial-env} "What spoon?"]))