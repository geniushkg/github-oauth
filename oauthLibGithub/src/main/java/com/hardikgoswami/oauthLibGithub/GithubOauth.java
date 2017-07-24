package com.hardikgoswami.oauthLibGithub;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;


public class GithubOauth {
    private String client_id;
    private String client_secret;
    private String nextActivity;
    private Context appContext;
    private boolean debug;
    private String packageName;
    private ArrayList<String> scopeList;
    private boolean clearBeforeLauch;
    private boolean openNextActivity;

    public boolean isDebug() {
        return debug;
    }

    public ArrayList<String> getScopeList() {
        return scopeList;
    }

    public void setScopeList(ArrayList<String> scopeList) {
        this.scopeList = new ArrayList<>();
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

    public GithubOauth withScopeList(ArrayList<String> scopeList) {
        setScopeList(scopeList);
        return this;
    }

    /**
     * Whether the app should clear all data (cookies and cache) before launching a new instance of
     * the webView
     *
     * @param clearBeforeLauch true to clear data
     * @return An instance of this class
     */
    public GithubOauth clearDataBeforeLauch(boolean clearBeforeLauch) {
        this.clearBeforeLauch = clearBeforeLauch;
        return this;
    }

    /**
     * Whether to open or not the activity specified as next
     *
     * @param openNextActivity true to open the next activity
     * @return An instance of this class
     */
    public GithubOauth openNextActivity(boolean openNextActivity) {
        this.openNextActivity = openNextActivity;
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
        boolean hasScope = scopeList != null && scopeList.size() > 0;

        Intent intent = new Intent(getAppContext(), OauthActivity.class);
        intent.putExtra("id", github_id);
        intent.putExtra("secret", github_secret);
        intent.putExtra("debug", isDebug());
        intent.putExtra("package", getPackageName());
        intent.putExtra("activity", getNextActivity());
        intent.putExtra("clearData", clearBeforeLauch);
        intent.putExtra("openNextActivity", openNextActivity);
        intent.putExtra("isScopeDefined", hasScope);

        if (hasScope) {
            intent.putStringArrayListExtra("scope_list", scopeList);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }
}
