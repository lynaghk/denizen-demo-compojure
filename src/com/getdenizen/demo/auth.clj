(ns com.getdenizen.demo.auth
  (:require [clj-http.client :as http]
            [ring.util.request :as req]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :refer [make-auth]]))


(defn fetch-user-info
  "Fetch user info from Denizen from `token`."
  [creds token]
  (let [res (http/get (str (:endpoint creds "https://api.getdenizen.com") "/tokens/" token)
                      {:throw-exceptions false
                       :basic-auth [(:secret-key creds)]
                       :as :json})]

    (when (= 200 (:status res))
      (:body res))))


(defn denizen-friend-workflow
  "Friend workflow that retrieves user info when a token is posted to `token-endpoint`."
  [opts]
  (let [{:keys [token-endpoint creds redirect-on-auth?]}
        (merge {:token-endpoint "/login"
                :redirect-on-auth? true}
               opts)]

    (fn [req]
      (when (and (= :post (:request-method req))
                 (= token-endpoint (req/path-info req)))
        (when-let [token (-> req :params :denizen-token)]
          (when-let [user (fetch-user-info creds token)]
            (make-auth (merge user {:identity (:email user)
                                    :roles #{:fine-user}})
                       {::friend/workflow :denizen
                        ::friend/redirect-on-auth? redirect-on-auth?})))))))
