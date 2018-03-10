package ru.sfedu.lereena;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder> {

    private List<ModelItem> items;

    RecyclerAdapter(List<ModelItem> items) {
        this.items = items;
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView image;

        // присваиваем элементы из xml
        ItemsViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.card_text);
            image = itemView.findViewById(R.id.card_image);
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
        holder.image.setImageResource(items.get(position).imgId);
        holder.text.setText(items.get(position).text);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
