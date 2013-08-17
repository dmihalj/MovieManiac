(ns register
  (:require [noir.session :as session]
               [ring.util.response :as response])
  
  (:use [hiccup.form :only [form-to label text-field email-field password-field drop-down submit-button]]
        [template :only [template-page]]
        [mongodb :only [insert-user get-user-by-email get-user-by-username]]))

(defn- get-register-form 
  "Show register form."
  []
   [:div.content
    [:p.content_title "Please enter data to register:"]
    [:p.error (session/flash-get :error)]
    (form-to [:post "/register"]
             [:table
              [:tr
               [:td (label :name "Name:")]
               [:td (text-field :name (session/flash-get :name))]]
              [:tr
               [:td (label :email "Email:")]
               [:td (email-field :email (session/flash-get :email))]]
              [:tr
               [:td (label :username "Username:")]
               [:td (text-field :username (session/flash-get :username))]]
              [:tr
               [:td (label :password "Password:")]
               [:td (password-field :password)]]
              [:tr
               [:td (label :confirm-password "Confirm password:")]
               [:td (password-field :confirm-password)]]
			  [:tr
               [:td (label :movie-genre "Choose your favorite genre:")]
               [:td (drop-down :movie-genre ["Action" "Animation" "Comedy" "Drama" "Documentary" "Family" "Horror" "Romance" "Thriller"] 1)]]
			  [:tr
               [:td (label :min-rating "Choose movie minimum rating:")]
               [:td (drop-down :min-rating [0 1 2 3 4 5 6 7 8 9 10] 1)]]
              [:tr
               [:td]
               [:td (submit-button {:class "register_btn"} "Register")]]])])

 (defn- show-error-page
  "Show error page"
  []
  [:div.content
   [:p.error "Please logout before create a new account."]])
 
(defn show-register-page 
  "Show register page."
  []
  (template-page
    "Register page"
    "register-page"
	 (let [user (session/get :user)]
		(if user (show-error-page) (get-register-form)))))

(defn- check-user-data
  "Check enetered user data." 
  [name email username password confirm-password movie-genre min-rating]
  (cond
    (> 5 (.length name)) "Name must be at least 5 characters long."
    (< 20 (.length name)) "Name should not be longer than 20 characters."
    (not= name (first (re-seq #"[A-Za-z0-9_]+" name))) "No special characters are allowed for the name."
    (not (nil? (get-user-by-email email))) "Email address exists. Please choose another one."
    (not (nil? (get-user-by-username username))) "Username exists. Please choose another one."
    (> 5 (.length username)) "Username must be at least 5 characters long."
    (< 15 (.length username)) "Username should not be longer than 15 characters."
    (not= username (first (re-seq #"[A-Za-z0-9_]+" username))) "No special characters are allowed for the username."
    (> 4 (.length password)) "Password must be at least 4 characters long."
    (not= password confirm-password) "Password and confirmed password are not the same."
    :else true))

(defn do-register 
  "If user data is entered properly, add user to database."
  [name email username password confirm-password movie-genre min-rating]
  (let [lusername (clojure.string/lower-case username)
        error-msg (check-user-data name email lusername password confirm-password movie-genre min-rating)]
    (if-not (string? error-msg)
      (do
        (insert-user name email lusername password movie-genre min-rating)
        (response/redirect "/"))
      (do
        (session/flash-put! :error error-msg)
        (session/flash-put! :name name)
        (session/flash-put! :email email)
        (session/flash-put! :username lusername)
        (response/redirect "/register")))))