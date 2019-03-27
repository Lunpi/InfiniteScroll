package com.example.infinitescroll.repository;

import android.os.AsyncTask;

import com.example.infinitescroll.model.PostDao;

class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {
    private final ContentRepository mRepo;
    private final PostDao mDao;
    RefreshAsyncTask(ContentRepository repo, PostDao dao) {
        mRepo = repo;
        mDao = dao;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        mDao.deleteAll();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mRepo.getContent(true);
    }
}
