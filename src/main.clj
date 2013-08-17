(ns main
  (:require [noir.session :as session])
  
  (:use [hiccup.form :only [form-to text-field submit-button]]
			[template :only [template-page]]
			[rest :only [list-movies-by-criteria list-movies-by-title]]))

(defn- get-search-form
	"Show search form."
	[]
	[:div.search_form
		[:p.content_title "Enter search criteria:"]
		(form-to [:post "/search"]
			(text-field {:class "search_field"} :search-criteria)
			(submit-button {:class "search_btn"} "Search"))])

(defn- get-recommended-movies-for-user
	"Get recommended movies for user."
	[]
	(let [user (session/get :user)]
	(list-movies-by-criteria (:movie_genre user) (:min_rating user))))

(defn- print-recomm-movie-data
	"Show recommended movie data."
	[movie]
	[:div.recomm_movie_container
		[:div.recomm_cover_container [:a.recomm_movie_cover_link {:href (str "/movie/"  (:MovieID movie))} [:img.recomm_movie_cover {:src (:CoverImage movie)}]]]
		[:div.recomm_title_container [:a.recomm_movie_title_link  {:href (str "/movie/"  (:MovieID movie))} (:MovieTitle movie)]]
	])

(defn- get-recommended-movies
	"Show recommended movies."
	[]
	[:div.recomendation_container
		[:p.recommendation_title "Our recommendations:"]
		(let [movies (get-recommended-movies-for-user)]
		(for [movie movies]
		(print-recomm-movie-data movie)))
	])

(defn show-home-page 
  "Show home page."
  [] 
  (template-page
    "Home page"
    "home-page"
    [:div.content
	(get-search-form)
	(get-recommended-movies)]))

(defn- print-search-movie-data
	"Show searched movie data."
	[movie]
	[:div.movie_container
		[:div.movie_cover_container [:a.search_movie_cover_link {:href (str "/movie/"  (:MovieID movie))} [:img.movie_cover_img {:src (:CoverImage movie)}]]]
		[:div.search_movie_data
		 [:div.movie_title_container [:a.movie_title_link  {:href (str "/movie/"  (:MovieID movie))} (:MovieTitle movie)]]
		 [:p.movie_details (str "Genre: " (:Genre movie) ", Rating: " (:MovieRating movie) ", Quality: " (:Quality movie) ", Torrent URL: <a class=\"torrent_url\" href=\"" (:TorrentUrl movie) "\">" (:TorrentUrl movie) "</a>, Size: " (:Size movie) ".")]]
	])

(defn- show-search-results
	"Show search results."
	[results]
	[:div.search_results
	 [:p.search_title "Search results:"]
	 (for [movie results]
	 (print-search-movie-data movie))
	])

(defn show-search-page 
  "Show search page."
  [search-criteria]
  (template-page
    "Search results"
    "search-page"
    [:div.content
	(get-search-form)
	(show-search-results (list-movies-by-title search-criteria))
	(get-recommended-movies)]))