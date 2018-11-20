package org.supremus.sych.sychnews;

import android.view.View;

import org.supremus.sych.sychnews.tasks.LoadDataTask;

public class NewsLoader {

    private static Boolean forceNet = false;

    private static Boolean isUpdate = false;

    public static void forceNetwork() { forceNet = true; }

    public static void load(NewsListActivity nla) {
        nla.getRv().setVisibility(View.GONE);
        nla.getPb().setVisibility(View.VISIBLE);
        int mode = forceNet ? LoadDataTask.TASK_NETWORK : LoadDataTask.TASK_DB;
        forceNet = false;
        new LoadDataTask(nla, mode, isUpdate).execute();
    }

    public static void setUpdate(Boolean isUpdate) {
        NewsLoader.isUpdate = isUpdate;
    }
}
