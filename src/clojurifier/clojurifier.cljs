(ns clojurifier.clojurifier
  (:require [clojure.string :as s]
            [khroma.log :as console]))


; the strings we want to replace
(def mapping {(js/RegExp. "haskell" "i") "Clojure"
                (js/RegExp. "scala" "i") "Clojure"})


(defn clojurize
  "Replaces all matched strings by 'mapping' in the element"
  [element] 
  (doseq [[k v] mapping]
    (set! (.-textContent element) (.replace (.-textContent element) k v))))


(defn- clojurable?
  "Decides if we want to try finding matching strings in the element.
  Script and textarea elements are out of our scope."
  [node]
  (and (= (.-nodeType node) 1) (not (#{"script" "textarea"} (.-nodeName node)))))


(defn clojurify
  "Replaces all matched strings by 'mapping' in the element and its children if the element is a text element"
  [element]
  (let [nn (s/lower-case (.-nodeName element))]
    (if (= nn "#text")
      (clojurize element)
      (when (clojurable? element)
        (dotimes [i (.-length (.-childNodes element))]
          (clojurify (.item (.-childNodes element) i) clojurize))))))
