package com.example.infinitescroll;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import static androidx.room.OnConflictStrategy.REPLACE;

import com.example.infinitescroll.reddit.RedditPost;

import java.util.List;

@Dao
public interface PostDao {
    @Insert(onConflict = REPLACE)
    void insert(RedditPost post);
    @Query("SELECT * FROM reddit_post_table")
    LiveData<List<RedditPost>> load();
    @Query("SELECT COUNT(*) FROM reddit_post_table")
    int checkDataCount();
}
