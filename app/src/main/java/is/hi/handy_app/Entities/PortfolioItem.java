package is.hi.handy_app.Entities;

public class PortfolioItem {

    private Long ID;
    private String title;
    private String description;
    private String location;
    private Image image;
    private String stringImage;

    private HandyUser user;

    public PortfolioItem() {
    }

    public PortfolioItem(String title, String description, String location, HandyUser handyman) {
        this.title = title;
        this.description = description;
        this.user = handyman;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HandyUser getUser() {
        return user;
    }

    public void setUser(HandyUser user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getStringImage() {
        return stringImage;
    }

    public void setStringImage(String stringImage) {
        this.stringImage = stringImage;
    }
}

