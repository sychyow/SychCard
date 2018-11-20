package org.supremus.sych.sychnews;

public interface UITooler {
    UITooler getUITool(int mode);
    UITooler setNewsAdapter(NewsAdapter na);
    UITooler setErrorText(String msg);
    void apply();
}
