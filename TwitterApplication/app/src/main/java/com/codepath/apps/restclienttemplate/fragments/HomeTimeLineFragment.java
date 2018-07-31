package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeTimeLineFragment extends TweetListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient(getContext());
        populateTimeLine();
    }

    public static HomeTimeLineFragment newInstance(Tweet tweet) {
        HomeTimeLineFragment fragmentTimeline = new HomeTimeLineFragment();
        Bundle args = new Bundle();
        args.putParcelable("tweet", tweet);
        fragmentTimeline.setArguments(args);
//        fragmentTimeline.tweets.add(tweet);
//        fragmentTimeline.addItems(getTweets());
        return fragmentTimeline;
    }


    public void populateTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               tweetAdapter.clear();
               addItems(response);
               HomeTimeLineFragment.super.swipeContainer.setRefreshing(false);
               HomeTimeLineFragment.super.scrollListener.resetState();

            }

//            private boolean isNetworkAvailable() {
//                ConnectivityManager connectivityManager
//                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }

    // Loading the data on scroll indefinitely
    public void loadNextDataFromApi(int offset) {
        onExtendTimeline("home");
    }
}
