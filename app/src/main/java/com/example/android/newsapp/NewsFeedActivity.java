package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsFeed>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    //variables needed
    private static final String LOG_TAG = NewsFeedActivity.class.getName();
    // URL for news data from the guardian dataset
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?&show-tags=contributor&q=debates&api-key=24eedf15-b45e-4956-82a8-41bf7b52ade7";
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

        //get json data
        ListView newsFeedListView = (ListView) findViewById(R.id.list);

        //empty list
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsFeedListView.setEmptyView(mEmptyStateTextView);

        //implement adapter
        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsFeedListView.setAdapter(mAdapter);

        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        //set an event on click listener
        //on click on a news open details in new window
        newsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current newsfeed that was clicked on
                NewsFeed currentNewsFeed = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsFeedUri = Uri.parse(currentNewsFeed.getUrl());

                // Create a new intent to view the newsFeed URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsFeedUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }

        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    //settings button
    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //This method passes the MenuItem that is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //determine which item was selected and what action to take
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_default_key)) ||
                key.equals(getString(R.string.settings_order_by_key))) {
            // Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the guardian as the query settings have been updated
            getLoaderManager().restartLoader(NEWSFEED_LOADER_ID, null, this);
        }
    }

    //on create loader
    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_default_key));
        //order by
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default_value)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("q", topic);
        //not to have to old news
        uriBuilder.appendQueryParameter("from-date", "2018-01-01");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", orderBy);

        return new NewsFeedLoader(this, uriBuilder.toString());
    }

    //on load finish
    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsFeeds) {
        //hide loading indicator when data is ready
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        //set text for no news founded
        mEmptyStateTextView.setText(R.string.no_news);
        // Clear the adapter of previous newsFeed data
        mAdapter.clear();
        // If there is a valid list of {@link NewsFeed}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsFeeds != null && !newsFeeds.isEmpty()) {
            mAdapter.addAll(newsFeeds);
        }
    }

    //on loader reset
    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
