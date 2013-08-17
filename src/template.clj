(ns template
  (:require [noir.session :as session])
  (:use [hiccup.core :only [html]]
           [hiccup.page :only [include-css]]))

 (defn- no-selected
  "Show left menu with no page selected." 
  []
  [:div.left_menu
   [:a.menu_item {:href "/"} "Home"]
   [:a.menu_item {:href "/favorites"} "Favorites"]])
   
  (defn- home-selected
  "Show left menu with home page selected." 
  []
  [:div.left_menu
   [:a.menu_item_selected {:href "/"} "Home"]
   [:a.menu_item {:href "/favorites"} "Favorites"]])

 (defn- favorites-selected
  "Show left menu with favorites page selected." 
  []
  [:div.left_menu
   [:a.menu_item {:href "/"} "Home"]
   [:a.menu_item_selected {:href "/favorites"} "Favorites"]])

(defn- user-logged
  "Show menu for logged user."
  [key-page]
  [:div.menu
  (cond
	(or (= key-page "search-page") (= key-page "movie-page")) (no-selected)
	(= key-page "home-page") (home-selected)
    (= key-page "favorites-page") (favorites-selected))
   [:div.right_menu [:a.menu_item {:href "/logout"} "Logout"]]])
   
 (defn- register-selected
  "Shows menu with register page selected."
  []
  [:div.menu
    [:a.menu_item {:href "/"} "Login"]
	[:a.menu_item_selected {:href "/register"} "Register"]])

(defn- login-selected
  "Shows menu with login page selected." 
  []
  [:div.menu
	 [:a.menu_item_selected {:href "/"} "Login"]
    [:a.menu_item {:href "/register"} "Register"]])

 (defn- user-logged-out
 "Show links for logged out user."
 [key-page]
 (cond
	(= key-page "register-page") (register-selected)
    (= key-page "login-page") (login-selected)))

 (defn- show-menu 
  "Shows appropriate links in menu."
  [key-page user]
  (if user (user-logged key-page) (user-logged-out key-page)))

 (defn template-page 
  "Template page for application."
  [title key-page content]
  (html 
    [:head
     [:meta {:charset "UTF-8"}] 
     [:title title]
     (include-css "/css/style.css")]
     (let [user (session/get :user)]
      [:body
       [:div.body
	    [:div.container
		   [:div.header
			[:div.header_logo_container
			 [:img.header_logo {:src "/images/header-logo.jpg" :alt "Movie Maniac"}]]
			(show-menu key-page user)]
		   
		   [:div.body
			content]
		   
		   [:div.footer
			[:div.footer_lbl_container
			 [:p.footer_lbl "Copyright &copy; 2013. Movie Maniac"]]
			[:div.footer_logo_container
			  [:img.footer_logo {:src "/images/footer-logo.jpg"}]]]]]])))