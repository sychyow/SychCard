package org.supremus.sych.sychnews.util;

import org.supremus.sych.sychnews.network.NYTApi;
import org.supremus.sych.sychnews.data.Category;
import org.supremus.sych.sychnews.data.FeedDTO;
import org.supremus.sych.sychnews.data.MultimediaDTO;
import org.supremus.sych.sychnews.data.NewsEntity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.data.ResultDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NewsExtractor {

    private static final Map<String, Integer> Categories = new HashMap<>();

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
        if (res.getMultimedia().size()==0) return "";
        for (MultimediaDTO mm: res.getMultimedia()) {
            if (!mm.getType().equals("image")) continue;
            if (mm.getFormat().equals("Normal")) return mm.getUrl();
        }
        return res.getMultimedia().get(0).getUrl();
    }

    public static List<NewsItem> extract(FeedDTO feed) {
        List<NewsItem> news = new ArrayList<>();
        Categories.clear();
        for (ResultDTO res:feed.getResults()){
            NewsItem item = new NewsItem(res.getTitle(), getImageUrl(res), getCategory(res.getSubsection()),
                    res.getPublishedDate(), res.getAbstract(), res.getUrl());
            news.add(item);
        }
        return news;
    }

    public static List<NewsItem> extract(List<NewsEntity> input) {
        List<NewsItem> news = new ArrayList<>();
        Categories.clear();
        for (NewsEntity ent:input){
            NewsItem item = makeItem(ent);
            news.add(item);
        }
        return news;
    }

    public static NewsEntity makeEntity(NewsItem item) {
        NewsEntity entity = new NewsEntity(
                item.getTitle(),
                item.getImageUrl(),
                item.getCategory()!=null?
                item.getCategory().getName():null,
                item.getPublishDate(),
                item.getPreviewText(),
                item.getFullText());
        entity.section = NYTApi.getCurrentSection();
        entity.id = item.getId();
        return entity;
    }

    public static NewsItem makeItem(NewsEntity ent) {
        NewsItem item =  new NewsItem(ent.title, ent.imageUrl, getCategory(ent.category),
                ent.publishDate, ent.previewText, ent.fullText);
        item.setId(ent.id);
        return item;
    }
}
