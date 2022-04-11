package is.hi.handy_app.Entities;

import java.io.Serializable;

public class Image implements Serializable {
    private long id;

    private byte[] image;

    private PortfolioItem portfolioItem;

    private Ad ad;

    public Image() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Image(byte[] image) {
        super();
        this.image = image;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    public PortfolioItem getPortfolioItem() {
        return portfolioItem;
    }

    public void setPortfolioItem(PortfolioItem portfolioItem) {
        this.portfolioItem = portfolioItem;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
}