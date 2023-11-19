(ns optoball.layouts.sequential
  (:require [optoball.models.time :as time]
            [clojure.math.combinatorics :as combo]
            [optoball.models.league :as league]))


;; Strategy:
;; Work by division
;; Work week to week
;; Create a set of all permutations of team assignments
;; In each week, draw a random assignment from it 


(defn team-combos [teams]
  (combo/combinations teams 2))

(defn greedly-find-unique-pairings
  "Selects from combos, finding a subset that has all teams playing once"
  [combos teams]

  (loop [combos (set combos)
         unused-teams (set teams)
         acc []]

    (cond
      (empty? combos) (throw (ex-info "Ran out of combos" {}))
      (-> unused-teams count (<= 1)) [combos acc] ;; Odd numbers of teams means someones not playing today
      :else (let [[[team-1 team-2] & combos-left] combos
                  available? (boolean (and
                                       (unused-teams team-1)
                                       (unused-teams team-2)))]
              (if available?
                (recur combos-left (-> unused-teams
                                       (disj team-1)
                                       (disj team-2))
                       (conj acc [team-1 team-2]))
              ;; Failed to match this combo, try the next
                (recur combos-left unused-teams acc))))))

#_(second
   (greedly-find-unique-pairings
    (combo/combinations [1 2 3 4 5 6] 2)
    [1 2 3 4 5 6]))

(defn assign-matches
  "Assigns teams (drawn from combos witch represents unused team pairings) to time slots.
   Returns a set of matches that we're going to make."
  [combos slots teams]

  ;; The trick is making sure we don't assign the same team twice.
  ;; We'll iterate through teams, and reject those 

  (let [[combos-left pairings] (greedly-find-unique-pairings combos teams)
        matches (map (fn [[team-1 team-2] {:keys [court-id time]}]
                       [team-1 team-2 court-id time]) pairings slots)]

    [{:combos combos-left
      :slots (drop (count pairings) slots)} ;; Since we naively take just the first slots
     ]
    matches))

;; MKHTODO Teams are playing mulitiple times!
(assign-matches
 (combo/combinations [1 2 3 4 5 6] 2)

 (for [court-num (range 1 3)
       time [[0 1]
             [0 2]
             [0 3]
             [0 4]
             [0 5]
             [0 6]
             [0 7]]]
   {:gym 1
    :court-id (str 1 "." court-num)
    :time time})
 [1 2 3 4 5 6])



(defn layout [{:keys [teams divisions] :as league} {:keys [week-range] :as luts}]
  (let [slots-by-week (group-by (comp time/week :time) slots)
        teams-by-division-id (group-by :division-id teams)
        plan-week (fn [{:keys [combos] :as ctx} week])]

    (reduce (fn [ctx division]
              (let [teams (get teams-by-division-id (:id division))
                    combos (team-combos teams)]
                (reduce (partial plan-week (assoc ctx :combos combos))
                        ctx week-range)))
            {} divisions)))