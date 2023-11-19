(ns optoball.models.match
  (:require [optoball.models.time :as time]))

(defn court [match] (nth match 2))
(defn time [match] (nth match 3))
(defn team-1 [match] (first match))
(defn team-2 [match] (second match))

(defn team-plays-other? [match other]
  (or (= other (first match))
      (= other (second match))))

(defn double-header? [team match-1 match-2]
  (and (team-plays-other? match-1 team)
       (team-plays-other? match-2 team)
       (= (-> match-1 time time/day)
          (-> match-2 time time/day))))