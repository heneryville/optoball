(ns optoball.models.time)

(defn day-of-week
  "Gets the day of week of a date object"
  [[date _]]
  (mod date 7))

(defn week
  "Gets which week into the scheduling the date object is on"
  [[date _]]
  (quot date 7))

(defn day [[date _]]
  date)

(defn- ->hours
  "Converts a date object into number of hours since the start of the calculation"
  [[date time-of-day]]
  (+ (* date 24) time-of-day))

(defn hours-between
  "Determines how many hours are between two dates"
  [d1 d2]
  (- (->hours d1) (->hours d2)))