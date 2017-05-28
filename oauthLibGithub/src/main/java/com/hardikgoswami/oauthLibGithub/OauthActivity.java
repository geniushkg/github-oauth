package com.hardikgoswami.oauthLibGithub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OauthActivity extends AppCompatActivity {

    public static final String GITHUB_URL = "https://github.com/login/oauth/authorize";
    public static final String GITHUB_OAUTH = "https://github.com/login/oauth/access_token";
    private static final String TAG = "github-oauth";
    public static String CODE = "";
    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";
    public static String ACTIVITY_NAME = "";
    public static String PACKAGE = "";
    private ProgressBar spinner;
    private WebView webview;
    private Class c;
    private boolean isScopeDefined;
    private boolean debug;
    public List<String> scopeList;
    public String scopeAppendToUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        scopeList = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            CLIENT_ID = getIntent().getStringExtra("id");
            CLIENT_SECRET = getIntent().getStringExtra("secret");
            ACTIVITY_NAME = getIntent().getStringExtra("activity");
            debug = getIntent().getBooleanExtra("debug", false);
            PACKAGE = getIntent().getStringExtra("package");
            isScopeDefined = getIntent().getBooleanExtra("isScopeDefined", false);
            if (isScopeDefined) {
                scopeList = getIntent().getStringArrayListExtra("scope_list");
            }
        } else {
            Log.d(TAG, "intent extras null");
        }
        if (debug)
            Log.d(TAG, "intent recieved is -client id :" + CLIENT_ID + "-secret:" + CLIENT_SECRET + "-activit : " + ACTIVITY_NAME + "-Package : " + PACKAGE);
//        try {
//            c = Class.forName(ACTIVITY_NAME);
//        } catch (ClassNotFoundException exp) {
//            if (debug)Log.d(TAG, "error :" + exp.getMessage());
//        }
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        if (isScopeDefined) {
            scopeAppendToUrl = getCsvFromList(scopeList);
        } else {
            scopeAppendToUrl = "";
        }
        Log.d(TAG, "onCreate: Scope request are : " + scopeAppendToUrl);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                // Try catch to allow in app browsing without crashing.
                try {
                    CODE = url.substring(url.lastIndexOf("?code=") + 1);
                    if (debug) Log.d(TAG, "code fetched is :" + CODE);
                    String[] token_code = CODE.split("=");
                    if (debug) Log.d(TAG, "code token :" + token_code[1]);
                    String tokenFetchedIs = token_code[1];
                    String[] cleanToken = tokenFetchedIs.split("&");
                    if (debug) Log.d(TAG, "token cleaned is :" + cleanToken[0]);
                    fetchOauthTokenWithCode(cleanToken[0]);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {}
                return false;
            }
        });
        String url_load = "";
        if (isScopeDefined) {
            url_load = GITHUB_URL + "?client_id=" + CLIENT_ID + "&scope=" + scopeAppendToUrl;
        } else {
            url_load = GITHUB_URL + "?client_id=" + CLIENT_ID;
        }
        webview.loadUrl(url_load);
    }

    private void fetchOauthTokenWithCode(String code) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder url = HttpUrl.parse(GITHUB_OAUTH).newBuilder();
        url.addQueryParameter("client_id", CLIENT_ID);
        url.addQueryParameter("client_secret", CLIENT_SECRET);
        url.addQueryParameter("code", code);
        String url_oauth = url.build().toString();
        final Request request = new Request.Builder()
                .header("Accept", "application/json")
                .url(url_oauth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (debug) Log.d(TAG, "IOException :" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String JsonData = response.body().string();
                    if (debug) Log.d(TAG, "response is:" + JsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(JsonData);
                        String auth_token = jsonObject.getString("access_token");
                        if (debug) Log.d(TAG, "token is :" + auth_token);
                        storeToSharedPreference(auth_token);
                    } catch (JSONException exp) {
                        if (debug) Log.d(TAG, "json exception :" + exp.getMessage());
                    }

                } else {
                    Log.d(TAG, "onResponse: not success : " + response.message());
                }

            }
        });
    }

    // Allow web view to go back a page.
    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void storeToSharedPreference(String auth_token) {
        SharedPreferences prefs = getSharedPreferences("github_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("oauth_token", auth_token);
        edit.commit();
        Intent intent = new Intent();
        intent.setClassName(PACKAGE, ACTIVITY_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public String getCsvFromList(List<String> scopeList) {
        String csvString = "";

        for (int i = 0; i < scopeList.size() - 1; i++) {
            csvString = csvString + scopeList.get(i) + ",";
        }
        csvString = csvString + scopeList.get(scopeList.size() - 1);

        return csvString;
    }
}
