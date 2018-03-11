package ru.sfedu.lereena.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import ru.sfedu.lereena.R;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        ImageView logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.background);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                InitActivity.this.startActivity(new Intent(InitActivity.this, FeedActivity.class));
            }

            @Override
            public void onError(VKError error) {
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();
        if (buttonId == R.id.login_button) {
            VKSdk.login(InitActivity.this, scope);
        }
    }
}
