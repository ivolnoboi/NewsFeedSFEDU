package ru.sfedu.lereena.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.sfedu.lereena.ModelItem;
import ru.sfedu.lereena.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView photo;
        TextView date, text;

        NewsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.c_view);
            photo = itemView.findViewById(R.id.card_authors_photo);
            date = itemView.findViewById(R.id.card_date);
            text = itemView.findViewById(R.id.card_text);
        }
    }

    List<ModelItem> news;
    Context context;

    public NewsAdapter(Context context, List<ModelItem> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false); // TODO: если будет падать проверять здесь
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int i) {
        Picasso.with(context).load(news.get(i).getPhotoURL()).into(newsViewHolder.photo);
        newsViewHolder.date.setText(news.get(i).getDate());
        newsViewHolder.text.setText(news.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
