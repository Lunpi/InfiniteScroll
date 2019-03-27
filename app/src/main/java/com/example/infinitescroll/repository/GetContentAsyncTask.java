package com.example.infinitescroll.repository;

import android.os.AsyncTask;

class GetContentAsyncTask extends AsyncTask<Boolean, Void, Void> {
    private final ContentRepository mRepo;
    GetContentAsyncTask(ContentRepository repo) {
        mRepo = repo;
    }

    @Override
    protected Void doInBackground(final Boolean... params) {
        mRepo.getPostsFromReddit(params[0]);
        return null;
    }
}
