# github-oauth
Android library to add oauth login flow for github , minimum api 14

##Github Auth Flow
1) Initialise oauth object with credentials
-> client_id thats provided by github
-> client_secret thats provided by github
-> next activity thats where you want to go after authentication

Execute will launch a new activity with webview and user token will be stored in shared prerence

shared preference name : github_prefs

String in preference : oauth_token


if you found any bug you can create issue or want to contribute feel free to PR.

For new programmer's -> do not hesitate , please send PR we both will learn something new. :)
