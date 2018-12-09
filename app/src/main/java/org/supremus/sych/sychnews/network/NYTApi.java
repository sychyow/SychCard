package org.supremus.sych.sychnews.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.supremus.sych.sychnews.MainActivity;
import org.supremus.sych.sychnews.data.NewsItem;
import org.supremus.sych.sychnews.util.DataUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NYTApi {
    public static final List<String> SECTIONS = Arrays.asList("home",
            "opinion",
            "world",
            "national",
            "politics",
            "upshot",
            "nyregion",
            "business",
            "technology",
            "theater",
            "sundayreview",
            "fashion",
            "magazine",
            "food",
            "travel",
            "magazine",
            "realestate",
            "automobiles",
            "obituaries",
            "insider");
    public static final int TIMEOUT = 5000; //Request timeout in ms


    private static final String URL = "https://api.nytimes.com/svc/topstories/v2/";
    private static final String API_KEY = "026d805aa17848b98e01e6d392a23fd0";

    private static final int TIMEOUT_IN_SECONDS = 2;
    private static NYTApi nytApi;
    private static TopStoriesService topStoriesService;
    private static String currentSection = "world";
    private static boolean enabled = false;
    private static int changedId = -1;
    private static int removedId = -1;
    private static boolean twoPanel = false;


    public static synchronized NYTApi getInstance() {
        if (nytApi == null) {
            nytApi = new NYTApi();
        }
        return nytApi;
    }

    public static String[] getSectionNames() {
        String res[] = new String[SECTIONS.size()];
        for(int i=0; i<SECTIONS.size(); i++)
            res[i] = DataUtils.capitaliseFL(SECTIONS.get(i));
        return res;
    }

    public static int getSelectedIndex() {
        for(int i=0; i<SECTIONS.size(); i++)
            if (currentSection.equals(SECTIONS.get(i))) return i;
        return -1;
    }


    private NYTApi() {
        final OkHttpClient httpClient = buildOkHttpClient();
        final Retrofit retrofit = buildRetrofitClient(httpClient);

        //init endpoints here. It's can be more then one endpoint
        topStoriesService = retrofit.create(TopStoriesService.class);
    }

    public static TopStoriesService getTopStoriesService() {
        return topStoriesService;
    }

    public static void setCurrentSection(int index) {
        NYTApi.currentSection = SECTIONS.get(index);
    }

    public static String getCurrentSection() {
        return currentSection;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        NYTApi.enabled = enabled;
    }

    public static void setChangedId(int id) {
        changedId = id;
    }

    public static int getChangedId() {
        return changedId;
    }

    public static void setRemovedId(int removedId) {
        NYTApi.removedId = removedId;
    }

    public static int getRemovedId() {
        return removedId;
    }

    public static NewsItem getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(NewsItem selectedItem) {
        NYTApi.selectedItem = selectedItem;
    }

    public static int getActivityMode() {
        return activityMode;
    }

    public static void setActivityMode(int activityMode) {
        NYTApi.activityMode = activityMode;
    }

    public static boolean isTwoPanel() {
        return twoPanel;
    }

    public static void setTwoPanel(boolean twoPanel) {
        NYTApi.twoPanel = twoPanel;
    }


    @NonNull
    private Retrofit buildRetrofitClient(@NonNull OkHttpClient client) {
        Gson gson = new GsonBuilder()
                .setDateFormat(DataUtils.DATE_FORMAT)
                .create();

        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @NonNull
    private OkHttpClient buildOkHttpClient() {
        final HttpLoggingInterceptor networkLogInterceptor = new HttpLoggingInterceptor();
        networkLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);


        return new OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor.create(API_KEY))
                .addInterceptor(networkLogInterceptor)
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    private static NewsItem selectedItem;

    private static int activityMode = MainActivity.MODE_LIST;

}
