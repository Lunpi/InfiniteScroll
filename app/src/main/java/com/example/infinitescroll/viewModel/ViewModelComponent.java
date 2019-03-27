package com.example.infinitescroll.viewModel;

import com.example.infinitescroll.ScrollFragment;

import dagger.Component;

@Component(modules = ViewModelModule.class)
public interface ViewModelComponent {
    void inject(ScrollFragment scrollFragment);
}
