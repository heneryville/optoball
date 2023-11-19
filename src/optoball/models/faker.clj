(ns optoball.models.faker)

;; CourtSlot
;;   -Court
;;     - Gymn
;;   - Time

;; League
;; Division
;; Team
;;  - Day of Week
;;  - Date
;;  - Time
;;  - Distance to game
;;  - Coach availability
;;  - Double header
;;  - Must Play
;;  - Avoid team
;;  - 1 game per week

;; Time
;;  - Date
;;  - Time
;;  -> Day of Week
;;  -> Week #

;; Matchup = team x team x court x time

(def GYMS 10)
(def COURTS-PER-GYM [2 2 2 2 3 4 4])
(def WEEKS 9)
(def DAY-OF_WEEK [0 4 5 6])
(def TIMES-OF_DAY (range 10 23))

(def DIVISIONS 3)
(def TEAMS-PER-DIVISIONS 30)

(defn make-team [id division-id]
  {:id id
   :division-id division-id
   :dows (set (random-sample 0.95 (range 7)))
   :exclusion-dates (set (take (rand-int 4) (random-sample 0.02 (range))))
   :times-always (random-sample 0.05
                                ['(> 3)
                                 '(> 4)
                                 '(< 16)
                                 '(< 20)])
   ;; MKHTODO Time restrictions on a specific date
   })

(defn make-division [id]
  {:id (str id)
   :dow (if (odd? id)
          #{0 1 2}
          #{2 3})})


(defn make-teams [divisions]
  (for [division divisions
        team (range 1 (inc TEAMS-PER-DIVISIONS))]
    (make-team (str (:id division) "." team) (:id division))))

(defn make-schedule []
  (for [week (range 1 (inc WEEKS))
        dow DAY-OF_WEEK
        time TIMES-OF_DAY]
    [(+ week dow) time]))

;; MKHTODO Gyms & courts should have some kind of base configuration, not just a bunch of slots
(defn make-slots []
  (for [gym (range 1 (inc GYMS))
        court-num (range 1 (inc (rand-nth COURTS-PER-GYM)))
        time (make-schedule)]
    {:gym gym
     :court-id (str gym "." court-num)
     :time time}))


(defn make-league []
  (let [divisions (for [div (range 1 (inc DIVISIONS))] (make-division div))
        teams (make-teams divisions)
        slots (make-slots)]
    {:league "fake-league"
     :divisions divisions
     :teams teams
     :slots slots
     }))


(make-league)