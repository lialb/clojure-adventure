(ns interaction.core)
(require '[clojure.string :as str])

;; # Interaction
;; A simple parser that can remember facts.
;;

;; # Actions
;;
;; Let an *action* be a function with a specific
;; interface: It will take in a *state* followed by
;; a sequence of arguments.  It will return a vector
;; consisting of a new state and a response string.
;;
;; Here is a simple example, a post-increment function.
;; Assume the state is a hash-map with a key :vars
;; and a value containing a hash of the variables and
;; their values.


(defn post-increment
  "Action: post-increment
   Returns the value of a variable from the state and increments it."
  [state var]
  (if-let [val (get-in state [:vars var])]
    [(update-in state [:vars var] inc) val]
    [(assoc-in state [:vars var] 1) 0]))

;; <pre><code>
;; interaction.core=> (post-increment {:vars {:x 10}} :x)
;; [{:vars {:x 11}} 10]
;; </code></pre>

;; ## Your work
;;
;; Fill in the code for these functions.
;;

(defn look [state var]
  (cond (integer? var) var
    (keyword? var) ((state :vars) var)
    :else 0
  )
)


"Given a state and a variable name, return the value of the variable
if it has been defined, otherwise return 0.
"
(defn lookup-var [state var]
  (cond (nil? (look state var)) [state 0] 
        :else [state (look state var)]
  )
)
(def state {:vars {:x 10 :y 20}})
;(println (lookup-var state :z))

;; <pre><code>
;; interaction.core=> (lookup-var {:vars {:x 10}} :x)
;; [{:vars {:x 10}} 10]
;; </code></pre>
"Action: set-plus.  Set var = e1 + e2, return the sum as a result."
(defn set-plus [state var e1 e2]
  (let [a (look state e1)
        b (look state e2)]
      (if (nil? (state :runtime))
        [{:vars (assoc (state :vars) var (+ a b))} (+ a b)]
        [{:runtime (state :runtime) :vars (assoc (state :vars) var (+ a b))} (+ a b)]
      )
  )
)
;(println (set-plus state :x :y :y))
;(println (set-plus state :z 20 40))

;; <pre><code>
;; interaction.core=> (set-plus {:vars {:x 10}} :y :x 20)
;; [{:vars {:x 10 :y 30}} 30]
;; </code></pre>
"Action: set-var. Set var = e1.  Return the new value as a result."
(defn set-var [state var e1]
  (let [a (look state e1)]
    (if (nil? (state :runtime))
      [{:vars (assoc (state :vars) var a)} a]
      [{:runtime (state :runtime) :vars (assoc (state :vars) var a)} a]
    )
  )
)

;(println (set-var state :z 20))
;(println (set-var state :x :y))
;; <pre><code>
;; interaction.core=> (set-var {:vars {:x 10}} :y :x)
;; [{:vars {:x 10 :y 10}} 10]
;; </code></pre>
"Action: there-is-a.  Remember that an object obj exists.
Returns \"There is a obj\" as a result."
(defn there-is-a [state object]
  [(assoc state :objects {object []}) (str "There is a " (str/lower-case (str/replace (str object) #":" "")) ".")]
)

;(println (there-is-a state :shoe))
;(println (there-is-a state :key))
;; <pre><code>
;; interaction.core=> (there-is-a {:vars {:x 10}} :shoe)
;; [{:vars {:x 10 :y 10}
;;   :objects {:shoe []}} "There is a shoe."]
;; </code></pre>
"Action: there-obj-a.  Remember an adjective that applies to an object.
Returns \"The obj is adj\" as a result."
(defn the-obj-is [state object adj]
  [(assoc state :objects {object [adj]}) (str "The " (str/lower-case (str/replace (str object) #":" "")) " is " (str/lower-case (str/replace (str adj) #":" "")) ".")]
)

;(println (the-obj-is ((there-is-a state :shoe) 0) :shoe :blue))

;; <pre><code>
;; interaction.core=> (the-obj-is {:vars {:x 10} :objects {:shoe []}} :shoe :blue)
;; [{:vars {:x 10} :objects {:shoe [:blue]}} "The shoe is blue."]
;; </code></pre>


"Describe the given object \"The obj is adj\" if it exists in state . If not, return \"There is no obj\""
(defn describe-obj [state object]
  (if (nil? ((state :objects) object))
      [state (str "There is no " (str/lower-case (str/replace (str object) #":" "")) ".")]
      (do (def result [state])
          (def lol ((state :objects) object))
          ;(println "Count: " (count ((state :objects) object)))
          (loop [i 0]
            (when (< i (count ((state :objects) object)))
              (def result (conj result (str "The " (str/lower-case (str/replace (str object) #":" "")) " is " (str/lower-case (str/replace (str (get lol i)) #":" "")) ".")             
              ))
              (recur (inc i))
            )
          )      
          result
      )
  )
  
)
;(println (describe-obj  {:vars {:x 10} :objects {:key [:blue :big :cute]}} :key))
;; <pre><code>
;; interaction.core=> (describe-obj  {:vars {:x 10} :objects {:shoe [:blue :big]}} :shoe)
;; [{:vars {:x 10}, :objects {:shoe [:blue :big]}} "The shoe is blue." "The shoe is big."]
;; </code></pre>

"Delete the given object and return \"What obj?\""
(defn forget-obj [state object]
  [(update-in state [:objects] (fn [nested] (apply dissoc nested [object]))) (str "What " (str/lower-case (str/replace (str object) #":" "")) "?")]
)

;(println (forget-obj  {:vars {:x 10} :objects {:shoe [:blue]}} :shoe))
;(println (forget-obj  {:vars {:x 10} :objects {:shoe [:blue] :show [:big :nice]}} :show))

;; <pre><code>
;; interaction.core=> (forget-obj {:objects {:show [:exciting]}} :show)
;; [{:objects {}} "What show?"]
;; </code></pre>


;; # Action Environment
;;
;; The runtime environment is a vector of the form
;;
;; ``` [ phrase action phrase action ...]```
;;
;; The "phrase" is a canonicalized vector of keywords.
;; (Remember that a keyword is a symbol starting with a colon, like :name.)
;; The `@` character will represent a variable.

(def initial-env [  [:postinc "@"] post-increment  ])  ;; add your other functions here

;; # Parsing
;;
;; This is mostly code from the lecture.
"Given an input string, strip out whitespaces, lowercase all words, and convert to a vector of keywords."
(defn canonicalize [input]
  (def inList (str/split (str/replace (str/lower-case input) #"[?.!]" "") #" +"))
  (def canon [])
  (loop [i 0]
    (when (< i (count inList))
      (def canon (conj canon (keyword (get inList i))))
      (recur (inc i))
    )  
  )
  canon
)
;(println  (canonicalize "There is a spoon"))
;; <pre><code>
;; interaction.core> (canonicalize "The shoe is blue.")
;; [:the :shoe :is :blue]
;; </code></pre>
"Given a state and a canonicalized input vector, search for a matching phrase and call its corresponding action.
If there is no match, return the original state and result \"I don't know what you mean.\""
(defn react [state input-vector]
  (cond (= 0 (compare (name (get input-vector 0)) "postinc")) (post-increment state (get input-vector 1))
        (= 0 (compare (name (get input-vector 0)) "lookup")) (lookup-var state (get input-vector 1))
        (= 0 (compare (name (get input-vector 0)) "describe")) (describe-obj state (get input-vector 1))
        (= 0 (compare (name (get input-vector 0)) "forget")) (forget-obj state (get input-vector 1))
        (= 0 (compare (name (get input-vector 0)) "there")) (there-is-a state (get input-vector 3))
        (= 0 (compare (name (get input-vector 0)) "the")) (the-obj-is state (get input-vector 1) (get input-vector 3))
        (= 0 (compare (name (get input-vector 0)) "set")) 
          (do
            (if (> (count input-vector) 4)
              (set-plus state (get input-vector 1) (get input-vector 3) (get input-vector 5))
              (set-var state (get input-vector 1) (get input-vector 3))
            )
          )
        :else [state "I dont know what you mean."]
  )
)
;(println (react (assoc state :runtime initial-env) [:set :x :to :y :plus 10]))
;(println (react {:vars {:x 10} :runtime initial-env} [:postinc :x]))
;(println (react (assoc state :runtime initial-env) [:lookup :x]))

;(println (react (assoc state :runtime i/initial-env) [:set :x :to :y :plus :x]))

;(println (react {:vars {:x 10} :objects {:spoon [:plastic :blue]} :runtime initial-env} [:describe :spoon]))
;; <pre><code>
;; interaction.core> (react {:vars {:x 10} :runtime initial-env} [:postinc :x])
;; [ {:vars {:x 11} :runtime { ... omitted for space ... }}  10]
;; </code></pre>

(defn repl
  "Start a REPL using the given environment.  The :runtime key should map to the action environment.
  Prints out the result and loops the state for another round.  Quits when you say bye.
  You may need (flush) to print out '>' without a newline "
  [env]
  )

;; <pre><code>
;; interaction.core=> (repl initial-env)
;; Welcome!  Let's talk.
;; > there is a spoon.
;; There is a spoon.
;; > the spoon is blue.
;; The spoon is blue.
;; > the spoon is big.
;; The spoon is big.
;; > describe the spoon.
;; The spoon is blue.
;; The spoon is big.
;; > forget the spoon.
;; What spoon?
;; > describe the spoon.
;; There is no spoon.
;; > bye
;; nil
;; interaction.core=> 
;; </code></pre>

(defn main
  "Start the REPL with the initial environment."
  []
  (repl initial-env)
  )
