package com.hardikgoswami.github_oauth_lib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String GITHUB_ID = BuildConfig.GITHUB_ID;
    public static final String GITHUB_SECRET = BuildConfig.GITHUB_SECRET;
    Button loginButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.login_btn);
        context = getApplicationContext();
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
    }


}
