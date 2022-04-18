package is.hi.handy_app.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HandyUser extends User implements Serializable {
    private Trade trade;
    private double hourlyRate;
    private double averageRating;
    private List<PortfolioItem> portfolioItem = new ArrayList<>();

    private List<Review> reviewsAbout = new ArrayList<>();
    // todo messages


    public HandyUser() {
    }

    public HandyUser(long id, String name, String email, Trade trade, double hourlyRate, double averageRating, List<PortfolioItem> portfolioItem, List<Review> reviewsAbout) {
        super(id, name, email);
        this.trade = trade;
        this.hourlyRate = hourlyRate;
        this.averageRating = averageRating;
        this.portfolioItem = portfolioItem;
        this.reviewsAbout = reviewsAbout;
    }
    public long getID(){return id;}

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public List<PortfolioItem> getPortfolioItem() { return portfolioItem; }

    public void setPortfolioItem(List<PortfolioItem> portfolioItem) { this.portfolioItem = portfolioItem; }

    public List<Review> getReviewsAbout() {
        return reviewsAbout;
    }

    public void setReviewsAbout(List<Review> reviewsAbout) {
        this.reviewsAbout = reviewsAbout;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return "HandyUser{" +
                "trade='" + trade + '\'' +
                ", hourlyRate=" + hourlyRate +
                ", name=" + name +
                ", email=" + email +
                '}';
    }
}
