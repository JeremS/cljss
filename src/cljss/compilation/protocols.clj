(ns cljss.compilation.protocols
  (:use midje.sweet))


(future-fact "the compiled elements must take a rule as a context")

(defprotocol CssSelector
  (compile-as-selector [this]
    "Compile a value considered a selector to a string."))

(defprotocol CssPropertyName
  (compile-as-property-name [this]
    "Compile a value considered a property name to a string."))

(defprotocol CssPropertyValue
  (compile-as-property-value [this]
    "Compile a value considered a property value to a string."))
