package com.example.infinitescroll;

import dagger.Component;

@Component(modules = ViewModelModule.class)
public interface ViewModelComponent {
    void inject(ScrollFragment scrollFragment);
}
