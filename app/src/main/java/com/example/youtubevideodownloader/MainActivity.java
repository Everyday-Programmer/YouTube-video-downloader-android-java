package com.example.youtubevideodownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.youtubevideodownloader.databinding.ActivityMainBinding;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import java.text.MessageFormat;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Skeleton skeleton;
    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(binding.urlET.getText()).toString().isEmpty()) {
                    binding.urlLayout.setError("Please paste video url!");
                } else {
                    binding.videoCardView.setVisibility(View.VISIBLE);

                    skeleton = SkeletonLayoutUtils.createSkeleton(binding.linearLayout);

                    skeleton.showSkeleton();

                    ApiService apiService = ApiClient.getClient("http://10.0.2.2:5000").create(ApiService.class); // Use your server IP if you're not the server running locally

                    VideoUrl videoUrl = new VideoUrl(binding.urlET.getText().toString());
                    Call<VideoDetails> call = apiService.getVideoDetails(videoUrl);
                    call.enqueue(new Callback<VideoDetails>() {
                        @Override
                        public void onResponse(@NonNull Call<VideoDetails> call, @NonNull Response<VideoDetails> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                VideoDetails videoDetails = response.body();

                                Glide.with(getApplicationContext()).load(videoDetails.getThumbnail()).transform(new CenterCrop(), new RoundedCorners(32)).into(binding.videoThumbnail);

                                binding.videoTitle.setText(videoDetails.getTitle());
                                binding.videoUploader.setText(MessageFormat.format("Uploaded By: {0}", videoDetails.getUploader()));
                                binding.videoDuration.setText(MessageFormat.format("Duration: {0} seconds", videoDetails.getDuration())); // This will be in seconds
                                binding.videoLikeCount.setText(MessageFormat.format("Like count: {0}", videoDetails.getLike_count()));
                                binding.videoDislikeCount.setText(MessageFormat.format("Dislike count: {0}", videoDetails.getDislike_count()));
                                binding.videoUploadDate.setText(MessageFormat.format("Uploaded on: {0}", videoDetails.getUpload_date()));
                                binding.videoViewCount.setText(MessageFormat.format("View Count: {0}", videoDetails.getView_count()));
                                binding.videoDescription.setText(videoDetails.getDescription());

                                binding.download.setVisibility(View.VISIBLE);

                                skeleton.showOriginal();
                            } else {
                                Log.e("Error", "Failed to get video details");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<VideoDetails> call, @NonNull Throwable throwable) {
                            Log.e("Error", "Network error", throwable);
                        }
                    });
                }
            }
        });

        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService apiService = ApiClient.getClient("http://10.0.2.2:5000").create(ApiService.class);

                VideoUrl videoUrl = new VideoUrl(Objects.requireNonNull(binding.urlET.getText()).toString());
                Call<DownloadResponse> call = apiService.getDownloadUrl(videoUrl);
                call.enqueue(new Callback<DownloadResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DownloadResponse> call, @NonNull Response<DownloadResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            DownloadResponse downloadResponse = response.body();

                            downloadVideo(downloadResponse.getDownload_url(), downloadResponse.getTitle());

                            Toast.makeText(MainActivity.this, "Downloading video...", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Error", "Failed to get download url");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DownloadResponse> call, @NonNull Throwable throwable) {
                        Log.e("Error", "Network error", throwable);
                    }
                });
            }
        });
    }

    private void downloadVideo(String videoUrl, String title) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(videoUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloading video");
        request.setDescription("Downloading video from " + videoUrl);
        request.allowScanningByMediaScanner();

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".mp4");

        downloadManager.enqueue(request);
    }
}