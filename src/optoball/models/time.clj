(ns optoball.models.time)


(defn day-of-week [[date _]]
  (mod date 7))

(defn week [[date _]]
  (quot date 7))

(defn day [[date _]]
  date)

(defn- ->hours [[date time-of-day]]
  (+ (* date 24) time-of-day))

(defn hours-between [d1 d2]
  (- (->hours d1) (->hours d2)))