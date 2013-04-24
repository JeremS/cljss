(ns cljss.precompilation
  (:require [clojure.string :as string]))


(defrecord Decorator [env f])

(defn- uuid [] (java.util.UUID/randomUUID))


(defn decorator
  "Construct a decorator, from a function that 
  decorate, transforms a rule given a environment.
  
  When no environment is provided
  an empty map is used as the default one.
  
  Decorator are used to process rules trees results of 
  parsing rules as expressed in the dsl."
  ([f] (decorator {} f))
  ([env f]
   (let [id (uuid) 
         env {id env}]                  ; generate a new global env
     (Decorator. env                    ; create decorator with general env and a wrappred decoration function
      (fn [v general-env]               ; the new decoration function takes 
        (let [local (get general-env id); recovers the env for this decorator
              [new-v new-local]  (f v local) ; decorate the value
              new-general (assoc general-env id new-local)] ; create a new value for the general env
          (list new-v new-general))))))) ; returns the new value and the new general env

(defn- chain-2-decorators [d1 d2]
  (let [{f1 :f env1 :env} d1
        {f2 :f env2 :env} d2]
    (Decorator. (merge env1 env2)
     (fn [r env]
       (let [[r env] (f1 r env)]
         (f2 r env))))))

(defn chain-decorators 
  "Allows to compose from left to right
  the behaviour of decorators."
  [d1 d2 & ds]
  (reduce chain-2-decorators 
          (list* d1 d2 ds)))

(defn- dr [r f env]
  (let [[new-r new-env] (f r env)
        new-sub-rules (map #(dr % f new-env)
                          (:sub-rules r))]
    (assoc new-r 
      :sub-rules new-sub-rules)))

(defn decorate-rule 
  "Applies a decorator to a rule and recursively 
  to its sub rules."
  [r {:keys [f env]}]
  (dr r f env))

(defn flatten-rule 
  "Given a rule returns a flatten list of the rule and its
  sub rules"
  [{:as r}]
  (let [new-r (assoc r :sub-rules '())
        sub-rs (:sub-rules r)]
    (cons new-r
          (mapcat flatten-rule sub-rs))))

(defn precompile-rule 
  "Decorate a rule then flattens it."
  [r deco]
  (-> r
      (decorate-rule deco)
      (flatten-rule)))


