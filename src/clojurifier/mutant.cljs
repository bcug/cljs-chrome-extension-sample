(ns clojurifier.mutant
  (:require [khroma.log :as console]
            [clojurifier.clojurifier :refer [clojurify]]))


; so we can use the circular dependency between observe-element and handle-mutation
(declare observe-element)

(defn handle-mutation 
  "Handles the change of an element. Clojurifies it and starts observing its children."
  [mutation]
  (clojurify (.-target mutation))
  (doseq [child (-> mutation .-target .-childNodes (array-seq 0))]
    (observe-element child)))

(defn handle-mutations 
  "Handles the incoming mutations one by one"
  [mutations]
  (doseq [m (js->clj mutations)]
    (handle-mutation m)))

(defn observe-element
  "Starts observing an element, calling handle-mutations on every attribute, child or text data change"
  [elem]
  (let [observer (js/MutationObserver. handle-mutations)]
    (.observe observer elem
              (clj->js {:attributes true :childList true :characterData true}))))

(defn observe-body 
  "Observes the body element for changes, indirectly watches all of it's children too"
  []
  (observe-element js/document.body))

