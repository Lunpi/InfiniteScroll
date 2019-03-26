package com.example.infinitescroll;

import android.app.Application;

import com.example.infinitescroll.reddit.RedditClient;

import androidx.lifecycle.ViewModelProviders;
import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {
    private Application mApplication;
    private ScrollFragment mScrollFragment;
    public ViewModelModule(Application application, ScrollFragment scrollFragment) {
        this.mApplication = application;
        this.mScrollFragment = scrollFragment;
    }

    @Provides
    ScrollFragment provideScrollFragment() {
        return mScrollFragment;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    ContentViewModel provideContentViewModel(ScrollFragment scrollFragment, ViewModelFactory factory) {
        return ViewModelProviders.of(scrollFragment, factory).get(ContentViewModel.class);
    }

    @Provides
    ViewModelFactory provideViewModelFactory(ContentRepository contentRepository) {
        return new ViewModelFactory(contentRepository);
    }

    @Provides
    ContentRepository provideContentRepository(Application application) {
        return new ContentRepository(application, new RedditClient());
    }
}
