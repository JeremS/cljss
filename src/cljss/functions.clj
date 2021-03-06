;; ## Functions
;; CSS 2 then CSS 3 have brought some functions
;; in the specification.

(ns cljss.functions
  (:require [clojure.string :as string])
  (:use cljss.protocols
        cljss.selectors.protocols
        [cljss.compilation :only (compile-seq-then-join)]))

;; Generic compilation of as css function.

(defn compile-function [name args compile-fn]
  (str (compile-fn name)
       \(
         (compile-seq-then-join args compile-fn ", ")
       \)))


(defrecord CssFunction [name args]
  CssSelector
  (compile-as-selector [_]
    (compile-function name args compile-as-selector))
  (compile-as-selector [this _]
    (compile-as-selector this))

  CssPropertyValue
  (compile-as-property-value [_]
    (compile-function name args compile-as-property-value)))


;; Template to define css function constructors.

(defmacro defcssfunction
  ([f-name]
   (list 'defcssfunction f-name (str f-name)))
  ([f-name f-str]
   `(defn ~f-name [& ~'args]
      (CssFunction. ~f-str ~'args))))

(defmacro defcssfunctions [& f-names]
  `(do
     ~@(for [f-name f-names]
         (list 'defcssfunction f-name))))

;; Use of the template to define the functions from the spec.

(defcssfunctions
  ; css2 functions
  url counter attr calc

  ;css3 transform functions
  matrix translate translateX translateY
  scale scaleX scaleY
  rotate skewX skewY

  matrix3d translate3d translateZ
  scale3d scaleZ
  rotate3d rotateX rotateY rotateZ

  perspective

  ; gradient
  linear-gradient radial-gradient
  repeating-linear-gradient repeating-radial-gradient)
