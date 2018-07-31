package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.DetailsTweetActivity;
import com.codepath.apps.restclienttemplate.ProfileActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;
    // define an interface required by the viewHolder
    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);
    }
    // pass in the tweet array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    // for each row inflate the layout and cache references in the viewholder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    // bind the value based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data according to the position
        Tweet tweet = mTweets.get(position);

        // populate the views according ti this data
        holder.tvUserName.setText(tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvName.setText(tweet.user.name);
        holder.tvDate.setText(getRelativeTimeAgo(tweet.createdAt));

        //shorten timestamp
        String shortTime = shortenRelativeTime((String) holder.tvDate.getText());
        holder.tvDate.setText(shortTime);

        Glide.with(context).load(tweet.user.profileImageURL).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create viewholder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvName;
        public TextView tvDate;
        public RelativeLayout itemTweets;
        public TextView tvBody;

        public ViewHolder (View convertView) {
            super(convertView);

            // perform findViewId lookups
            ivProfileImage = convertView.findViewById(R.id.ivProfileImage);
            tvUserName = convertView.findViewById(R.id.tvUserName);
            tvDate = convertView.findViewById(R.id.tvRelativeDate);
            itemTweets = convertView.findViewById(R.id.itemTweets);
            tvBody = convertView.findViewById(R.id.tvBody);
            tvName = convertView.findViewById(R.id.tvName);

            //handle row click event
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null ) {
//                        // get the position of this row element
//                        int position = getAdapterPosition();
//                        // fire the listener callback
//                        mListener.onItemSelected(v, position);
//                    }
//                }
//            });
            ivProfileImage.setOnClickListener(this);
            itemTweets.setOnClickListener(this);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            Tweet tweet = mTweets.get(position);
            switch (v.getId()){
                case R.id.ivProfileImage:
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("screen_name", tweet.user.screenName);
                    intent.putExtra("origin", "tweet");
                    context.startActivity(intent);
                break;
                default:
                    Intent i = new Intent(context, DetailsTweetActivity.class);
                    i.putExtra("tweet_id", tweet.uid);
                    context.startActivity(i);
            }
        }
    }

    public String shortenRelativeTime(String timestamp) {
        String[] splitTime = timestamp.trim().split("\\s+");
        List<String> times = Arrays.asList("second", "seconds", "minute", "minutes", "hour", "hours", "day", "days", "week", "weeks");
        // deal with recent tweets of form "# _ ago"
        if (times.contains(splitTime[1])) {
            timestamp = splitTime[0] + splitTime[1].charAt(0);
        }
        // deal with old tweets of form M D, Y
        else if (splitTime[2].equals("2017")) {
            timestamp = splitTime[0] + " " + splitTime[1].substring(0, splitTime[1].length() -1);
        }
        return timestamp;
    }




    // displayed the relative timestamp for each tweet

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all the elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
