# github-oauth
Simple library to integrate github authentication to android app using Oauth.

## Installation

    compile 'com.github.geniushkg:oauthLibGithub:1.0.1'

Add to manifest 

    <uses-permission android:name="android.permission.INTERNET"/>

and activity declaration:

    <activity android:name="com.hardikgoswami.oauthLibGithub.OauthActivity"/>
##Github Auth Flow
Initialise new Auth instance with credentials</br>
1. Client id : you get it from your github profile by creating new app.</br>
2. Client Secret : same as above.</br>
3. NextActivity : Thats your activity you want launch after user gets authenticated.</br>
4. Context : you  can use context variable from place where initiate the process that is getActivity() from fragment or getapplicationcontext() from activity.

**Sample initialization :**


        // Github ID and secret are generated in github.com profile
		// package name is your packagename
		// next activity is your activity with full name including package 
		// you can use debug(true) for logcat , use TAG = "github-oauth"

		loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 GithubOauth
                        .Builder()
                        .withClientId(GITHUB_ID)
                        .withClientSecret(GITHUB_SECRET)
                        .withContext(context)
                        .packageName("com.hardikgoswami.github_oauth_lib")
                        .nextActivity("com.hardikgoswami.github_oauth_lib.UserActivity")
                        .debug(true)
                         .execute();
            }
        });

**Note :** Callback url can be as per your requirement or make it http://localhost while registering new Oauth application.


Execute will launch a new activity with webview and user token will be stored in shared preference


shared preference name : github_prefs

String in preference : oauth_token

		// Sample to read logged in user oauth token
        public static final String PREFERENCE = "github_prefs";
		sharedPreferences = getSharedPreferences(PREFERENCE, 0);
        String oauthToken = sharedPreferences.getString("oauth_token", null);
        Log.d(TAG, "oauth token for github loged in user is :" + oauthToken);

if you found any bug you can create issue or want to contribute feel free to PR.

For new programmer's -> do not hesitate , please send PR we both will learn something new. :)

[twitter](https://twitter.com/geniushkg "Goswami Hardik") - [linkedIn](https://www.linkedin.com/in/geniushkg "Goswami Hardik") - [blog](http://hardikgoswami.com "Goswami Hardik") 
