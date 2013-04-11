(ns cljss.selectors-test
  (:require [midje.repl :as m])
  (:use cljss.selectors))

(m/facts "About selector combination"
         (m/fact "string and keyword selectors can be combined
                 and give a path like selector"
                 (combine :div :a)   => [:div :a]
                 (combine "div" "a") => ["div" "a"]
                 (combine :div "a")  => [:div "a"]
                 (combine "div" :a)  => ["div" :a])
         
         (m/fact "combining strings or keyword selectors with a vector
                 gives a concatenation of the two giving a path like "
                 (combine :div  [:p :a]) => [:div :p :a]
                 (combine "div" [:p :a]) => ["div" :p :a]
                 
                 (combine [:div :p] :a)  => [:div :p :a]
                 (combine ["div" :p] :a) => ["div" :p :a])
         
         (m/fact "combining one of the previous kind of selectors
                 with a set selector gives the set of the combinations"
                 (combine #{:div :section} :p)
                 => #{[:div :p][:section :p]}
                 
                 (combine :p #{:div :section})
                 => #{[:p :div][:p :section]}
                 
                 (combine #{:div :section} [:p :a])
                 => #{[:div :p :a][:section :p :a]}
                 
                 (combine [:p :a] #{:div :section})
                 => #{[:p :a :div][:p :a :section]}
                 
                 (combine #{:div :section} #{:p :a})
                 => #{[:div :p] [:div :a] 
                      [:section :p] [:section :a]}))

(m/future-fact "Finish intergration of the the parent selector -> extend type etc...")
(m/future-fact 
 "See if there is a way to split everything like lenght or colors.
 each type would have its own file, containing its parsing, decorator, compilation strategy.")