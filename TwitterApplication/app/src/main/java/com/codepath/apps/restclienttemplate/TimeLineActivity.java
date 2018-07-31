package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.fragments.ComposeFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetListFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.io.IOException;

public class TimeLineActivity extends AppCompatActivity implements TweetListFragment.TweetSelectedListener{

    TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        // get the view pager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // set the adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
//        vpPager.setAdapter(tweetsPagerAdapter);
        //setup the Tab Layout to use the viewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        // Find the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        if (!isNetworkAvailable() || isOnline()) {
            Toast.makeText(this, "Please check your internet Connection!!", Toast.LENGTH_SHORT).show();
        }



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }





//    public void fetchTimelineAsync(int page) {
//        client.getHomeTimeline(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                // Remember to CLEAR OUT old items before appending in the new ones
//                tweetAdapter.clear();
//                tweets.clear();
//
//                for (int i = 0; i < response.length(); i++) {
//                    // Convert each object to a tweet model
//                    // Add that tweet model to our data source
//                    // Notify the adapter that we've added an item
//                    try {
//                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
//                        tweets.add(tweet);
//                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//            }
//        });
//    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myCompose:
                Toast.makeText(this, "Compose button click", Toast.LENGTH_SHORT).show();
                showAlertDialog();
                return true;
            case R.id.myProfile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("origin", "menu");
                startActivity(intent);
                return true;
            default:
        return super.onOptionsItemSelected(item);
        }
    }



    private void showAlertDialog() {
        FragmentManager fragment = getSupportFragmentManager();
        ComposeFragment composeFragment = ComposeFragment.newInstance("Some title");
        composeFragment.show(fragment, "fragment_compose");
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
//        Intent intent = new Intent(this, ProfileActivity.class);
//        startActivity(intent);
//        Toast.makeText(this, tweet.body, Toast.LENGTH_SHORT).show();
    }
}
