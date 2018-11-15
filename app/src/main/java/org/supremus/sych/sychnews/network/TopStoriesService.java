package org.supremus.sych.sychnews.network;

import org.supremus.sych.sychnews.data.FeedDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TopStoriesService {
    @GET("{section}.json")
    Call<FeedDTO> getStories(@Path("section") String section);
}
