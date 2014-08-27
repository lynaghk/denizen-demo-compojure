(ns com.getdenizen.demo.web
  (:require [compojure.core :refer [routes context GET ANY]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :refer [redirect]]
            [cemerick.friend :as friend]
            [hiccup.core :refer [html]]
            [clojure.pprint :refer [pprint]]
            [com.getdenizen.demo.auth :as auth])
  (:gen-class))


(defn customer-routes
  [config]
  (routes
   (GET "/" req
     (html
      [:h1 "Private stuff for our fine users!"]

      [:h2 "Some information about yourself:"
       [:pre (with-out-str
               (pprint (friend/identity req)))]]

      [:a {:href "/logout"} "Logout"]))

   (route/not-found "Not Found")))


(defn public-routes
  [config]
  (routes

   (GET "/" req
     (html
      [:h1 "Public route available to everyone."]
      [:span "See "
       [:a {:href "/private/"} "/private/"]
       " for the secret stuff."]))

   (GET "/login/" req
     (html
      [:form {:method "POST" :action "/login"}
       [:script {:src "https://ui.getdenizen.com/onboard.js"
                 :data-name "My rad app"
                 :data-flow "register"
                 :data-denizen-key (:denizen-publishable-key config)}]]))


   (friend/logout
    (ANY "/logout" req (redirect "/")))

   (route/not-found "Not Found")))


(defn app
  [config]

  (-> (routes

       (context "/private" []
         (friend/wrap-authorize (customer-routes config)
                                #{:fine-user}))

       (public-routes config))

      (friend/authenticate {:allow-anon? true
                            :default-landing-uri "/"
                            :login-uri "/login/"
                            :workflows [(auth/denizen-friend-workflow {:token-endpoint "/login"
                                                                       :creds {:secret-key (:denizen-secret-key config)}})]})
      handler/site))


(defn -main [& args]
  (let [config {:port                    (Integer. (or (System/getenv "PORT") "8080"))
                :denizen-publishable-key (System/getenv "DENIZEN_PUBLISHABLE_KEY")
                :denizen-secret-key      (System/getenv "DENIZEN_SECRET_KEY")}
        handler (app config)]

    (when-not (:denizen-secret-key config)
      (println "Required DENIZEN_SECRET_KEY environment variable is not set, exiting.")
      (System/exit 1))

    (when-not (:denizen-publishable-key config)
      (println "Required DENIZEN_PUBLISHABLE_KEY environment variable is not set, exiting.")
      (System/exit 1))

    (run-jetty handler {:port (:port config)
                        :join? false})))
