package com.example.youtubevideodownloader;

public class VideoDetails {
    private String title;
    private String thumbnail;
    private String uploader;
    private String description;
    private String upload_date;
    private String url;
    private int duration;
    private int view_count;
    private int like_count;
    private int dislike_count;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public int getDislike_count() {
        return dislike_count;
    }

    public int getDuration() {
        return duration;
    }

    public int getLike_count() {
        return like_count;
    }

    public int getView_count() {
        return view_count;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDislike_count(int dislike_count) {
        this.dislike_count = dislike_count;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }
}