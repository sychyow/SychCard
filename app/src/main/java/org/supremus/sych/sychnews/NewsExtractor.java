package org.supremus.sych.sychnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NewsExtractor {

    private static Map<String, Integer> Categories = new HashMap<>();

    private static Category getCategory(String name) {
        if ( name == null||name.equals("")) return null;
        if (Categories.containsKey(name)) {
            return new Category(Categories.get(name), name);
        }
        int newNum = Categories.size()+1;
        Categories.put(name, newNum);
        return new Category(newNum, name);
    }

    private static String getImageUrl(ResultDTO res) {
        if (res.getMultimedia().size()==0) return "http://static01.nytimes.com/packages/images/developer/logos/poweredby_nytimes_30a.png";
        for (MultimediaDTO mm: res.getMultimedia()) {
            if (!mm.getType().equals("image")) continue;
            if (mm.getFormat().equals("Normal")) return mm.getUrl();
        }
        return res.getMultimedia().get(0).getUrl();
    }

    public static List<NewsItem> extract(FeedDTO feed) {
        List<NewsItem> news = new ArrayList<NewsItem>();
        Categories.clear();
        for (ResultDTO res:feed.getResults()){
            NewsItem item = new NewsItem(res.getTitle(), getImageUrl(res), getCategory(res.getSubsection()),
                    res.getPublishedDate(), res.getAbstract(), res.getUrl());
            news.add(item);
        }
        return news;
    }
}
