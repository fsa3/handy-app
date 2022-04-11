package is.hi.handy_app.Entities;

import java.util.List;
import java.util.ArrayList;


public class User {
    protected long ID;
    protected String name;
    protected String email;
    protected String info;
    protected String password;

    private List<Ad> advertisements = new ArrayList<>();

    public User(){
    }

    public User(long id, String name) {
        this.ID = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Ad> getAds() {
        return advertisements;
    }

    public void setAds(List<Ad> ads) {
        this.advertisements = ads;
    }

    public long getID() {
        return ID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
}
