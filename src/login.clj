(ns login
  (:require [noir.session :as session]
                [ring.util.response :as response])
  
  (:use [hiccup.form :only [form-to label text-field password-field submit-button]]
          [template :only [template-page]]
          [mongodb :only [get-user-by-username]]))
		
(defn- get-login-form 
  "Show login form."
  [] 
   [:div.content
    [:p.content_title "Please enter your username and password:"]
    [:p.error (session/flash-get :error)]
    (form-to [:post "/login"]
			[:div.table_container
             [:table
              [:tr
               [:td (label :username "Username:")]
               [:td (text-field :username (session/flash-get :username))]]
              [:tr
               [:td (label :password "Password:")]
               [:td (password-field :password)]]
              [:tr
               [:td {:colspan 2 :style "text-align:center"} (submit-button {:class "login_btn"} "Log In")]]]])
    [:p.create_account_lbl "If you dont have an account, you can create one " [:a.create_account_link {:href "/register"} "here" ] "."]])

(defn show-login-page 
  "Show login page."
  [] 
  (template-page
    "Login page"
    "login-page"
    (get-login-form)))

(defn- check-user
  "Check user data."
  [user password]
   (cond
    (nil? user) "User with given username does not exist."
    (not= password (:password user)) "Password is not correct."
    :else true))

(defn do-login 
  "Log the user, if username and password are correct."
  [username password]
  (let [lusername (clojure.string/lower-case username)
		 user (get-user-by-username lusername)
         error-msg (check-user user password)]
			(if-not (string? error-msg)
			  (do 
				(session/put! :user user)
				(response/redirect "/"))
			  (do
				(session/flash-put! :error error-msg)
				(session/flash-put! :username lusername)
				(response/redirect "/")))))
		
(defn do-logout []
  (session/remove! :user))