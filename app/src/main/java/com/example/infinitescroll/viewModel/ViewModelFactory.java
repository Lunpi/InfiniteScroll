package com.example.infinitescroll.viewModel;

import com.example.infinitescroll.repository.ContentRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private ContentRepository mContentRepository;
    public ViewModelFactory(ContentRepository contentRepository) {
        this.mContentRepository = contentRepository;
    }
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ContentViewModel.class)) {
            return (T) new ContentViewModel(mContentRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
