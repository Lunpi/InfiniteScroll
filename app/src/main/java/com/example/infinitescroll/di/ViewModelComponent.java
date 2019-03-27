package com.example.infinitescroll.di;

import com.example.infinitescroll.ScrollFragment;

import dagger.Component;

@Component(modules = ViewModelModule.class)
public interface ViewModelComponent {
    void inject(ScrollFragment scrollFragment);
}
