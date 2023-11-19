(ns optoball.models.io
  (:require [clojure.edn :as edn]
            [optoball.models.lut :as lut]
            [optoball.models.faker :as faker]))


(defn ->edn [league]
  (spit  "fake-league.edn" (pr-str league))
  true
  )

(defn <-edn []
  (let [league (edn/read-string (slurp "fake-league.edn"))
        lut (lut/index league)]
    [league lut]))