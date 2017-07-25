package com.hardikgoswami.oauthLibGithub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
    public static String CODE = "";
    public static String PACKAGE = "";
    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";
    public static String ACTIVITY_NAME = "";

    private static final String TAG = "github-oauth";

    public String scopeAppendToUrl = "";
    public List<String> scopeList;

    private WebView webview;

    private boolean clearDataBeforeLaunch = false;
    private boolean isScopeDefined = false;
    private boolean debug = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        scopeList = new ArrayList<>();
        scopeAppendToUrl = "";

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            CLIENT_ID = intent.getStringExtra("id");
            PACKAGE = intent.getStringExtra("package");
            CLIENT_SECRET = intent.getStringExtra("secret");
            ACTIVITY_NAME = intent.getStringExtra("activity");
            debug = intent.getBooleanExtra("debug", false);
            isScopeDefined = intent.getBooleanExtra("isScopeDefined", false);
            clearDataBeforeLaunch = intent.getBooleanExtra("clearData", false);
        } else {
            Log.d(TAG, "intent extras null");
            finish();
        }

        String url_load = GITHUB_URL + "?client_id=" + CLIENT_ID;

        if (isScopeDefined) {
            scopeList = intent.getStringArrayListExtra("scope_list");
            scopeAppendToUrl = getCsvFromList(scopeList);
            url_load += "&scope=" + scopeAppendToUrl;
        }

        if (debug) {
            Log.d(TAG, "intent received is "
                    + "\n-client id: " + CLIENT_ID
                    + "\n-secret:" + CLIENT_SECRET
                    + "\n-activity: " + ACTIVITY_NAME
                    + "\n-Package: " + PACKAGE);
            Log.d(TAG, "onCreate: Scope request are : " + scopeAppendToUrl);
        }

        if (clearDataBeforeLaunch) {
            clearDataBeforeLaunch();
        }

        webview = (WebView) findViewById(R.id.webview);

        if (webview == null) {
            return;
        }

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                super.shouldOverrideUrlLoading(view, url);
                // Try catch to allow in app browsing without crashing.
                try {
                    if (!url.contains("?code=")) return false;

                    CODE = url.substring(url.lastIndexOf("?code=") + 1);
                    String[] token_code = CODE.split("=");
                    String tokenFetchedIs = token_code[1];
                    String[] cleanToken = tokenFetchedIs.split("&");

                    fetchOauthTokenWithCode(cleanToken[0]);

                    if (debug) {
                        Log.d(TAG, "code fetched is: " + CODE);
                        Log.d(TAG, "code token: " + token_code[1]);
                        Log.d(TAG, "token cleaned is: " + cleanToken[0]);
                    }
                } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        webview.loadUrl(url_load);
    }

    private void clearDataBeforeLaunch() {
        CookieManager cookieManager = CookieManager.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                // a callback which is executed when the cookies have been removed
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d(TAG, "Cookie removed: " + aBoolean);
                }
            });
        } else {
            //noinspection deprecation
            cookieManager.removeAllCookie();
        }
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
                if (debug) {
                    Log.d(TAG, "IOException: " + e.getMessage());
                }

                finishThisActivity(ResultCode.ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String JsonData = response.body().string();

                    if (debug) {
                        Log.d(TAG, "response is: " + JsonData);
                    }

                    try {
                        JSONObject jsonObject = new JSONObject(JsonData);
                        String auth_token = jsonObject.getString("access_token");

                        storeToSharedPreference(auth_token);

                        if (debug) {
                            Log.d(TAG, "token is: " + auth_token);
                        }

                    } catch (JSONException exp) {
                        if (debug) {
                            Log.d(TAG, "json exception: " + exp.getMessage());
                        }
                    }

                } else {
                    if (debug) {
                        Log.d(TAG, "onResponse: not success: " + response.message());
                    }
                }

                finishThisActivity(ResultCode.SUCCESS);
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
        edit.apply();
    }

    /**
     * Finish this activity and returns the result
     *
     * @param resultCode one of the constants from the class ResultCode
     */
    private void finishThisActivity(int resultCode) {
        setResult(resultCode);
        finish();
    }

    /**
     * Generate a comma separated list of scopes out of the
     *
     * @param scopeList list of scopes as defined
     * @return comma separated list of scopes
     */
    public String getCsvFromList(List<String> scopeList) {
        String csvString = "";

        for (String scope : scopeList) {
            if (!csvString.equals("")) {
                csvString += ",";
            }

            csvString += scope;
        }

        return csvString;
    }
}
