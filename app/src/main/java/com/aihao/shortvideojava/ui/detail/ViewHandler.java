package com.aihao.shortvideojava.ui.detail;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aihao.libcommon.utils.PixUtils;
import com.aihao.libcommon.view.EmptyView;
import com.aihao.shortvideojava.R;
import com.aihao.shortvideojava.databinding.LayoutFeedDetailBottomInateractionBinding;
import com.aihao.shortvideojava.model.Comment;
import com.aihao.shortvideojava.model.Feed;

public abstract class ViewHandler {
    private final FeedDetailViewModel viewModel;
    protected FragmentActivity mActivity;
    protected Feed mFeed;
    protected RecyclerView mRecyclerView;
    protected LayoutFeedDetailBottomInateractionBinding mInateractionBinding;
    protected  FeedCommentAdapter listAdapter;
    private CommentDialog commentDialog;


    public ViewHandler(FragmentActivity activity) {
        mActivity = activity;
        viewModel = ViewModelProviders.of(activity).get(FeedDetailViewModel.class);
    }

    @CallSuper
    public  void bindInitData(Feed feed) {
        mInateractionBinding.setOwner(mActivity);
        mFeed = feed;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(null);
        listAdapter = new FeedCommentAdapter(mActivity);
        mRecyclerView.setAdapter(listAdapter);
        viewModel.setItemId(mFeed.itemId);

        viewModel.getPageData().observe(mActivity, new Observer<PagedList<Comment>>() {
            @Override
            public void onChanged(PagedList<Comment> comments) {
                listAdapter.submitList(comments);
                handleEmpty(comments.size() > 0);
            }
        });
        mInateractionBinding.inputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });
    }

    private void showCommentDialog () {
        if (commentDialog == null) {
            commentDialog = CommentDialog.newInstance(mFeed.itemId);
        }
        commentDialog.setCommentAddListener(comment -> {
            handleEmpty(true);
            listAdapter.addAndRefreshList(comment);
        });
        commentDialog.show(mActivity.getSupportFragmentManager(), "comment_dialog");
    }

    private EmptyView mEmptyView;
    public  void handleEmpty(boolean hasData) {
        if (hasData) {
            if (mEmptyView != null) {
                listAdapter.removeHeaderView(mEmptyView);
            }
        } else {
            if (mEmptyView == null) {
                mEmptyView = new EmptyView(mActivity);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = PixUtils.dp2px(40);
                mEmptyView.setLayoutParams(layoutParams);
                mEmptyView.setTitle(mActivity.getString(R.string.feed_comment_empty));
            }
            listAdapter.addHeaderView(mEmptyView);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (commentDialog != null && commentDialog.isAdded()) {
            commentDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onBackPressed() {

    }
}
