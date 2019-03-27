package com.example.infinitescroll;

import android.content.Context;

import com.example.infinitescroll.reddit.RedditPost;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RedditPost.class}, version = 2)
public abstract class PostDatabase extends RoomDatabase {
    public abstract PostDao postDao();

    private static volatile PostDatabase sInstance;

    static PostDatabase getDatabase(final Context context) {
        if (sInstance == null) {
            synchronized (PostDatabase.class) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), PostDatabase.class, "post_database")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }
}
