(ns favorites
  (:require [noir.session :as session])
  
  (:use  [hiccup.form :only [form-to hidden-field submit-button]]
			[template :only [template-page]]
			[mongodb :only [get-movies-for-user delete-user-movie]]))

(defn- print-movie-data
	"Show movie data."
	[movie]
	[:div.movie_container
		[:div.movie_cover_container [:a.movie_cover_link {:href (str "/movie/"  (:movie-id movie))} [:img.movie_cover_img {:src (clojure.string/replace (:cover-img movie) #"\\" "/")}]]]
		[:div.movie_data
		 [:div.movie_title_container [:a.movie_title_link  {:href (str "/movie/"  (:movie-id movie))} (:title movie)]]
		 [:p.movie_details (str "Genre: " (:genre movie) ", Rating: " (:rating movie) ", Quality: " (:quality movie) ", Torrent URL: <a class=\"torrent_url\" href=\"" (:torent-url movie) "\">" (:torent-url movie) "</a>, Size: " (:size movie) ".")]]
		[:div.remove_favorite
		 (form-to [:delete "/favorite"]
          (hidden-field :id (:_id movie))
          (submit-button {:class "remove_favorite_btn" :style "width: auto !important;"} "Remove from favorites"))]])

(defn- show-favorite-movies
	"Show favorite movies."
	[movies]
		[:div.content
		 [:img.favorites_logo {:src "/images/favorites-logo.png"}]
		 [:img.favorites_logo {:src "/images/favorites-logo.png"}]
		 [:p.favorites_content_title "Your favorite list"]
		 (for [movie movies]
		 (print-movie-data movie))])

(defn- show-no-favorite-movies
	"Show no favorite movies."
	[]
	[:div.content
	 [:p.content_title "You don't have any movie(s) in your favorite list."]
	 [:a.go_to_serach {:href "/"} "Go to search for one."]
	 [:div.no_favorites_img_container [:img.no_favorites_img {:src "/images/smile.jpg"}]]])

(defn show-favorites-page 
  "Show favorites page."
  [] 
  (template-page
    "Favorite movies"
    "favorites-page"
	 (let [favorite-movies (get-movies-for-user (:_id (session/get :user)))]
	 (if (empty? favorite-movies) (show-no-favorite-movies) (show-favorite-movies favorite-movies)))))

(defn do-delete-favorite 
  "Delete user favorite movie."
  [id]
    (delete-user-movie id))