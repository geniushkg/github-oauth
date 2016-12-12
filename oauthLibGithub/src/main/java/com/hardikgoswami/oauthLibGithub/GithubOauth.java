package com.hardikgoswami.oauthLibGithub;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GithubOauth {
    private String client_id;
    private String client_secret;
    private String nextActivity;
    private Context appContext;
    private boolean debug;
    private String packageName;
    private ArrayList<String> scopeList;

    public boolean isDebug() {
        return debug;
    }

    public ArrayList<String> getScopeList() {
        return scopeList;
    }

    public void setScopeList(ArrayList<String> scopeList) {
        this.scopeList = new ArrayList<String>();
        this.scopeList = scopeList;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static GithubOauth Builder() {
        return new GithubOauth();
    }

    public GithubOauth withContext(Context context) {
        setAppContext(context);
        return this;
    }

    public GithubOauth withClientId(String client_id) {
        setClient_id(client_id);
        return this;
    }

    public GithubOauth withClientSecret(String client_secret) {
        setClient_secret(client_secret);
        return this;
    }

    public GithubOauth nextActivity(String activity) {
        setNextActivity(activity);
        return this;
    }

    public GithubOauth debug(boolean active) {
        setDebug(active);
        return this;
    }

    public GithubOauth packageName(String packageName) {
        setPackageName(packageName);
        return this;
    }

    public GithubOauth withScopeList(ArrayList<String> scopeList){
        setScopeList(scopeList);
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

    /**
     * This method will execute the instance created , client_id ,
     * client_secret , packagename and activity fully qualified is must
     */
    public void execute() {
        ArrayList<String> scopeList = getScopeList();
        String github_id = getClient_id();
        String github_secret = getClient_secret();
        String activityName = getNextActivity();
        Intent intent = new Intent(appContext, OauthActivity.class);
        intent.putExtra("id", github_id);
        intent.putExtra("secret", github_secret);
        intent.putExtra("debug", debug);
        intent.putExtra("package", packageName);
        intent.putExtra("activity", nextActivity);
        if (scopeList.size()>0){
        intent.putStringArrayListExtra("scope_list",scopeList);
        intent.putExtra("isScopeDefined",true);
        }else {
            intent.putExtra("isScopeDefined",false);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
