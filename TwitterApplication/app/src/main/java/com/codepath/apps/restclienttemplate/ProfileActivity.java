package com.codepath.apps.restclienttemplate;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.fragments.UserTimeLineFragment;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        String origin = getIntent().getStringExtra("origin");

        // create user fragment
        UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);

        // display the fragment inside the container (dynamically)
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer, userTimeLineFragment);

        // commit
        ft.commit();

        //toolbar for the activity profile
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        client = TwitterApp.getRestClient(this);
        if (origin.equals("menu")) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        // deserialize the user object
                        User user = User.fromJSON(response);
                        //set the title on the actionbar based the use screenName
                        getSupportActionBar().setTitle(user.screenName);
                        // Populate the user headline
                        populateUserHeadLine(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        } else if (origin.equals("tweet")) {
            client.getUserProfilInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        // deserialize the user object
                        User user = User.fromJSON(response);
                        //set the title on the actionbar based the use screenName
                        getSupportActionBar().setTitle(user.screenName);
                        // Populate the user headline
                        populateUserHeadLine(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void populateUserHeadLine(User user) {
        TextView tvName = findViewById(R.id.tvName);
        TextView tvTagLine = findViewById(R.id.tvTagLine);
        TextView tvFollowers = findViewById(R.id.tvFollowers);
        TextView tvFollowing = findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);
        tvTagLine.setText(user.tagLine);

            tvFollowers.setText(Html.fromHtml("<b>" + user.followersCount + "</b>" + " FOLLOWERS"));
            tvFollowing.setText(Html.fromHtml("<b>" + user.followingsCount + "</b>" + " FOLLOWING"));

        //use Glide to load profile image
        Glide.with(this)
                .load(user.profileImageURL)
                .bitmapTransform(new RoundedCornersTransformation(this, 5, 0))
                .into(ivProfileImage);

//        TextView view = (TextView)findViewById(R.id.sampleText);
//        String formattedText = "This <i>is</i> a <b>test</b> of <a href='http://foo.com'>html</a>";
//// or getString(R.string.htmlFormattedText);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            view.setText(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            view.setText(Html.fromHtml(formattedText));
//        }
    }
}
