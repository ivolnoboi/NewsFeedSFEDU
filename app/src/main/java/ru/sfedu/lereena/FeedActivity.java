package ru.sfedu.lereena;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiGroups;
import com.vk.sdk.api.methods.VKApiWall;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView recView;
    private LinearLayoutManager vertLLM;
    private LinearLayoutManager horizLLM;

    private String[] scope = new String[]{VKScope.GROUPS, VKScope.PAGES, VKScope.NOTIFICATIONS, VKScope.NOTIFY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        VKSdk.login(this);

        recView = findViewById(R.id.recv);
        recView.setHasFixedSize(true);

        vertLLM = new LinearLayoutManager(this);
        horizLLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recView.setLayoutManager(vertLLM);

        initializeData();
        initializeAdapter();
    }

    private List<ModelItem> items;

    public void initializeData(){
        items = new ArrayList<>();
        items.add(new ModelItem("text1", R.drawable.monad));
        items.add(new ModelItem("text2", R.drawable.hoursofexam));
        items.add(new ModelItem("text3", R.drawable.wut));
    }

    private void initializeAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(items);
        recView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                VKRequest vkRequest = new VKApiGroups().getById(VKParameters.from("group_ids", "mmcs_sfedu"));
                vkRequest.executeSyncWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);

                        VKList vkList = (VKList) response.parsedModel;
                        try {
                            VKRequest vkRequest1 = new VKApiWall()
                                    .get(VKParameters.from(VKApiConst.OWNER_ID, vkList.get(0).fields.getInt("id"), VKApiConst.COUNT, 10));
                            vkRequest1.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    super.onComplete(response);
                                    try {
                                        JSONObject jsonObject = (JSONObject) response.json.get("response");
                                        JSONArray jsonArray = (JSONArray) jsonObject.get("items");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject post = (JSONObject) jsonArray.get(i);
                                            //arrayList.add(post.getString("text"));
                                            items.add(new ModelItem(post.getString("text"), R.drawable.monad));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            //arrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.content_main, arrayList);
                            //listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
                Toast.makeText(getApplicationContext(), "bad", Toast.LENGTH_LONG);
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback) ) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recView.setLayoutManager(horizLLM);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recView.setLayoutManager(vertLLM);
        }
    }
}
