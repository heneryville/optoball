(ns optoball.models.team-test
  (:require [clojure.test :refer [deftest is are]]
            [optoball.models.team :as sut]))

(def sample-team {:id 1
                  :dows #{0 1 2 3 4 5}
                  :exclusion-dates #{11}
                  :times-always ['[> 3]]})

(deftest can-play-at-time?-test
  (are [team time expected] (= expected (sut/can-play-at-time? team time))
    sample-team [0 5] true ;; Valid time
    sample-team [6 5] false ;; Bad dow
    sample-team [11 5] false ;; Exclusion date
    sample-team [0 1] false ;; Fails time predicate
    ))