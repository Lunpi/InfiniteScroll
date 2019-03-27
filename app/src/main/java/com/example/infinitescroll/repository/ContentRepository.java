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
        // Check the posts count in database when initializing this repository to get the first set of content ASAP.
        checkDataCount();
    }

    public LiveData<List<RedditPost>> loadRedditPostsFromDb() {
        mPostsInDb = mPostDao.load();
        return mPostsInDb;
    }

    /**
     * Tell the HTTP client to get contents. Right now just get HOTs in Reddit (five contents at a time).
     * The ViewModel may request for refreshing contents or getting new contents.
     * Both cases will eventually reach here.
     * So use a boolean parameter to determine getting or refreshing contents.
     * 
     * If the ViewModel requests for refreshing, just don't set the "count" and the "after" in the URL.
     * Otherwise, need to set the current posts count and the full name of the last post to get the sequential posts.
     * 
     * @param reset To determine refreshing or getting contents (true: refresh, false: get new content)
     */
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
        // Need to re-initialize the HTTP client to avoid "Internal Server Error"
        mRedditClient = new RedditClient();
        new RefreshAsyncTask(this, mPostDao).execute();
    }
}
