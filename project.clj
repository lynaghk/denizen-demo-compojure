(defproject com.getdenizen/demo "0.1.0-SNAPSHOT"
  :description "Simple Compojure application using Denizen to handle user login"
  :url "https://getdenizen.com"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.7"
                  :exclusions [ring/ring-core]]
                 [com.cemerick/friend "0.2.1"
                  :exclusions [commons-codec org.apache.httpcomponents/httpclient]]
                 [ring/ring-jetty-adapter "1.3.0"]
                 [hiccup "1.0.5"]
                 [clj-http "0.9.2"]]

  :min-lein-version "2.0.0"
  :main com.getdenizen.demo.web
  :uberjar-name "denizen-demo-standalone.jar"
  :profiles {:uberjar {:aot :all}})
