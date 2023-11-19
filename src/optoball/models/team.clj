(ns optoball.models.team
  (:require [optoball.models.time :as time]))

;; MKHTODO put in a malli spec for these kinds of objects

;; {:id id
;;    :division division
;;    :dows (set (random-sample 0.95 (range 7)))
;;    :exclusion-dates (set (take (rand-int 4) (random-sample 0.02 (range))))
;;    :times-always (random-sample 0.05
;;                                 ['(> 3)
;;                                  '(> 4)
;;                                  '(< 16)
;;                                  '(< 20)])
;;    ;; MKHTODO Time restrictions on a specific date
;;    }

(defn run-time-comparison-clause [[op operand] time-of-day]
  (let [full-clause (list op time-of-day operand)]
    (eval full-clause)))

(defn can-play-at-time? [team time]
  (let [[date time-of-day] time]
    (and
     (-> team :exclusion-dates (contains? date) not)
     (->> team :times-always (every? #(run-time-comparison-clause % time-of-day)))
     (-> team :dows (contains? (time/day-of-week time)))
     )))