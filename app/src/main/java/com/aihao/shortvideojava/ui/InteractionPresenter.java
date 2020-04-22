package com.aihao.shortvideojava.ui;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LifecycleOwner;

import com.aihao.libcommon.extention.LiveDataBus;
import com.aihao.libcommon.global.AppGlobals;
import com.aihao.libnetwork.ApiResponse;
import com.aihao.libnetwork.ApiService;
import com.aihao.libnetwork.JsonCallback;
import com.aihao.shortvideojava.model.Feed;
import com.alibaba.fastjson.JSONObject;

public class InteractionPresenter {
    public static final String DATA_FROM_INTERACTION = "data_from_interaction";

    private static final String URL_TOGGLE_FEED_LIK = "/ugc/toggleFeedLike";

    private static final String URL_TOGGLE_FEED_DISS = "/ugc/dissFeed";

    private static final String URL_SHARE = "/ugc/increaseShareCount";

    private static final String URL_TOGGLE_COMMENT_LIKE = "/ugc/toggleCommentLike";

    //TODO
    private static final  long userId = 1587265663;

    //给一个帖子点赞/取消点赞，它和给帖子点踩一踩是互斥的
    public static void toggleFeedLike(LifecycleOwner owner, Feed feed) {
        toggleFeedLikeInternal(feed);
    }

    private static void toggleFeedLikeInternal(Feed feed) {

        ApiService.get(URL_TOGGLE_FEED_LIK)
                //.addParam("userId", UserManager.get().getUserId())
                .addParam("userId", userId)
                .addParam("itemId", feed.itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasLiked = response.body.getBoolean("hasLiked").booleanValue();
                            feed.getUgc().setHasLiked(hasLiked);
                            LiveDataBus.get().with(DATA_FROM_INTERACTION)
                                    .postValue(feed);
                        }
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        showToast(response.message);
                    }
                });
    }

    //给一个帖子点踩一踩/取消踩一踩,它和给帖子点赞是互斥的
    public static void toggleFeedDiss(LifecycleOwner owner, Feed feed) {
        toggleFeedDissInternal(feed);
    }
    private static void toggleFeedDissInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_DISS)
                //.addParam("userId", UserManager.get().getUserId())
                .addParam("userId", userId)
                .addParam("itemId", feed.itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        if (response.body != null) {
                            boolean hasLiked = response.body.getBoolean("hasLiked").booleanValue();
                            feed.getUgc().setHasdiss(hasLiked);
                        }
                    }

                    @Override
                    public void onError(ApiResponse<JSONObject> response) {
                        showToast(response.message);
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    private static void showToast(String message) {
        ArchTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AppGlobals.getApplication(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
