package com.example.infinitescroll;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.infinitescroll.reddit.RedditPost;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.GONE;

public class ScrollFragment extends Fragment {
    private ProgressBar mLoading;
    private ContentAdapter mContentAdapter;
    private ContentViewModel mModel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentAdapter = new ContentAdapter(getContext());
        mModel = ViewModelProviders.of(this).get(ContentViewModel.class);
        mModel.getPosts().observe(this, new Observer<List<RedditPost>>() {
            @Override
            public void onChanged(@Nullable List<RedditPost> posts) {
                if (posts == null) return;
                mLoading.setVisibility(View.GONE);
                mContentAdapter.setPosts(posts);
                mContentAdapter.notifyItemRangeInserted(mContentAdapter.getItemCount(), posts.size());
            }
        });
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scroll, container, false);
        mLoading = v.findViewById(R.id.loading);
        
        RecyclerView content = v.findViewById(R.id.content);
        content.setLayoutManager(new LinearLayoutManager(getContext()));
        content.setAdapter(mContentAdapter);
        content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)
                        && newState == SCROLL_STATE_IDLE
                        && mLoading.getVisibility() == GONE) {
                    mLoading.setVisibility(View.VISIBLE);
                    mModel.fetchPosts();
                }
            }
        });
        return v;
    }
}
