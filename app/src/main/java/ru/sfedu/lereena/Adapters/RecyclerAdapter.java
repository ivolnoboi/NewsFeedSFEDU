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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder> {

    private List<ModelItem> items;
    Context context;

    RecyclerAdapter(Context context, List<ModelItem> items) {
        this.context = context;
        this.items = items;
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView text, date;
        ImageView image;

        // присваиваем элементы из xml
        ItemsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.c_view);
            date = itemView.findViewById(R.id.card_date);
            text = itemView.findViewById(R.id.card_text);
            image = itemView.findViewById(R.id.card_authors_photo);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Picasso.with(context).load(items.get(position).getPhotoURL()).into(holder.image);
        //holder.image.setImageResource(items.get(position).imgId);
        holder.text.setText(items.get(position).getText());
        holder.date.setText(items.get(position).getDate());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
