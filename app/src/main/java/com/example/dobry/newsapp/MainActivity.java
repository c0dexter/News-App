package com.example.dobry.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
    ListView newsListView;

    boolean isConnected;
    /**
     * URL for newses data from the Guardian API
     */
    private String mUrlRequestGuardianApi = "";
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * Circle progress bar
     */
    private View circleProgressBar;
    /**
     * Adapter for the list of newses
     */
    private NewsAdapter mAdapter;
    /**
     * Search field
     */
    private SearchView mSearchViewField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declaration and initialization ConnectivityManager for checking internet connection
        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);


        /**
         * At the beginning check the connection with internet and save result to (boolean) variable isConnected
         * Checking if network is available
         * If TRUE - work with LoaderManager
         * If FALSE - hide loading spinner and show emptyStateTextView
         */
        checkConnection(cm);

        // Find a reference to the {@link ListView} in the layout
        newsListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of newses as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Find a reference to the empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Circle progress
        circleProgressBar = findViewById(R.id.loading_spinner);

        // Search button
        Button mSearchButton = (Button) findViewById(R.id.search_button);

        // Search field
        mSearchViewField = (SearchView) findViewById(R.id.search_view_field);
        mSearchViewField.onActionViewExpanded();
        mSearchViewField.setIconified(true);
        mSearchViewField.setQueryHint("Enter a phrase");


        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Progress bar mapping
            Log.i(LOG_TAG, "INTERNET connection status: " + String.valueOf(isConnected) + ". Sorry dude, no internet - no data :(");


            circleProgressBar.setVisibility(GONE);
            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


        // Set an item click listener on the Search Button, which sends a request to
        // Guardian API based on value from Search View
        mSearchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                // Check connection status
                checkConnection(cm);

                if (isConnected) {
                    // Update URL and restart loader to displaying new result of searching
                    updateQueryUrl(mSearchViewField.getQuery().toString());
                    restartLoader();
                    Log.i(LOG_TAG, "Search value: " + mSearchViewField.getQuery().toString());
                } else {
                    // Clear the adapter of previous newses data
                    mAdapter.clear();
                    // Set mEmptyStateTextView visible
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    // ...and display message: "No internet connection."
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }

            }

        });

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Check value of newsUrl and convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews != null ? currentNews.getNewsUrl() : null);
                Log.i(LOG_TAG, "News URI value: " + newsUri);

                // Create a new intent to view buy the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    /**
     * Check if query contains spaces if YES replace these with PLUS sign
     *
     * @param searchValue - user data from SearchView
     * @return improved String URL for making HTTP request
     */
    private String updateQueryUrl(String searchValue) {

        if (searchValue.contains(" ")) {
            searchValue = searchValue.replace(" ", "+");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://http://content.guardianapis.com/search?q=").append(searchValue).append("&order-by=newest&order-date=published&show-section=true&show-fields=headline,thumbnail&show-references=author&show-tags=contributor&page=1&page-size=10&api-key=test");
        mUrlRequestGuardianApi = sb.toString();
        return mUrlRequestGuardianApi;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i("There is no instance", ": Created new one loader at the beginning!");
        // Create a new loader for the given URL
        updateQueryUrl(mSearchViewField.getQuery().toString());
        return new NewsLoader(this, mUrlRequestGuardianApi);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {

        // Progress bar mapping
        View circleProgressBar = findViewById(R.id.loading_spinner);
        circleProgressBar.setVisibility(GONE);

        // Set empty state text to display "No newses found."
        mEmptyStateTextView.setText(R.string.no_newses);
        Log.i(LOG_TAG, ": Newses has been moved to adapter's data set. This will trigger the ListView to update!");

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newses != null && !newses.isEmpty()) {
            mAdapter.addAll(newses);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.i(LOG_TAG, ": Loader reset, so we can clear out our existing data!");
    }

    public void restartLoader() {
        mEmptyStateTextView.setVisibility(GONE);
        circleProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
    }

    public void checkConnection(ConnectivityManager connectivityManager) {
        // Status of internet connection
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            isConnected = true;

            Log.i(LOG_TAG, "INTERNET connection status: " + String.valueOf(isConnected) + ". It's time to play with LoaderManager :)");

        } else {
            isConnected = false;

        }
    }

}
