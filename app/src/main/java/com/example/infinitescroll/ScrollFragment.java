package com.example.infinitescroll;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.infinitescroll.reddit.RedditPost;

import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
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
        mModel.getPosts().observe(this, new Observer<ArrayList<RedditPost>>() {
            @Override
            public void onChanged(@Nullable ArrayList<RedditPost> posts) {
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
                    mModel.getPosts();
                }
            }
        });
        return v;
    }
}
