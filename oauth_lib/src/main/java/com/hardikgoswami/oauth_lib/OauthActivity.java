package com.hardikgoswami.oauth_lib;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OauthActivity extends AppCompatActivity {

    public static final String GITHUB_URL = "https://github.com/login/oauth/authorize";
    public static final String GITHUB_OAUTH = "https://github.com/login/oauth/access_token";
    private static final String TAG = OauthActivity.class.getSimpleName();
    public static String CODE = "";
    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";
    public static String ACTIVITY_NAME = "";
    private ProgressBar spinner;
    private WebView webview;
    private Class c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        if (getIntent().getExtras() != null) {
            CLIENT_ID = getIntent().getStringExtra("id");
            CLIENT_SECRET = getIntent().getStringExtra("secret");
            ACTIVITY_NAME = getIntent().getStringExtra("activity");
        }
        try {
            c = Class.forName(ACTIVITY_NAME);
        } catch (ClassNotFoundException exp) {
            Log.d(TAG, "error :" + exp.getMessage());
        }
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                CODE = url.substring(url.lastIndexOf("?code=") + 1);
                Log.d(TAG, "code fetched is :" + CODE);
                String[] token_code = CODE.split("=");
                fetchOauthTokenWithCode(token_code[1]);
                return false;
            }
        });
        String url_load = GITHUB_URL + "?client_id=" + CLIENT_ID;
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String JsonData = response.body().string();
                    Log.d(TAG, "response is:" + JsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(JsonData);
                        String auth_token = jsonObject.getString("access_token");
                        Log.d(TAG, "token is :" + auth_token);
                        storeToSharedPreference(auth_token);
                    } catch (JSONException exp) {
                        Log.d(TAG, "json exception :" + exp.getMessage());
                    }

                }

            }
        });
    }

    private void storeToSharedPreference(String auth_token) {
        SharedPreferences prefs = getSharedPreferences("github_prefs", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("oauth_token", auth_token);
        edit.commit();
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
