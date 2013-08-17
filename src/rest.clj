(ns rest
  (:require [clojure.data.json :as json]))
			
(defn- get-data 
  "Get data from specified uri."
  [uri]
     (json/read-str (slurp uri) :key-fn keyword))
	 
(defn get-movie-details 
  "Get movie details by movie id."
  [id]
     (get-data (str "http://yify-torrents.com/api/movie.json?id=" id)))
	 
(defn list-movies-by-criteria 
  "Get movies by user criteria."
  [genre min-rating]
     (:MovieList (get-data (str "http://yify-torrents.com/api/list.json?genre=" genre "&rating=" min-rating "&limit=6&sort=date&order=desc"))))
	 
(defn list-movies-by-title 
  "Get movies by title."
  [title]
     (:MovieList (get-data (str "http://yify-torrents.com/api/list.json?keywords=" title "&sort=date&order=desc"))))