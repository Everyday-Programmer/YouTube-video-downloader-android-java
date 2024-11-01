package com.example.youtubevideodownloader;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/download_url")
    Call<DownloadResponse> getDownloadUrl(@Body VideoUrl videoUrl);

    @POST("/video_info")
    Call<VideoDetails> getVideoDetails(@Body VideoUrl videoUrl);
}