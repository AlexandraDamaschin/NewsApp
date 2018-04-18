package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {
    //variables needed
    //log tag
    private static final String LOG_TAG = NewsFeedLoader.class.getName();
    private String mUrl;

    //Constructor
    public NewsFeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    //on start loading
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //This is on a background thread.
    @Override
    public List<NewsFeed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<NewsFeed> newsFeeds = QueryUtils.fetchNewsFeedData(mUrl);
        return newsFeeds;
    }

}
