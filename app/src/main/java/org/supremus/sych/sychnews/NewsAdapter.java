package org.supremus.sych.sychnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "SychNews.NewsAdapter";

    @Override
    public void onClick(View v) {
        RecyclerView rv = (RecyclerView) v.getParent();
        int pos = rv.getChildLayoutPosition(v);
        NewsDetailActivity.launch(rv.getContext(), data.get(pos));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(NewsItem item) {
            Category.setText(item.getCategory().getName());
            Preview.setText(item.getPreviewText());
            Locale loc = Timestamp.getResources().getConfiguration().getLocales().get(0);
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, loc);
            Timestamp.setText(df.format(item.getPublishDate()));
            Glide.with(itemView).load(item.getImageUrl()).into(Image);
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
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
