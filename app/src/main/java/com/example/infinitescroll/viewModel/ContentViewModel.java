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
        mPosts = mContentRepo.loadRedditPostsFromDb();
    }

    /**
     * Get the LiveData we want to observe, it's posts from Reddit in this case.
     * It doesn't invoke loading data from Database.
     * @return The LiveData of Reddit posts
     */
    public LiveData<List<RedditPost>> getLiveDataPosts() {
        return mPosts;
    }

    /**
     * Tell the repository to fetch new content.
     * It will invoke onChange() callback in the observer after inserting new data into database. 
     */
    public void getContent() {
        mContentRepo.getContent(false);
    }

    /**
     * Tell the repository to refresh content.
     * It will first delete all contents and then fetch again. 
     */
    public void refreshContent() {
        mContentRepo.refreshContent();
    }
}
