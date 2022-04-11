package is.hi.handy_app.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Ad implements Comparable<Ad>, Serializable {
    @SerializedName("ID")
    private long mID;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("image")
    private Image mImage;
    @SerializedName("stringImage")
    private String mStringImage;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("timePosted")
    private Timestamp mTimePosted;
    @SerializedName("formattedDate")
    private String mFormattedDate;
    @SerializedName("trade")
    private Trade mTrade;

    private User user;

    public Ad(long ID, String title, String description, Image image, String stringImage, String location, Timestamp timePosted, String formattedDate, Trade trade, User user) {
        mID = ID;
        mTitle = title;
        mDescription = description;
        mImage = image;
        mStringImage = stringImage;
        mLocation = location;
        mTimePosted = timePosted;
        mFormattedDate = formattedDate;
        mTrade = trade;
        this.user = user;
    }

    public Ad() {
        mTimePosted = new Timestamp(System.currentTimeMillis());
        mFormattedDate = new SimpleDateFormat("dd.MM.yyyy").format(mTimePosted);
    }

    public Ad(String title, String description) {
        this.mTitle = title;
        this.mDescription = description;
        mTimePosted = new Timestamp(System.currentTimeMillis());
        mFormattedDate = new SimpleDateFormat("dd.MM.yyyy").format(mTimePosted);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Timestamp getTimePosted() {
        return mTimePosted;
    }

    public void setTimePosted(Timestamp timePosted) {
        this.mTimePosted = timePosted;
    }

    public Trade getTrade() {
        return mTrade;
    }

    public void setTrade(Trade trade) {
        this.mTrade = trade;
    }

    public long getID() {
        return mID;
    }

    public void setID(Long ID) {
        this.mID = ID;
    }

    public Image getImage() {
        return mImage;
    }

    public void setImage(Image image) {
        this.mImage = image;
    }

    public String getStringImage() {
        return mStringImage;
    }

    public void setStringImage(String stringImage) {
        this.mStringImage = stringImage;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public String getFormattedDate() {
        return mFormattedDate;
    }

    @Override
    public int compareTo(Ad o) {
        return -this.mTimePosted.compareTo(o.mTimePosted);
    }
}
