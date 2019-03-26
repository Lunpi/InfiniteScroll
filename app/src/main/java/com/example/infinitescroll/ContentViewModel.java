package com.example.infinitescroll;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.infinitescroll.reddit.RedditPost;

import java.util.List;

import javax.inject.Inject;

public class ContentViewModel extends ViewModel {
    private ContentRepository mContentRepo;
    private LiveData<List<RedditPost>> mPosts;

    @Inject
    public ContentViewModel(ContentRepository contentRepository) {
        this.mContentRepo = contentRepository;
        mPosts = mContentRepo.getPosts();
    }

    LiveData<List<RedditPost>> getPosts() {
        return mPosts;
    }

    public void fetchPosts() {
        mContentRepo.fetchNewContent();
    }
}
