package ru.sfedu.lereena.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.opencsv.CSVReader;
import com.vk.sdk.VKScope;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.sfedu.lereena.Adapters.NewsAdapter;
import ru.sfedu.lereena.ModelItem;
import ru.sfedu.lereena.R;
import ru.sfedu.lereena.RecyclerItemClickListener;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener {

    final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.mm.yyy hh:mm", Locale.getDefault());
    public static final int DWNLD = 0, DWNLDED = 1;
    final int COUNT = 100;
    private boolean startedDownload, fromStart, inProcess;
    private VKRequest request;
    private String startValue;
    private ProgressBar progressBar;
    private Handler handler;
    private RecyclerView recView;
    private NewsAdapter adapter;
    private LinearLayoutManager llm;
    private int totalItemCnt, lastVisibleItem;
    private Thread thread;

    private String[][] urls;
    private List<ModelItem> items;
    private String[] scope = new String[]{VKScope.GROUPS, VKScope.PAGES, VKScope.NOTIFICATIONS, VKScope.NOTIFY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(

                )
                /*.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        InputMethodManager inputMethodManager = (InputMethodManager) FeedActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(FeedActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }
                })*/
                .build();

        initializeURLsArr(this);
        initializeDrawerItems(result);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        llm = new LinearLayoutManager(this);

        initHandler();
        initRecV();
        initializeViews();
        initializeNews();
        updateAdapter();
    }

    private void initializeDrawerItems(Drawer drawer) {
        for (int i = 1; i < urls[0].length; i++) {
            drawer.addItem(new PrimaryDrawerItem().withIdentifier(i).withName(urls[0][i]));
        }
    }

    private void initializeURLsArr(Context context) {

        urls = new String[28][27];

        CSVReader reader;
        reader = new CSVReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.vkgroupsids)));

        String[] line;
        try {
            for (int i = 0; i < 28; i++) {
                    line = reader.readNext();
                    urls[i] = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DWNLD:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case DWNLDED:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        };
    }

    private void initRecV() {
        recView = findViewById(R.id.recv);
        recView.setLayoutManager(llm);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCnt = llm.getItemCount();//сколько всего элементов
                lastVisibleItem = llm.findLastVisibleItemPosition();//какая позиция последнего элемента на экране
                if (!startedDownload && lastVisibleItem == totalItemCnt - 1 && !inProcess) {
                    startedDownload = true;
                    downloadMoreNews();
                } else {
                    startedDownload = false;
                }
            }
        });
        recView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ModelItem n = items.get(position);
                Intent intent = new Intent(FeedActivity.this, NewsDetailActivity.class);
                intent.putExtra("image", n.getPhotoURL());
                intent.putExtra("date", n.getDate());
                intent.putExtra("text", n.getText());
                intent.putExtra("like", n.getLike());
                intent.putStringArrayListExtra("attachment", n.getAttachmentURLs());
                startActivity(intent);
            }
        }));
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
    }

    private void initializeNews() {
        handler.sendEmptyMessage(DWNLD);
        fromStart = true;
        startValue = "";
        items = new ArrayList<>();
        if (request != null) {
            request.cancel();
        }
        request = new VKRequest("newsfeed.get",
                VKParameters.from(VKApiConst.FILTERS, "post", VKApiConst.COUNT, COUNT));
        downloadNews();
    }

    private void updateAdapter() {
        adapter = new NewsAdapter(FeedActivity.this, items);
        recView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();
    }

    private void downloadMoreNews() {
        handler.sendEmptyMessage(DWNLD);
        fromStart = false;
        request = new VKRequest("newsfeed.get",
                VKParameters.from(VKApiConst.FILTERS, "post",
                        VKApiConst.COUNT, COUNT, "start_from", startValue));
        downloadNews();
    }

/*    private void downloadMoreNews(String groupID) {
        handler.sendEmptyMessage(DWNLD);
        fromStart = false;
        request = new VKApiGroups().getById(VKParameters.from("group_ids", groupID, VKApiConst.COUNT, COUNT, "start_from", startValue));
        downloadNews();
    }*/

    private void downloadNews() {
        if (!inProcess) {
            thread = new Thread(new Runnable() {
                public void run() {
                    inProcess = true;
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @SuppressLint("UseSparseArrays")
                        @Override
                        public void onComplete(VKResponse response) {
                            super.onComplete(response);
                            Map<Long, String> profileID, groupID;
                            profileID = new HashMap<>();
                            groupID = new HashMap<>();
                            int prevSize = items.size();
                            try {
                                JSONObject jsonResponse = response.json.getJSONObject("response");
                                if (!startValue.equals(jsonResponse.getString("next_from"))) {
                                    putInMap(jsonResponse.getJSONArray("profiles"), profileID);
                                    putInMap(jsonResponse.getJSONArray("groups"), groupID);
                                    startValue = jsonResponse.getString("next_from");
                                    prepareNews(jsonResponse.getJSONArray("items"), profileID, groupID);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (fromStart) {
                                updateAdapter();
                            } else {
                                recView.getAdapter().notifyItemRangeInserted(prevSize, items.size());
                            }
                            handler.sendEmptyMessage(DWNLDED);
                            inProcess = false;
                        }
                    });
                }
            });
            thread.start();
        }
    }

    private void putInMap(JSONArray array, Map map) throws JSONException {
        JSONObject object;
        for (int i = 0; i < array.length(); i++) {
            object = array.getJSONObject(i);
            map.put(object.getLong("id"), object.getString("photo_100"));
        }
    }

    private void prepareNews(JSONArray array, Map<Long, String> pID, Map<Long, String> gID) throws JSONException {
        Date date;
        JSONObject object, attObject;
        JSONArray attachment;
        Long sourceID, like;
        String photoURL, text, dateString, attachmentURL, type;
        ArrayList<String> attachmentURLs;
        for (int i = 0; i < array.length(); i++) {
            object = array.getJSONObject(i);
            sourceID = object.getLong("source_id");
            photoURL = sourceID > 0 ? pID.get(sourceID) : gID.get(-sourceID);
            text = object.get("text").toString();
            date = new Date(object.getLong("date") * 1000);
            dateString = DATE_FORMAT.format(date);
            like = object.getJSONObject("likes").getLong("count");
            attachment = object.optJSONArray("attachments");
            attachmentURLs = new ArrayList<>();
            if (attachment != null) {
                for (int j = 0; j < attachment.length(); j++) {
                    attObject = attachment.getJSONObject(j);
                    type = attObject.getString("type");
                    if (type.equals("link")) {
                        attObject = attObject.getJSONObject("link");
                    }
                    if (type.equals("photo") || type.equals("link")) {
                        attachmentURL = attObject.getJSONObject("photo").optString("photo_604");
                        if (attachmentURL != null) {
                            attachmentURLs.add(attachmentURL);
                        } else {
                            attachmentURLs.add(attObject.getJSONObject("photo").optString("photo_130"));
                        }
                    }
                }
            }
            if (!text.isEmpty()) {
                items.add(new ModelItem(photoURL, dateString, text, like, attachmentURLs));
            }
        }
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

}

