package org.supremus.sych.sychnews;

import java.util.ArrayList;
import java.util.List;

public final class NewsExtractor {

    private static String getImageUrl(ResultDTO res) {
        if (res.getMultimedia().size()==0) return "";
        for (MultimediaDTO mm: res.getMultimedia()) {
            if (!mm.getType().equals("image")) continue;
            if (mm.getWidth()==150) return mm.getUrl();
        }
        return res.getMultimedia().get(0).getUrl();
    }

    public static List<NewsItem> extract(FeedDTO feed) {
        List<NewsItem> news = new ArrayList<NewsItem>();
        for (ResultDTO res:feed.getResults()){
            NewsItem item = new NewsItem(res.getTitle(), getImageUrl(res), new Category(1,res.getSubsection()),
                    res.getPublishedDate(), res.getAbstract(), res.getUrl());
            news.add(item);
        }
        return news;
    }
}