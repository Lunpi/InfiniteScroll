package com.example.infinitescroll.reddit;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RedditClient {
    private final String REDDIT_URL = "http://www.reddit.com/";
    private AsyncHttpClient mClient;
    
    public RedditClient() {
        mClient = new AsyncHttpClient();
    }
    
    private String generateUrl(String subreddit, int currentPostsCount, String lastPostName) {
        String subredditUrl = subreddit.isEmpty() ? "" : "r/" + subreddit;
        String count = currentPostsCount == 0 ? "" : "&count=" + currentPostsCount;
        String after = "".equals(lastPostName) ? "" : "&after=" + lastPostName;
        return REDDIT_URL + subredditUrl + ".json?limit=5" + count + after;
    }

    public void getPosts(JsonHttpResponseHandler handler, String subreddit, int currentPostsCount, String lastPostName) {
        mClient.get(generateUrl(subreddit, currentPostsCount, lastPostName), handler);
    }
}
