(ns optoball.quality-test
  (:require
   [clojure.test :refer [deftest is]]
   [optoball.quality :as sut]))


(deftest team-plays-self-test
  (is (= 1
         (sut/team-plays-self [[1 2]
                               [1 1]] {})))

  (is (= 3
         (sut/team-plays-self [[2 2]
                               [1 1]
                               [3 3]] {}))))

(deftest team-plays-out-of-division-test
  (is (= 1
         (sut/team-plays-out-of-division [[1 2]
                                          [1 3]] {:teams-by-id {1 {:division "a"}
                                                                2 {:division "b"}
                                                                3 {:division "a"}}}))))

(deftest court-double-bookings-test
  (is (= 1
         (sut/court-double-bookings [[1 2 1 [1 1]]
                                     [3 4 1 [1 1]]
                                     [3 4 1 [2 1]]
                                     [5 6 2 [1 1]]] {}))))

(deftest teams-without-full-schedule-test
  (is (= 1
         (sut/teams-without-full-schedule [[1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 2]
                                           [1 3]] {}))))

(deftest teams-playing-multiple-times-per-week-test
  (is (= 225
         (sut/teams-have-rest-between-games [[1 2 0 [1 1]]
                                             [1 3 0 [1 2]] ;; Double header will be ignored
                                             [1 4 0 [2 3]] ;; But next-day game won't
                                             [1 5 0 [24 2]]
                                             [6 7 0 [2 2]] ;; Random noise other teams that mean nothing
                                             [8 9 0 [20 2]]] {}))))

(deftest follows-team-time-and-date-restrictions
  (is (= 1
         (sut/follows-team-time-and-date-restrictions
          [[1 2 0 [1 1]]]
          {:teams-by-id {1 {:id 1 :dows #{}} ;; No days of weeks allowed
                         2 {:id 2 :dows #{0 1 2 3 4 5 6 7}}
                         }}))))


(deftest quality-test
  (is (= 1
         (sut/court-double-bookings [[1 2 1 [1 1]]
                                     [3 4 1 [1 1]]
                                     [3 4 1 [2 1]]
                                     [5 6 2 [1 1]]] {}))))

