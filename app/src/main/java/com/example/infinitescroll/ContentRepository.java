package com.example.infinitescroll;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.infinitescroll.reddit.RedditClient;
import com.example.infinitescroll.reddit.RedditPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

class ContentRepository {
    private PostDao mPostDao;
    private LiveData<List<RedditPost>> mPosts;
    public ContentRepository(Application application) {
        PostDatabase db = PostDatabase.getDatabase(application);
        checkDataCount(db);
        mPostDao = db.postDao();
        mPosts = mPostDao.load();
    }
    
    public LiveData<List<RedditPost>> getPosts() {
        return mPosts;
    }
    
    private void fetchContent() {
        RedditClient client = new RedditClient();
        final int postCount = (mPosts.getValue() == null || mPosts.getValue().isEmpty()) ? 0 : mPosts.getValue().size();
        String lastPostName = (postCount > 0) ? mPosts.getValue().get(postCount-1).getName() : "";
        JsonHttpResponseHandler httpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                try {
                    JSONObject data = responseBody.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");
                    for (RedditPost post : RedditPost.parseJson(children)) {
                        mPostDao.insert(post);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        };
        client.getPosts(httpResponseHandler, "", postCount, lastPostName);
    }
    
    public void fetchNewContent() {
        new FetchAsyncTask(this).execute();
    }
    
    private static class FetchAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ContentRepository mRepo;
        FetchAsyncTask(ContentRepository repo) {
            mRepo = repo;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mRepo.fetchContent();
            return null;
        }
    }

    private void checkDataCount(PostDatabase db) {
        new CheckDataCountAsync(this, db).execute();
    }
    
    private static class CheckDataCountAsync extends AsyncTask<Void, Void, Void> {
        private final PostDao mDao;
        private final ContentRepository mRepo;
        CheckDataCountAsync(ContentRepository repo, PostDatabase db) {
            mRepo = repo;
            mDao = db.postDao();
        }
        @Override
        protected Void doInBackground(final Void... params) {
            if (mDao.checkDataCount() == 0) {
                mRepo.fetchContent();
            }
            return null;
        }
    }
}
