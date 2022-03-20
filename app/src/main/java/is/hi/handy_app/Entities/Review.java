package is.hi.handy_app.Entities;

import java.sql.Timestamp;

public class Review {
    private long ID;

    private String text;
    private int rating;
    private Timestamp timePosted;
    private User author;
    private HandyUser handyman;

    public Review() {
    }

    public Review(String text, int rating, User author, HandyUser handyman) {
        this.text = text;
        this.rating = rating;
        this.author = author;
        this.handyman = handyman;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(Timestamp timePosted) {
        this.timePosted = timePosted;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public HandyUser getHandyman() {
        return handyman;
    }

    public void setHandyman(HandyUser handyman) {
        this.handyman = handyman;
    }
}
