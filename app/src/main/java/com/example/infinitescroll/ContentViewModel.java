package com.example.infinitescroll;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.infinitescroll.reddit.RedditClient;
import com.example.infinitescroll.reddit.RedditPost;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ContentViewModel extends ViewModel {
    private MutableLiveData<ArrayList<RedditPost>> mPosts = new MutableLiveData<ArrayList<RedditPost>>();

    MutableLiveData<ArrayList<RedditPost>> getPosts() {
        fetchPosts();
        return mPosts;
    }

    private void fetchPosts() {
        RedditClient client = new RedditClient();
        final int postCount = mPosts.getValue() == null ? 0 : mPosts.getValue().size();
        String lastPostName = mPosts.getValue() == null ? "" : mPosts.getValue().get(postCount-1).getName();
        JsonHttpResponseHandler httpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                try {
                    JSONObject json = responseBody.getJSONObject("data");
                    JSONArray posts = json.getJSONArray("children");
                    ArrayList<RedditPost> currentPosts = mPosts.getValue() == null ? new ArrayList<RedditPost>() : mPosts.getValue();
                    currentPosts.addAll(RedditPost.parseJson(posts));
                    mPosts.setValue(currentPosts);
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
}
