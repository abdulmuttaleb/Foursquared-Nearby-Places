# Foursqaured API fetching Android Application

- This application fetches venues from foursquared API, fetches these places pictures using their photo api since they are two separate things. 
- This App is lifecycle aware. 
- It fetches cached places if there is a problem either by turning off location or having an internet problem

## Libraries used to make this application
- Retrofit for API Calls
- Gson for serializing and deserializing data
- OkHttp as a connection client
- RxJava for reactive execution of background operations
- Google's Lifecycle components for MVVM architecture and lifecycle aware components
- Room for SQLite database caching

### gradle.properties Structure

> **Note**
Please, provide your client_id and client_secret, you get from Foursquared when you register an app, in **gradle.properties** file to be able to build this application

```Groovy
org.gradle.jvmargs=-Xmx1536m
android.useAndroidX=true
android.enableJetifier=true
kotlin.code.style=official
foursquared_client_id = YOUR_CLIENT_ID
foursquared_client_secret = YOUR_CLIENT_SECRET
```
