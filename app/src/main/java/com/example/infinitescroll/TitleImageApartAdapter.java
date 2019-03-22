package com.example.infinitescroll;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infinitescroll.reddit.RedditPost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TitleImageApartAdapter extends RecyclerView.Adapter<TitleImageApartAdapter.TitleImageApartViewHolder> {

    private ArrayList<RedditPost> mPosts = new ArrayList<RedditPost>();
    private Context mContext;
    
    public class TitleImageApartViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mThumbnail;
        public TitleImageApartViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.title);
            mThumbnail = v.findViewById(R.id.thumbnail);
        }
    }
    
    public TitleImageApartAdapter(Context context) {
        mContext = context;
    }
    
    @Override
    public @NonNull TitleImageApartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.title_image_apart, parent, false);
        return new TitleImageApartViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TitleImageApartViewHolder vh, int position) {
        vh.mTitle.setText(mPosts.get(position).getTitle());
        Picasso.with(mContext).load(mPosts.get(position).getThumbnail()).into(vh.mThumbnail);
    }
    
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
    
    
    public void setPosts(ArrayList<RedditPost> posts) {
        mPosts = posts;
    }
}
