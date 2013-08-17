# Movie Maniac

This is a web application written in Clojure with support of the following libraries: [CongoMongo](https://github.com/aboekhoff/congomongo),  [data.json](https://github.com/clojure/data.json), [Ring](https://github.com/ring-clojure/ring), [Mongo-session](https://github.com/amalloy/mongo-session), [Compojure](https://github.com/weavejester/compojure), [lib-noir](https://github.com/noir-clojure/lib-noir), [Hiccup](https://github.com/weavejester/hiccup)  and [clj-time](https://github.com/clj-time/clj-time).

Application is used for searching, reviewing and storing your favorite movies, but with real torrent links for the movie you like. Also the application, based on your preferences, recomends the movies that you may like.

First time when the application is started, test user with his test data is imported. But we are encouraging you to create your own account and feel the application. Application uses REST API from http://yify-torrents.com for all movies data and MongoDB for user personal data.

To use the application you have to register, where you can put the data regarding the movies you prefere.

After registering, you need to login. And you will be automatically redirected to the Home page. There you can find our recomendations, or you can search at your own. On this page you can find some basic movie data, but if you click on movie link (cover image or title), you will be taken to the movie details page.

On movie details page you can find more data for desired movie. Short description of the movie, raitings, screenshots, download link, size etc., so if you are interested you can download movie immediatelly or save it as favorite and download it later, or if it is allready favorite and you don't like it any more you can remove it from your favorite list.

On favorite page you can find your favorite movies with some basic movie data, administrate the list, or go to the movie details page also.
	
## Setup instructions for running locally

* Download and install [Leiningen](https://github.com/technomancy/leiningen).

* Download and install [MongoDB](http://www.mongodb.org/). 

* Start MongoDB.

* To start the application (use comand line) go to the project directory and run `lein run`.

## Note: 
- Because the application is based on REST API resources, sometimes it can be slow, primarly because of the supplier load, so please be patient.
- Be aware, this is only student project for educational purpose.

## References

* [Practical Clojure](http://www.amazon.com/Practical-Clojure-Experts-Voice-Source/dp/1430272317), Luke VanderHart and Stuart Sierra
* [Clojure Programming](http://www.amazon.com/Clojure-Programming-Chas-Emerick/dp/1449394701), Chas Emerick, Brian Carper and Chrisophe Grand
* [Developing and Deploying a Simple Clojure Web Application](http://mmcgrana.github.io/2010/07/develop-deploy-clojure-web-applications.html),
* [A brief overview of the Clojure web stack](http://brehaut.net/blog/2011/ring_introduction) 
* Documentation from used libraries, which can be found on their github links (see above library links).

## License

Distributed under the Eclipse Public License, the same as Clojure.
