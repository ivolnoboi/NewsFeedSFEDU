package ru.sfedu.lereena.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.sfedu.lereena.R;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {

    public static class AttachmentViewHolder extends RecyclerView.ViewHolder {

        ImageView attachment;
        ProgressBar progressBar;

        public AttachmentViewHolder(View view) {
            super(view);
            attachment = view.findViewById(R.id.attachmentImageView);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    Context context;
    ArrayList<String> imageUrls;

    public AttachmentAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_detail, parent, false);
        return new AttachmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AttachmentViewHolder holder, int i) {
        Picasso.with(context).load(imageUrls.get(i)).into(holder.attachment, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
