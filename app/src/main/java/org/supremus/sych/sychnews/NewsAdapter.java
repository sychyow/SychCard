package org.supremus.sych.sychnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Response;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "SychNews.NewsAdapter";

    private static final  int VIEW_STANDARD = 1;
    private static final  int VIEW_ALTERNATIVE = 2;

    @Override
    public void onClick(View v) {
        RecyclerView rv = (RecyclerView) v.getParent();
        int pos = rv.getChildLayoutPosition(v);
        NewsDetailActivity.launch(rv.getContext(), data.get(pos));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView Category;
        final ImageView Image;
        TextView Header;
        TextView Preview;
        TextView Timestamp;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Category = itemView.findViewById(R.id.tv_category);
            Image = itemView.findViewById(R.id.iv_image);
            Header = itemView.findViewById(R.id.tv_header);
            Preview = itemView.findViewById(R.id.tv_preview);
            Timestamp= itemView.findViewById(R.id.tv_timestamp);
        }

        void bind(NewsItem item) {
            Category.setText(item.getCategory().getName());
            Header.setText(item.getTitle());
            Preview.setText(item.getPreviewText());
            DataUtils.setDateString(Timestamp, item.getPublishDate());
            Glide.with(itemView).load(item.getImageUrl()).into(Image);
        }
    }

    private List<NewsItem> data;

    NewsAdapter() {
        //data = DataUtils.generateNews();
        TopStoriesService svc = NYTApi.getInstance().getTopStoriesService();
        try {
            Response<FeedDTO> response = svc.getStories("world").execute();
            if (response.code()==200) {
                data = NewsExtractor.extract(response.body());
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        data = new ArrayList<>();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_STANDARD:
                v=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item, parent, false);
                break;
            case VIEW_ALTERNATIVE:
                v=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_alt, parent, false);
                break;
                default: return null;
        }
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

    @Override
    public int getItemViewType(int pos) {
        Category c = data.get(pos).getCategory();
        switch (c.getId()) {
            case 2: return VIEW_ALTERNATIVE;
        }
        return VIEW_STANDARD;
    }

}
