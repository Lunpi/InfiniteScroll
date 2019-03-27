package com.example.infinitescroll.repository;

import android.os.AsyncTask;

import com.example.infinitescroll.model.PostDao;

class CheckDataCountAsyncTask extends AsyncTask<Void, Void, Void> {
    private final ContentRepository mRepo;
    private final PostDao mDao;
    CheckDataCountAsyncTask(ContentRepository repo, PostDao dao) {
        mRepo = repo;
        mDao = dao;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        if (mDao.checkDataCount() == 0) {
            mRepo.getPostsFromReddit(true);
        }
        return null;
    }
}
