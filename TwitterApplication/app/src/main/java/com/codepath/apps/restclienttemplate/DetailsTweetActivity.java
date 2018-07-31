package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailsTweetActivity extends AppCompatActivity {

    Context context;
    Tweet tweet;
    private TwitterClient client;
    ImageView imgDetailsProfileImage;
    TextView tvDetailsTweetUsername;
    TextView tvDetailsTweetName;
    TextView tvDetailsTweetTime;
    TextView tvDetailsTweetBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tweet);
        client = TwitterApp.getRestClient(this);

        // Find the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        imgDetailsProfileImage = findViewById(R.id.imgDetailsProfileImage);
        tvDetailsTweetUsername = findViewById(R.id.tvDetailsTweetUsername);
        tvDetailsTweetName = findViewById(R.id.tvDetailsTweetName);
        tvDetailsTweetTime = findViewById(R.id.tvDetailsTweetTime);
        tvDetailsTweetBody = findViewById(R.id.tvDetailsTweetBody);

        Long tweetId = getIntent().getLongExtra("tweet_id", 0);

        if (tweetId != 0) {
            client.getInfoTweet(tweetId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        tweet = Tweet.fromJSON(response);
                        onTweetLoad();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("TwitterClient", response.toString());
                }

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
        } else {
            Log.e("TweetDetailActivity", "Unable to obtain tweet ID");

        }


//        onTweetLoad();
    }

    public void onTweetLoad(){
        if (tweet != null) {
            tvDetailsTweetBody.setText(tweet.body);
            tvDetailsTweetTime.setText(formatTime(tweet.createdAt));
            tvDetailsTweetName.setText(tweet.user.screenName);
            tvDetailsTweetUsername.setText(tweet.user.name);

            Glide.with(this)
                    .load(tweet.user.profileImageURL)
                    .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                    .into(imgDetailsProfileImage);

//            tvTweetBody.setText(tweet.body);
//            tvUsername.setText(tweet.user.name);
//            tvScreenName.setText(tweet.user.screenName);
//            tvTimeStamp.setText(formatTime(tweet.createdAt));


        } else {
            Toast.makeText(this, "Tweet is not null" + "The tweet Values: "
                    + tweet.body + " " +tweet.createdAt + " " + tweet.user.screenName
                    + " " + tweet.user.name, Toast.LENGTH_LONG).show();
        }
    }


    public String formatTime(String twitterTime) {
        DateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        DateFormat targetFormat = new SimpleDateFormat("h:mm a \u2022 dd MMM yy", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(twitterTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);  // 20120821
        return formattedDate;
    }


}
