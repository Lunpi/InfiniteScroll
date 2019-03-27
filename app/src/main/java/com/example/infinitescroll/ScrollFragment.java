package com.example.infinitescroll;

import androidx.lifecycle.Observer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.infinitescroll.viewModel.DaggerViewModelComponent;
import com.example.infinitescroll.viewModel.ViewModelModule;
import com.example.infinitescroll.reddit.RedditPost;
import com.example.infinitescroll.viewModel.ContentViewModel;

import java.util.List;

import javax.inject.Inject;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.GONE;

public class ScrollFragment extends Fragment {
    private ProgressBar mLoadingCenter;
    private ProgressBar mLoading;
    private ContentAdapter mContentAdapter;
    @Inject
    ContentViewModel mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentAdapter = new ContentAdapter(getContext());
        DaggerViewModelComponent.builder()
                .viewModelModule(new ViewModelModule(getActivity().getApplication(), this))
                .build()
                .inject(this);
        mModel.getLiveDataPosts().observe(this, new Observer<List<RedditPost>>() {
            @Override
            public void onChanged(List<RedditPost> posts) {
                if (posts == null) return;
                // Only update UI in the following conditions:
                // 1. Contents are already cached and loaded into UI, and then users scroll up to refresh.
                // 2. When the ViewModel gets new set of contents.
                if (posts.isEmpty()) {
                    mLoadingCenter.setVisibility(View.VISIBLE);
                    if (mContentAdapter.getItemCount() > 0) {
                        mContentAdapter.setPosts(posts);
                        mContentAdapter.notifyDataSetChanged();
                    }
                } else {
                    hideProgressBar();
                    mContentAdapter.setPosts(posts);
                    mContentAdapter.notifyItemRangeInserted(mContentAdapter.getItemCount(), posts.size());
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scroll, container, false);
        mLoadingCenter = v.findViewById(R.id.loading_center);
        mLoading = v.findViewById(R.id.loading_bottom);

        RecyclerView content = v.findViewById(R.id.content);
        content.setLayoutManager(new LinearLayoutManager(getContext()));
        content.setAdapter(mContentAdapter);
        content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                boolean isIdle = (newState == SCROLL_STATE_IDLE
                        && mLoadingCenter.getVisibility() == GONE
                        && mLoading.getVisibility() == GONE);
                // Inform ViewModel to request for content when the following conditions meet:
                // 1. Users reach the top/bottom of the list.
                // 2. User's finger leaves the screen.
                // 3. Finish loading contents.
                if (!recyclerView.canScrollVertically(1)
                        && isIdle) {
                    mLoading.setVisibility(View.VISIBLE);
                    mModel.getContent();
                } else if (!recyclerView.canScrollVertically(-1)
                        && isIdle) {
                    mLoadingCenter.setVisibility(View.VISIBLE);
                    mModel.refreshContent();
                }
            }
        });
        return v;
    }

    private void hideProgressBar() {
        if (mLoadingCenter != null && mLoadingCenter.getVisibility() != GONE) {
            mLoadingCenter.setVisibility(GONE);
        }
        if (mLoading != null && mLoading.getVisibility() != GONE) {
            mLoading.setVisibility(GONE);
        }
    }
}
