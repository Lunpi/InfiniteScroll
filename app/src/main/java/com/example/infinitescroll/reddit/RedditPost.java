package com.example.infinitescroll.reddit;

import com.example.infinitescroll.ContentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RedditPost {
    private String mTitle;
    private String mThumbnail;
    private String mName;
    private int mLayoutType;
    
    public String getTitle() {
        return mTitle;
    }
    
    public String getThumbnail() {
        return mThumbnail;
    }
    
    public String getName() {
        return mName;
    }
    
    public int getLayoutType() {
        return mLayoutType;
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
                post.mThumbnail = "default";
            }
            post.mLayoutType = ("self".equals(post.mThumbnail) || "default".equals(post.mThumbnail)) ?
                    ContentAdapter.LAYOUT_TITLE_BESIDE_THUMBNAIL :
                    ContentAdapter.LAYOUT_TITLE_OVER_THUMBNAIL;
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
