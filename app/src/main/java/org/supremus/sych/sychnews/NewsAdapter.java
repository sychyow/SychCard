package org.supremus.sych.sychnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final String TAG = "SychNews.NewsAdapter";

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Category;
        ImageView Image;
        TextView Preview;
        TextView Timestamp;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Category = itemView.findViewById(R.id.tv_category);
            Image = itemView.findViewById(R.id.iv_image);
            Preview = itemView.findViewById(R.id.tv_preview);
            Timestamp= itemView.findViewById(R.id.tv_timestamp);
        }
    }

    private List<NewsItem> data;

    NewsAdapter() {
        data = DataUtils.generateNews();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = data.get(position);
        holder.Category.setText(item.getCategory().getName());
        holder.Preview.setText(item.getPreviewText());
        holder.Timestamp.setText(item.getPublishDate().toString());
        Glide.with(holder.itemView).load(item.getImageUrl()).into(holder.Image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
