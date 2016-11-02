package com.hardikgoswami.oauth_lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by geniushkg on 11/2/2016.
 */

public class GithubOauth {
    private String client_id;
    private String client_secret;
    private Activity nextActivity;
    private Context appContext;

    public static GithubOauth Builder() {
        return new GithubOauth();
    }

    public GithubOauth withContext(Context context){
        setAppContext(context);
        return this;
    }

    public GithubOauth withClientId(String client_id){
        setClient_id(client_id);
        return this;
    }

    public GithubOauth withClientSecret(String client_secret){
        setClient_secret(client_secret);
        return this;
    }

    public GithubOauth nextActivity(Activity activity){
        setNextActivity(activity);
        return this;
    }


    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public void setNextActivity(Activity nextActivity) {
        this.nextActivity = nextActivity;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public Activity getNextActivity() {
        return nextActivity;
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public  void execute(){
        String github_id = getClient_id();
        String github_secret = getClient_secret();
        Intent intent = new Intent(appContext,OauthActivity.class);
        intent.putExtra("id",github_id);
        intent.putExtra("secret",github_secret);
        appContext.startActivity(intent);
    }
}
