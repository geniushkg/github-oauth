package com.hardikgoswami.oauthLibGithub;

import android.content.Context;
import android.content.Intent;

/**
 * Created by geniushkg on 11/2/2016.
 */

public class GithubOauth {
    private String client_id;
    private String client_secret;
    private Class nextActivity;
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

    public GithubOauth nextActivity(Class activity){
        setNextActivity(activity);
        return this;
    }


    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }


    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public Class getNextActivity() {
        return nextActivity;
    }

    public void setNextActivity(Class nextActivity) {
        this.nextActivity = nextActivity;
    }

    public  void execute(){
        String github_id = getClient_id();
        String github_secret = getClient_secret();
        String activityName = getNextActivity().getClass().getSimpleName();

        Intent intent = new Intent(appContext,OauthActivity.class);
        intent.putExtra("id",github_id);
        intent.putExtra("secret",github_secret);
        intent.putExtra("activity",activityName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
