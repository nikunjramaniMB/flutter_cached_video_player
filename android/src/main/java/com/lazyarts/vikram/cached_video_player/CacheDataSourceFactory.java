package com.lazyarts.vikram.cached_video_player;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.util.Map;

class CacheDataSourceFactory implements DataSource.Factory {
    private final Context context;
    private DefaultDataSource.Factory defaultDatasourceFactory;
    private final long maxFileSize, maxCacheSize;
    private DefaultHttpDataSource.Factory defaultHttpDataSourceFactory;
    CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize) {
        super();
        this.context = context;
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        defaultHttpDataSourceFactory = new DefaultHttpDataSource.Factory();
    }
    void setHeaders(Map<String, String> httpHeaders){
        defaultHttpDataSourceFactory.setDefaultRequestProperties(httpHeaders);
    }
    @Override
    public DataSource createDataSource() {
        defaultDatasourceFactory = new DefaultDataSource.Factory(this.context,defaultHttpDataSourceFactory);
        SimpleCache simpleCache = SimpleCacheSingleton.getInstance(context, maxCacheSize).simpleCache;
        return new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(),
                new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }

}
