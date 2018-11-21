package org.supremus.sych.sychnews.interfaces;

import org.supremus.sych.sychnews.data.NewsItem;

public interface NewsItemProvider {
    NewsItem getItem();
    void setItem(NewsItem val);
}
