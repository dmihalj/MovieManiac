(ns mongodb
  (:use somnium.congomongo))

(def conn 
  (make-connection "movie_maniac_db"))

(set-connection! conn)

(defn- generate-id [coll]
  "Generate entity identifier." 
  (:seq (fetch-and-modify :sequences {:_id coll} {:$inc {:seq 1}}
                          :return-new? true :upsert? true)))

(defn- insert-entity [coll values]
   "Insert an entity into database."
  (insert! coll (assoc values :_id (generate-id coll))))

(defn insert-user
  [name email username password movie-genre min-rating]
  "Insert user into database." 
  (insert-entity :users 
                  {:name name
                   :email email
                   :username username
                   :password password
				   :movie_genre movie-genre
                   :min_rating min-rating}))

(defn get-user-by-email [email]
  "Find user by email."  
  (fetch-one :users :where {:email email}))
 
(defn get-user-by-username [username]
  "Find user by username."  
  (fetch-one :users :where {:username username}))
 
(defn insert-user-movie
  [movie-id title cover-img genre rating torent-url quality size user-id]
  "Insert user favorite movie into database." 
  (insert-entity :user-movies 
                  {:movie-id movie-id
                   :title title
                   :cover-img cover-img
                   :genre genre
				   :rating rating
				   :torent-url torent-url
				   :quality quality
                   :size size
				   :user-id user-id}))

(defn get-movies-for-user [user-id]
  "Find user favorite movies." 
  (fetch :user-movies :where {:user-id user-id}))
  
(defn get-user-movie [user-id movie-id]
  "Find user favorite movie."  
  (fetch-one :user-movies :where {:user-id user-id :movie-id movie-id}))
  
 (defn delete-user-movie [id]
  "Delete user favorite movie." 
  (destroy! :user-movies {:_id id}))
  
(defn delete-user-by-username [username]
  "Delete user with given username." 
  (destroy! :users {:username username}))