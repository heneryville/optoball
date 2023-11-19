(ns optoball.utils)


(defn index-by [f col]
  (->> col
       (map (juxt f identity))
       (into {})))