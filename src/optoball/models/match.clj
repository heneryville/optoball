(ns optoball.models.match
  (:require [optoball.models.time :as time])
  )

(defn court [match] (nth match 2))
(defn time [match] (nth match 3))

(defn double-header? [match-1 match-2]

  (= (-> match-1 time time/day)
     (-> match-2 time time/day)))