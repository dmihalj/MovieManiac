(ns movie
  (:require [noir.session :as session]
				[clj-time.format :as time-format])
  
  (:use  [hiccup.form :only [form-to hidden-field submit-button]]
			[template :only [template-page]]
			[mongodb :only [get-user-movie delete-user-movie insert-user-movie]]
			[rest :only [get-movie-details]]))

(def parser-formatter (time-format/formatter "yyyy-MM-dd HH:mm:ss"))
  
(def unparser-formatter (time-format/formatter "HH:mm dd/MM/YY"))
 
(defn- print-add-to-favorite-status
	"Show add to favorite status."
	[movie]
	[:div.favorite_staus_container
	[:img.favorite_logo {:src "/images/favorites-logo.png"}]
	[:p.favorite_status_lbl "Do you want to add this movie to your favorite list."]
	(form-to [:put "/movie"]
		  (hidden-field :movie-id (:MovieID movie))
		  (hidden-field :title (:MovieTitle movie))
		  (hidden-field :cover-url (:MediumCover movie))
		  (hidden-field :genre (:Genre1 movie))
		  (hidden-field :rating (:MovieRating movie))
		  (hidden-field :torent-url (:TorrentUrl movie))
		  (hidden-field :quality (:Quality movie))
		  (hidden-field :size (:Size movie))
          (submit-button {:class "add_favorite_btn"} "Add"))])

(defn- print-added-to-favorite-status
	"Show added to favorite status."
	[movie]
	[:div.favorite_staus_container
	[:img.favorite_logo {:src "/images/favorites-logo.png"}]
	[:p.favorite_status_lbl "You have added this movie to your favorite list."]
	(form-to [:delete "/movie"]
          (hidden-field :id (:_id movie))
		  (hidden-field :movie-id (:movie-id movie))
          (submit-button {:class "remove_favorite_btn"} "Remove"))])

(defn- print-favorite-status
	"Show appropriate favorite button."
	[movie]
	(let [user-movie (get-user-movie (:_id (session/get :user)) (:MovieID movie) )] (if user-movie (print-added-to-favorite-status user-movie) (print-add-to-favorite-status movie))))
 
(defn- show-movie-details
	"Show movie details."
	[movie]
	[:div.movie_header
	[:div.movie_header_cover_container [:img.movie_header_cover {:src (:LargeCover movie)}]]
	[:div.movie_header_details_container
	 [:p.movie_header_upload_date (str "Uploaded time: " (time-format/unparse unparser-formatter (time-format/parse parser-formatter (:DateUploaded movie))))]
	 [:p.movie_header_title (:MovieTitle movie)]
	 [:p.movie_header_details (str "Genre: " (:Genre1 movie) ", " (:Genre2 movie))]
	 [:p.movie_header_details (str "Duration: " (:MovieRuntime movie) " minutes")]
	 [:p.movie_header_details (str "Language: " (:Language movie))]
	 [:p.movie_header_details (str "Subtitles: " (:Subtitles movie))]
	 [:p.movie_header_details (str "Rating: " (:MovieRating movie))]
	 [:div.movie_header_actions
	  [:a.imdb_link {:href (:ImdbLink movie) :target "_blank"} [:img.imdb_logo {:src "/images/imdb-logo.jpg" :alt "IMDB"}]]
	  (print-favorite-status movie)]]])

(defn- show-movie-description
	"Show movie description."
	[movie]
	[:div.movie_description
	 [:p.movie_description_lbl "Description: "]
	 [:p.movie_description_content (:LongDescription movie)]])

(defn- show-movie-technical-specs
	"Show movie technical specs."
	[movie]
	[:div.movie_technical_specs
	 [:p.movie_technical_specs_lbl "Technical specs: "]
	 [:p.movie_technical_specs_content (str "Quality: " (:Quality movie) ", Resolution: " (:Resolution movie) ", FrameRate: " (:FrameRate movie) ", Size: " (:Size movie))]])
	 
(defn- show-movie-screenshots
	"Show movie screenshots."
	[movie]
	[:div.movie_screenshots
	 [:p.movie_screenshots_lbl "Screenshots: "]
	 [:div [:img.movie_screenshot {:src (:LargeScreenshot1 movie)}]]
	 [:div [:img.movie_screenshot {:src (:LargeScreenshot2 movie)}]]])

(defn show-movie-page 
  "Show movie details page."
  [id] 
  (let [movie (get-movie-details id)]
  (template-page
    (str "Movie: " (:MovieTitle movie))
    "movie-page"
	[:div.content
	(show-movie-details movie)
	(show-movie-description movie)
	(show-movie-technical-specs movie)
	(show-movie-screenshots movie)
	[:div.download_link_container [:a.download_btn {:href (:TorrentUrl movie)} "DOWNLOAD"]]])))

(defn do-delete-movie 
  "Delete user favorite movie."
  [id]
    (delete-user-movie id))
	
(defn do-insert-movie 
  "Insert user favorite movie."
  [movie-id title cover-url genre rating torent-url quality size]
    (insert-user-movie movie-id title cover-url genre rating torent-url quality size (:_id (session/get :user))))