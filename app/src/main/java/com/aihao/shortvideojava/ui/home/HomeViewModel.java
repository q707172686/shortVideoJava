package com.aihao.shortvideojava.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.aihao.libnetwork.ApiResponse;
import com.aihao.libnetwork.ApiService;
import com.aihao.libnetwork.JsonCallback;
import com.aihao.libnetwork.Request;
import com.aihao.shortvideojava.model.Feed;
import com.aihao.shortvideojava.ui.AbsViewModel;
import com.aihao.shortvideojava.ui.MutableDataSource;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomeViewModel extends AbsViewModel<Feed> {

    private volatile boolean witchCache = true;
    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();
    private AtomicBoolean loadAfter = new AtomicBoolean(false);
    private String mFeedType;
    //TODO
    private static final  long userId = 1587265663;

    @Override
    public DataSource createDataSource() {
        return new  FeedDataSource();
    }

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    class FeedDataSource extends ItemKeyedDataSource<Integer, Feed> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            //加载初始化数据的
            loadData(0, callback);
            witchCache = false;
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //加载分页数据的
            loadData(params.key, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            callback.onResult(Collections.emptyList());
            //是能够向前加载数据的
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    };

    public void setFeedType(String feedType) {

        mFeedType = feedType;
    }

    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (key > 0) {
            loadAfter.set(true);
        }
///feeds/queryHotFeedsList
        Request request = ApiService.get("/feeds/queryHotFeedsList")
                .addParam("feedType", null)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", 10)
                .responseType(new TypeReference<ArrayList<Feed>>() {
                }.getType());

        if (witchCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                   // Log.e("loadData", "onCacheSuccess: "+response.body.size());
                   MutableDataSource dataSource = new MutableDataSource<Integer, Feed>();
                   dataSource.data.addAll(response.body);

                   PagedList pagedList = dataSource.buildNewPagedList(config);
                   cacheLiveData.postValue(pagedList);
                }
            });
        }

        try {
            Request netRequest = witchCache ? request.clone() : request;
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;

            callback.onResult(data);

            if (key > 0) {
                //通过liveData发送数据 告诉UI层 是否应该主动关闭上拉加载分页的动画
              // getBoundaryPageData().postValue(data.size() > 0);
                loadAfter.set(false);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        Log.e("loadData", "loadData: key:" + key);

    }

    @SuppressLint("RestrictedApi")
    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (loadAfter.get()) {
            callback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id, callback);
            }
        });
    }
}