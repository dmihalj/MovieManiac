(defproject movie-maniac "1.0"
  :description "Movie maniac web application"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [congomongo "0.4.1"]
                 [org.clojure/data.json "0.2.2"]
				 [ring "1.2.0"]
				 [amalloy/mongo-session "0.0.2"]
				 [compojure "1.1.5"]
				 [lib-noir "0.6.6"]
				 [hiccup "1.0.4"]
				 [clj-time "0.5.1"]]
  :main server)