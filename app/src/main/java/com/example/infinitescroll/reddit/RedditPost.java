package com.example.infinitescroll.reddit;

import com.example.infinitescroll.ContentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reddit_post_table")
public class RedditPost {
    @PrimaryKey
    @NonNull
    private String mId = "";
    private String mTitle;
    private String mThumbnail;
    private String mName;
    private int mLayoutType;
    
    public String getId() {
        return mId;
    }
    
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
    
    public void setId(String id) {
        mId = id;
    }
    
    public void setTitle(String title) {
        mTitle = title;
    }
    
    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }
    
    public void setName(String name) {
        mName = name;
    }
    
    public void setLayoutType(int layoutType) {
        mLayoutType = layoutType;
    }
    
    public static RedditPost parseJson(JSONObject json) {
        RedditPost post = new RedditPost();
        try {
            post.mId = json.getString("id");
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
    
    public static List<RedditPost> parseJson(JSONArray jsonArray) {
        if (jsonArray == null) return null;
        List<RedditPost> posts = new ArrayList<RedditPost>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json;
            try {
                json = jsonArray.getJSONObject(i).getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            RedditPost post = RedditPost.parseJson(json);
            posts.add(post);
        }
        return posts;
    }
}
