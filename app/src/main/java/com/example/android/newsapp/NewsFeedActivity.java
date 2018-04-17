package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeed>> {
    //variables needed
    private static final String LOG_TAG = NewsFeedActivity.class.getName();
    // URL for news data from the guardian dataset
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=24eedf15-b45e-4956-82a8-41bf7b52ade7";
    //adapter
    private NewsFeedAdapter mAdapter;
    //constant value for the loader id
    private static final int NEWSFEED_LOADER_ID = 1;
    //text view for empty list
    private TextView mEmptyStateTextView;


    //on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_activity);
    }

    //on create loader
    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int i, Bundle bundle) {
    }

    //on load finish
    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> earthquakes) {

    }

    //on loader reset
    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
    }
}
