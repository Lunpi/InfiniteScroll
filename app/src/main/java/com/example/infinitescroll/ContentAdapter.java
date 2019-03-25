package com.example.infinitescroll;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infinitescroll.reddit.RedditPost;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {
    
    public static final int LAYOUT_TITLE_OVER_THUMBNAIL = 0;
    public static final int LAYOUT_TITLE_BESIDE_THUMBNAIL = 1;
    private List<RedditPost> mPosts;
    private Context mContext;
    
    class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private ImageView mThumbnail;
        private ContentViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.title);
            mThumbnail = v.findViewById(R.id.thumbnail);
        }
    }
    
    public ContentAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return mPosts.get(position).getLayoutType();
    }
    
    @Override
    public @NonNull
    ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResource = viewType == LAYOUT_TITLE_OVER_THUMBNAIL ?
                R.layout.title_over_thumbnail : R.layout.title_image_apart;
        View v = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);
        return new ContentViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder vh, int position) {
        vh.mTitle.setText(mPosts.get(position).getTitle());
        String thumbnail = mPosts.get(position).getThumbnail();
        if ("self".equals(thumbnail) || "default".equals(thumbnail)) {
            vh.mThumbnail.setImageResource(R.drawable.snoo);
        } else {
            Picasso.with(mContext).load(thumbnail).into(vh.mThumbnail);
        }
    }
    
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
    
    public void setPosts(List<RedditPost> posts) {
        mPosts = posts;
    }
}
