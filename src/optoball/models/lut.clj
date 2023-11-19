(ns optoball.models.lut
  (:require [optoball.utils :refer [index-by]]
            [optoball.models.time :as time]))


(defn index [league]
  (let [index-by-id (fn [key] (->> league key (index-by :id)))]
    {:teams-by-id (index-by-id :teams)
     :divisions-by-id (index-by-id :division)
     :week-range (->> league :slots (map (comp time/week :time)) distinct)
     
     }))

(defn team-by-id [luts team-id]
  (-> luts
      :teams-by-id
      (get team-id)))

(defn division [luts team-id]
  (-> luts
      (team-by-id team-id)
      :division))