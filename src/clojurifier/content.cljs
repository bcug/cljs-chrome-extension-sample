(ns clojurifier.content
  (:require [clojurifier.mutant :refer [observe-body]]
            [clojurifier.clojurifier :refer [clojurify]])
  (:require-macros [dommy.macros :refer [sel1]]))

(defn init 
  "Clojurifies everything on the page and starts observing the body element for changes"
  []
  (clojurify (sel1 :body))
  (observe-body))


