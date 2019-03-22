package com.example.infinitescroll.reddit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditPost {
    public String mTitle;
    public String mThumbnail;
    public String mName;
    
    public String getTitle() {
        return mTitle;
    }
    
    public String getThumbnail() {
        return mThumbnail;
    }
    
    public String getName() {
        return mName;
    }
    
    public static RedditPost parseJson(JSONObject json) {
        RedditPost post = new RedditPost();
        try {
            post.mTitle = json.getString("title");
            post.mName = json.getString("name");
            // Not every post has a thumbnail
            try {
                post.mThumbnail = json.getString("thumbnail");
            } catch (JSONException e) {
                post.mThumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return post;
    }
    
    public static ArrayList<RedditPost> parseJson(JSONArray jsonArray) {
        if (jsonArray == null) return null;
        ArrayList<RedditPost> posts = new ArrayList<RedditPost>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json;
            try {
                json = jsonArray.getJSONObject(i).getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            RedditPost post = RedditPost.parseJson(json);
            if (post != null) {
                posts.add(post);
            }
        }
        return posts;
    }
}
