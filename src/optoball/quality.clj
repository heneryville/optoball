(ns optoball.quality
  (:require
   [optoball.models.lut :as lut]
   [optoball.models.time :as time]
   [optoball.models.league :as league]
   [optoball.models.match :as match]
   [optoball.models.team :as team]
   ))

;; Consistency Rules
;; * Team never plays iteslf
;; * Team plays only others in the same division
;; * Court has only one assignment at a time
;; * All teams play a minimum number of games

(defn team-plays-self [matches _luts]
  (->> matches
       (filter (fn [[team1 team2 _]] (= team1 team2)))
       count))

(defn team-plays-out-of-division [matches luts]
  (->> matches
       (filter (fn [[team1 team2 _]] (not= (lut/division luts team1)
                                           (lut/division luts team2))))
       count))

(defn court-double-bookings [matches _luts]
  (->> matches
       (map (juxt match/court match/time))
       frequencies
       vals
       (filter #(> % 1))
       count))

(defn teams-without-full-schedule [matches _luts]
  (->> matches
       (map (juxt first second))
       (mapcat identity)
       frequencies
       vals
       (filter #(< % league/GAMES-IN-SEASON))
       count))

(defn- matches-by-team [matches]
  (merge-with concat (group-by first matches)
              (group-by second matches)))

;; Hard rules
;; * Team plays games more than some days apart
;; * Team follows DOW, time, and date constraints
;; * Coach available
;; * Coach doesn't move gyms
;; * Double header preference is followed

(defn teams-have-rest-between-games [matches _luts]
  (->> matches
       matches-by-team
       (mapcat (fn [[team matches]]
                 (->> matches
                      (sort-by match/time)
                      (partition 2 1) ;; Adjacent pairs
                      ;; Double headers are exceptional and handled elsewhere
                      (remove (fn [[m1 m2]] (match/double-header? team m1 m2))))))
       (map #(time/hours-between (-> % second match/time) (-> % first match/time)))
       (filter #(< % league/INTER-GAME-SPACING-HOURS))
       (map (fn [hours-apart]
              (let [hours-from-allowed (- league/INTER-GAME-SPACING-HOURS hours-apart)]
                (* hours-from-allowed hours-from-allowed)))) ;; Squaring this makes small violations not a big deal, but big ones huge
       (reduce + 0)))

(defn follows-team-time-and-date-restrictions [matches luts]
  (->> matches
       (mapcat (fn [match]
                 [[(->> match match/team-1 (lut/team-by-id luts)) (match/time match)]
                  [(->> match match/team-2 (lut/team-by-id luts)) (match/time match)]]))
       (remove (fn [[team time]]
                 (team/can-play-at-time? team time)))
       count))

;; Soft Rules
;; * Minimize distance to games
;; * Minimize time between double headers
;; * Don't play avoided teams
;; * Play preferred sometime in the season teams
;; * Double headers have as little time between them as possible


(defn quality [assignments luts]
  (map (fn [rules]
         (->> rules
              (map (fn [rule] (rule assignments luts)))
              (reduce + 0)))
       [[team-plays-self team-plays-out-of-division court-double-bookings teams-without-full-schedule]
       ;; MKHTODO Normalize these values into miles or something
        [teams-have-rest-between-games follows-team-time-and-date-restrictions]
        []]))

;; MKHTODO More rules:
;; * Plays within division
;; * Double headers are directly adjacent in time. Don't space them in time because idle teens are the devil's play things
;; * Coach restrictions