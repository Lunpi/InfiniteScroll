package com.example.infinitescroll;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.infinitescroll.reddit.RedditPost;

import java.util.List;

public class ContentViewModel extends AndroidViewModel {
    private LiveData<List<RedditPost>> mPosts;
    private ContentRepository contentRepo;

    public ContentViewModel(Application application) {
        super(application);
        contentRepo = new ContentRepository(application);
        mPosts = contentRepo.getPosts();
    }

    LiveData<List<RedditPost>> getPosts() {
        return mPosts;
    }

    public void fetchPosts() {
        contentRepo.fetchNewContent();
    }
}
