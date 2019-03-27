package com.example.infinitescroll.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.infinitescroll.model.PostDao;
import com.example.infinitescroll.model.PostDatabase;
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
    private LiveData<List<RedditPost>> mPostsInDb;
    private RedditClient mRedditClient;

    public ContentRepository(Application application, RedditClient redditClient) {
        mRedditClient = redditClient;
        PostDatabase db = PostDatabase.getDatabase(application);
        mPostDao = db.postDao();
        checkDataCount();
    }

    public LiveData<List<RedditPost>> getRedditPosts() {
        mPostsInDb = mPostDao.load();
        return mPostsInDb;
    }

    void getPostsFromReddit(boolean reset) {
        final int postCount = (mPostsInDb.getValue() == null || reset) ?
                0 : mPostsInDb.getValue().size();
        String lastPostName = (postCount > 0) ?
                mPostsInDb.getValue().get(postCount-1).getName() : "";

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

    private void checkDataCount() {
        new CheckDataCountAsyncTask(this, mPostDao).execute();
    }

    public void getContent(boolean reset) {
        new GetContentAsyncTask(this).execute(reset);
    }

    public void refreshContent() {
        mRedditClient = new RedditClient();
        new RefreshAsyncTask(this, mPostDao).execute();
    }
}
