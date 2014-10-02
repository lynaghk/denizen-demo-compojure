# Denizen-powered Compojure demo app

This demo Clojure web app uses Denizen for user registration and login.

You may want to [watch a short video overview](https://www.youtube.com/watch?v=y8gQRqQTHGI) of how this codebase fits into Denizen.

This web app has the following routes:

    /          (can be viewed by anyone)
    /private/  (can only be viewed by logged-in users)


## Prerequisites

Install Leiningen:

    https://github.com/technomancy/leiningen

Register with Denizen:

    https://getdenizen.com/

and get a Denizen keypair (both test and live keypairs will work fine, though you will need a live keypair if you want to register users with plans and charge real credit cards).


## Run (local)

You can run this demo locally:

```sh
    export DENIZEN_SECRET_KEY="...your denizen secret key..."
    export DENIZEN_PUBLISHABLE_KEY="...your denizen publishable key..."
    lein run
```

then go to `http://localhost:8080/`, where you will be able to register/login and then view `http://localhost:8080/private/`.
If you try this latter URL without being logged in, you will be redirected to `/`.


## Run (heroku)

If you'd like to run this out on Heroku, first install the Heroku toolbelt:

    https://toolbelt.heroku.com/

then create yourself a (free) Heroku app:

    heroku create

then set your Denizen keypair and deploy:

    heroku config:set DENIZEN_SECRET_KEY="...your denizen secret key..."
    heroku config:set DENIZEN_PUBLISHABLE_KEY="...your denizen publishable key..."
    git push heroku master

You can view your app in a browser by typing:

    heroku open
