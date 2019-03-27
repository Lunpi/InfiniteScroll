package com.example.infinitescroll.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.infinitescroll.repository.ContentRepository;
import com.example.infinitescroll.reddit.RedditPost;

import java.util.List;

import javax.inject.Inject;

public class ContentViewModel extends ViewModel {
    private ContentRepository mContentRepo;
    private LiveData<List<RedditPost>> mPosts;

    @Inject
    public ContentViewModel(ContentRepository contentRepository) {
        mContentRepo = contentRepository;
        mPosts = mContentRepo.getRedditPosts();
    }

    public LiveData<List<RedditPost>> getRedditPosts() {
        return mPosts;
    }

    public void getContent() {
        mContentRepo.getContent(false);
    }

    public void refreshContent() {
        mContentRepo.refreshContent();
    }
}
