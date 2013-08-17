(ns server
  (:require [compojure.route :as route]
				[noir.session :as session]
				[ring.util.response :as response])
  
  (:use [compojure.core :only [defroutes GET POST DELETE PUT]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [ring.middleware.params :only [wrap-params]]
        [mongo-session.core :only [mongo-session]]
		[login :only [show-login-page do-login do-logout]]
		[register :only [show-register-page do-register]]
		[main :only [show-home-page show-search-page]]
		[favorites :only [show-favorites-page do-delete-favorite]]
		[movie :only [show-movie-page do-delete-movie do-insert-movie]]))

(defroutes handler
  (GET "/" [] (let [user (session/get :user)] (if user (show-home-page) (show-login-page))))
  (POST "/login" [username password] (do-login username password))
  (GET "/logout" [] (do (do-logout) (response/redirect "/")))
  (GET "/register" [] (show-register-page))
  (POST "/register" [name email username password confirm-password movie-genre min-rating] (do-register name email username password confirm-password movie-genre min-rating))
  (POST "/search" [search-criteria] (response/redirect (str "/search=" search-criteria)))
  (GET "/search=:search-criteria" [search-criteria] (let [user (session/get :user)] (if user (show-search-page search-criteria) (do (session/flash-put! :error "Please first log in.") (response/redirect "/")))))
  (GET "/favorites" [] (let [user (session/get :user)] (if user (show-favorites-page) (do (session/flash-put! :error "Please first log in.") (response/redirect "/")))))
  (DELETE "/favorite" [id] (do (do-delete-favorite (Integer/valueOf id)) (response/redirect "/favorites")))
  (GET "/movie/:id" [id] (let [user (session/get :user)] (if user (show-movie-page id) (do (session/flash-put! :error "Please first log in.") (response/redirect "/")))))
  (DELETE "/movie" [id movie-id] (do (do-delete-movie (Integer/valueOf id)) (response/redirect (str "/movie/" movie-id))))
  (PUT "/movie" [movie-id title cover-url genre rating torent-url quality size] (do (do-insert-movie movie-id title cover-url genre rating torent-url quality size) (response/redirect (str "/movie/" movie-id))))
  (route/resources "/")
  (route/not-found "Page not found."))

(def app
  (-> #'handler
    (wrap-reload)
    (wrap-params)
    (session/wrap-noir-flash)
    (session/wrap-noir-session {:store (mongo-session :sessions)})
    (wrap-stacktrace)))

(defn start-server [] 
  (run-jetty #'app {:port 9999 :join? false})
  (println "\nStart your action on http://localhost:9999!"))

(defn insert-user-data [] 
  (mongodb/insert-user "Test User" "test@test.com" "test" "test" "Action" "3"))
  
(defn insert-movies-data [] 
   (let [user (mongodb/get-user-by-username "test")](do
   (mongodb/insert-user-movie "3843" "Fighting (2009) 1080p UNRATED" "http:\\\\static.yify-torrents.com\\attachments\\Fighting_2009_1080p_UNRATED\\poster_med.jpg" "Action" "5.4" "http:\\\\yify-torrents.com\\download\\start\\Fighting_2009_1080p_BluRay_x264_YIFY_mp4.torrent" "1080p" "1.64 GB" (:_id user))
   (mongodb/insert-user-movie "3841" "The Last Boy Scout (1991) 1080p" "http:\\\\static.yify-torrents.com\\attachments\\The_Last_Boy_Scout_1991_1080p\\poster_med.jpg" "Action" "6.8" "http:\\\\yify-torrents.com\\download\\start\\The_Last_Boy_Scout_1991_1080p_BluRay_x264_YIFY_mp4.torrent" "1080p" "1.64 GB" (:_id user))
   (mongodb/insert-user-movie "3834" "Pawn Shop Chronicles (2013) 1080p" "http:\\\\static.yify-torrents.com\\attachments\\Pawn_Shop_Chronicles_2013_1080p\\poster_med.jpg" "Action" "5.5" "http:\\\\yify-torrents.com\\download\\start\\Pawn_Shop_Chronicles_2013_1080p_BluRay_x264_YIFY_mp4.torrent" "1080p" "1.65 GB" (:_id user))
   (mongodb/insert-user-movie "3832" "Silver Streak (1976) 1080p" "http:\\\\static.yify-torrents.com\\attachments\\Silver_Streak_1976_1080p\\poster_med.jpg" "Action" "6.8" "http:\\\\yify-torrents.com\\download\\start\\Silver_Streak_1976_1080p_BluRay_x264_YIFY_mp4.torrent" "1080p" "1.65 GB" (:_id user)))))

(defn insert-test-data [] 
  (let [user (mongodb/get-user-by-username "test")]
  (if-not user (do (insert-user-data) (insert-movies-data)))))
 
(defn -main [& args]
  (do
   (start-server)
   (insert-test-data)))