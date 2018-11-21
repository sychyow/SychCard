package org.supremus.sych.sychnews.interfaces;

import org.supremus.sych.sychnews.NewsAdapter;

public interface UITooler {
    UITooler getUITool(int mode);
    UITooler setNewsAdapter(NewsAdapter na);
    UITooler setErrorText(String msg);
    void apply();
}
