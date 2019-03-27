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

import javax.inject.Singleton;

@Singleton
public class ContentRepository {
    private PostDao mPostDao;
    private LiveData<List<RedditPost>> mPosts;
    private RedditClient mRedditClient;

    public ContentRepository(Application application, RedditClient redditClient) {
        PostDatabase db = PostDatabase.getDatabase(application);
        checkDataCount(db);
        mPostDao = db.postDao();
        mPosts = mPostDao.load();
        mRedditClient = redditClient;
    }

    public LiveData<List<RedditPost>> getPosts() {
        return mPosts;
    }

    private void getPostsFromReddit(boolean reset) {
        final int postCount = (mPosts.getValue() == null || reset) ? 0 : mPosts.getValue().size();
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
        mRedditClient.getPosts(httpResponseHandler, "", postCount, lastPostName);
    }

    public void getContent(boolean reset) {
        new GetContentAsyncTask(this).execute(reset);
    }

    private static class GetContentAsyncTask extends AsyncTask<Boolean, Void, Void> {
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

    public void refreshPosts() {
        mRedditClient = new RedditClient();
        new RefreshAsyncTask(this, mPostDao).execute();
    }

    private static class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {
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
                mRepo.getPostsFromReddit(true);
            }
            return null;
        }
    }
}
