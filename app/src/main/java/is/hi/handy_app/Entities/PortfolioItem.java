package is.hi.handy_app.Entities;

import com.google.gson.annotations.SerializedName;

public class PortfolioItem {

    @SerializedName("id")
    private Long mID;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("image")
    private Image mImage;
    @SerializedName("stringImage")
    private String mStringImage;

    private HandyUser user;

    public PortfolioItem() {
    }

    public PortfolioItem(String title, String description, String location, HandyUser handyman) {
        mTitle = title;
        mDescription = description;
        mLocation = location;
        this.user = handyman;
    }

    public Long getID() {
        return mID;
    }

    public void setID(Long ID) {
        this.mID = ID;
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

    public HandyUser getUser() {
        return user;
    }

    public void setUser(HandyUser user) {
        this.user = user;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
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
}

