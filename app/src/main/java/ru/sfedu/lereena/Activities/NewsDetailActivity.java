package ru.sfedu.lereena.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.sfedu.lereena.Adapters.AttachmentAdapter;
import ru.sfedu.lereena.R;

public class NewsDetailActivity extends Activity {

    private RecyclerView recyclerView;
    private ImageView imageView;
    private TextView dateView, textView, likeView;
    private ArrayList<String> attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        initViews();
        setData();
    }

    private void initViews() {
        imageView = findViewById(R.id.imageViewDetail);
        dateView = findViewById(R.id.dateViewDetail);
        textView = findViewById(R.id.textViewDetail);
        likeView = findViewById(R.id.likeViewDetail);
        recyclerView = findViewById(R.id.recyclerViewDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setData() {
        Intent intent = getIntent();
        attachment = intent.getStringArrayListExtra("attachment");
        Picasso.with(this).load(intent.getStringExtra("image")).into(imageView);
        dateView.setText(intent.getStringExtra("date"));
        textView.setText(intent.getStringExtra("text"));
        likeView.setText(getString(R.string.total_likes) + intent.getLongExtra("like", 0)); // TODO: разобраться и заменить плейсхолдером
        recyclerView.setAdapter(new AttachmentAdapter(this, attachment));
    }
}
