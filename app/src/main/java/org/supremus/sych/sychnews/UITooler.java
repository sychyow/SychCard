package org.supremus.sych.sychnews;

import org.supremus.sych.sychnews.data.NewsItem;

public interface UITooler {
    UITooler getUITool(int mode);
    UITooler setNewsAdapter(NewsAdapter na);
    UITooler setErrorText(String msg);
    void apply();
}
