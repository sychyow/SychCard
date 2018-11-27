package org.supremus.sych.sychnews;

import android.view.View;

import org.supremus.sych.sychnews.fragments.NewsListFragment;
import org.supremus.sych.sychnews.tasks.LoadDataTask;

public class NewsLoader {

    private  Boolean forceNet;

    private  Boolean isUpdate;

    private NewsLoader(boolean net, boolean upd) {
        forceNet = net;
        isUpdate = upd;
    }

    public NewsLoader forceNetwork() {
        forceNet = true;
        return this;
    }

    public NewsLoader setUpdate() {
        isUpdate = true;
        return this;
    }

    public static NewsLoader get() {
        return new NewsLoader(false, false);
    }

    public  void load(NewsListFragment nla) {
        nla.getRv().setVisibility(View.GONE);
        nla.getPb().setVisibility(View.VISIBLE);
        int mode = forceNet ? LoadDataTask.TASK_NETWORK : LoadDataTask.TASK_DB;
        forceNet = false;
        new LoadDataTask(nla, mode, isUpdate).execute();
    }


}
