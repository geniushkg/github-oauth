package com.hardikgoswami.github_oauth_lib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hardikgoswami.oauth_lib.GithubOauth;

public class MainActivity extends AppCompatActivity {

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
                        .nextActivity(UserActivity.class)
                        .withContext(getApplicationContext())
                        .withClientSecret("xxxx")
                        .withClientId("xxx")
                        .execute();
            }
        });
    }





}
