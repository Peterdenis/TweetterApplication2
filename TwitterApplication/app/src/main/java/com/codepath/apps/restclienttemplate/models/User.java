package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.MediaBrowserCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    // List all the attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageURL;

    public String tagLine;
    public int followersCount;
    public int followingsCount;

    public User() {
        super();
    }

    public User (Parcel parcel) {
        this.name = parcel.readString();
        this.uid = parcel.readLong();
        this.screenName = parcel.readString();
        this.profileImageURL = parcel.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    // deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        // extract the values from the json
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageURL = json.getString("profile_image_url");

        user.tagLine = json.getString("description");
        user.followersCount = json.getInt("followers_count");
        user.followingsCount = json.getInt("friends_count");
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageURL);
    }


}
