package org.supremus.sych.sychnews;

import org.supremus.sych.sychnews.data.NewsItem;

public interface UIUpdater {
    void makeViewer();
    void updateUI(NewsItem item);
}
