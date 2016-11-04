# github-oauth
Simple library to integrate github authentication to android app using Oauth.

## Installation

    compile 'com.github.geniushkg:github_oauth:0.2.2'

##Github Auth Flow
Initialise new Auth instance with credentials</br>
1. Client id : you get it from your github profile by creating new app.</br>
2. Client Secret : same as above.</br>
3. NextActivity : Thats your activity you want launch after user gets authenticated.</br>
4. Context : you  can use context variable from place where initiate the process that is getActivity() from fragment or getapplicationcontext() from activity.

**Sample initialization :**


        // Github ID and secret are generated in github.com profile 
		loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GithubOauth
                        .Builder()
                        .withClientId(GITHUB_ID)
                        .withClientSecret(GITHUB_SECRET)
                        .withContext(context)
                        .nextActivity(UserActivity.class)
                         .execute();
            }
        });

**Note : ** Callback url can as per your requirement or make it http://localhost will registering new Oauth application.


Execute will launch a new activity with webview and user token will be stored in shared prerence


shared preference name : github_prefs

String in preference : oauth_token


if you found any bug you can create issue or want to contribute feel free to PR.

For new programmer's -> do not hesitate , please send PR we both will learn something new. :)

[https://twitter.com/geniushkg](https://twitter.com/geniushkg "Goswami Hardik")
