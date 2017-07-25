package com.hardikgoswami.oauthLibGithub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;


public class GithubOauth {
    public static final int REQUEST_CODE = 1000;
    private String client_id;
    private String client_secret;
    private String nextActivity;
    private Activity appContext;
    private boolean debug;
    private String packageName;
    private ArrayList<String> scopeList;
    private boolean clearBeforeLaunch;

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


    public GithubOauth withContext(Activity activity) {
        setAppContext(activity);
        return this;
    }

    /**
     * @deprecated Use withContext(Activity) instead
     */
    @Deprecated
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
     * @param clearBeforeLaunch true to clear data
     * @return An instance of this class
     */
    public GithubOauth clearBeforeLaunch(boolean clearBeforeLaunch) {
        this.clearBeforeLaunch = clearBeforeLaunch;
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

    public void setAppContext(Activity appContext) {
        this.appContext = appContext;
    }

    /**
     * @deprecated Use setAppContext(Activity) instead
     */
    @Deprecated
    public void setAppContext(Context appContext) {
        this.appContext = (Activity) appContext;
    }

    public String getNextActivity() {
        return nextActivity;
    }

    public void setNextActivity(String nextActivity) {
        this.nextActivity = nextActivity;
    }

    /**
     * This method will execute the instance created. The activity of login will be launched and
     * it will return a result after finishing its execution. The result will be one of the constants
     * hold in the class {@link ResultCode}
     * client_id, client_secret, package name and activity fully qualified are required
     */
    public void execute() {
        ArrayList<String> scopeList = getScopeList();
        String github_id = getClient_id();
        String github_secret = getClient_secret();
        boolean hasScope = scopeList != null && scopeList.size() > 0;

        Intent intent = new Intent(getAppContext(), OauthActivity.class);
        intent.putExtra("id", github_id);
        intent.putExtra("debug", isDebug());
        intent.putExtra("secret", github_secret);
        intent.putExtra("package", getPackageName());
        intent.putExtra("activity", getNextActivity());
        intent.putExtra("clearData", clearBeforeLaunch);
        intent.putExtra("isScopeDefined", hasScope);

        if (hasScope) {
            intent.putStringArrayListExtra("scope_list", scopeList);
        }

        appContext.startActivityForResult(intent, REQUEST_CODE);
    }
}
