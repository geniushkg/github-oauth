package com.hardikgoswami.oauthLibGithub;

import android.content.Context;
import android.content.Intent;

/**
 * Created by geniushkg on 11/2/2016.
 */

public class GithubOauth {
    private String client_id;
    private String client_secret;
    private String nextActivity;
    private Context appContext;
    private boolean debug;
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

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

    public GithubOauth nextActivity(String activity){
        setNextActivity(activity);
        return this;
    }

    public GithubOauth debug(boolean active){
        setDebug(active);
        return this;
    }
    public GithubOauth packageName(String packageName){
        setPackageName(packageName);
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

    public String getNextActivity() {
        return nextActivity;
    }

    public void setNextActivity(String nextActivity) {
        this.nextActivity = nextActivity;
    }

    public  void execute(){
        String github_id = getClient_id();
        String github_secret = getClient_secret();
        String activityName = getNextActivity();
        Intent intent = new Intent(appContext,OauthActivity.class);
        intent.putExtra("id",github_id);
        intent.putExtra("secret",github_secret);
        intent.putExtra("debug",debug);
        intent.putExtra("package",packageName);
        intent.putExtra("activity",nextActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
