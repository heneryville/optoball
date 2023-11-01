(ns optoball.models.lut)

(defn team-by-id [luts team-id]
  (-> luts
      :teams-by-id
      (get team-id)))

(defn division [luts team-id]
  (-> luts
      (team-by-id team-id)
      :division))