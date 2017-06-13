(ns guestbook.routes.home
  (:require [guestbook.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [guestbook.db.core :as db]))

(defn home-page []
  (layout/render
    "home.html"
    {:messages (db/get-messages)}))

(defn about-page []
  (layout/render "about.html"))

(defn save-message! [{:keys [params]}]
  (db/save-message!
    (assoc params :timestamp (java.util.Date.)))
  (response/found "/"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/message" request (save-message! request))
  (GET "/about" [] (about-page)))
