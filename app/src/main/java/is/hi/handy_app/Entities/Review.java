package is.hi.handy_app.Entities;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Review {

    @SerializedName("id")
    private long mID;
    @SerializedName("text")
    private String mText;
    @SerializedName("rating")
    private int mRating;
    @SerializedName("timeposted")
    private Timestamp mTimePosted;
    @SerializedName("author")
    private User mAuthor;
    @SerializedName("handyman")
    private HandyUser mHandyman;

    public Review() {
    }

    public Review(String text, int rating, User author, HandyUser handyman) {
        this.mText = text;
        this.mRating = rating;
        this.mAuthor = author;
        this.mHandyman = handyman;
    }

    public long getID(){return mID;}

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        this.mRating = rating;
    }

    public Timestamp getTimePosted() {
        return mTimePosted;
    }

    public void setTimePosted(Timestamp timePosted) {
        this.mTimePosted = timePosted;
    }

    public User getAuthor() {
        return mAuthor;
    }

    public void setAuthor(User author) {
        this.mAuthor = author;
    }

    public HandyUser getHandyman() {
        return mHandyman;
    }

    public void setHandyman(HandyUser handyman) {
        this.mHandyman = handyman;
    }
}
