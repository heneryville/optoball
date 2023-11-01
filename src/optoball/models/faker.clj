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

(def DIVISIONS 10)
(def TEAMS-PER-DIVISIONS 30)

(defn make-team [id division]
  {:id id
   :division division})

(defn make-teams []
  (for [division (range 1 (inc DIVISIONS))
        team (range 1 (inc TEAMS-PER-DIVISIONS))]
    {:id (str division "." team)
     :division division}))

(defn make-schedule []
  (for [week (range 1 (inc WEEKS))
        dow DAY-OF_WEEK
        time TIMES-OF_DAY]
    [(+ week dow) time]))

(defn make-slots []
  (for [gym (range 1 (inc GYMS))
        court-num (range 1 (inc (rand-nth COURTS-PER-GYM)))
        time (make-schedule)]
    {:gym gym
     :court (str gym "." court-num)
     :time time}))