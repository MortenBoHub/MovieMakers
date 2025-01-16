package dk.easv.moviemakers.BE;

import java.sql.Timestamp;

public class Movies {
    //Instance fields
    private int id;
    private String title;
    private int year;
    private String categories;
    private float rating;
    private float personalrating;
    private String filelink;
    private Timestamp lastview;
    private String address;

    //Constructor
    public Movies(int id, String title, int year, String categories, float rating, float personalrating, String filelink, Timestamp lastview, String address) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.categories = categories;
        this.rating = rating;
        this.personalrating = personalrating;
        this.filelink = filelink;
        this.lastview = lastview;
        this.address = address;

    }

    //Getters and setters
    public int getId() {
        return this.id;
    }
    public void setId(int value) {
        this.id = value;
    }


    public String getTitle() {
        return this.title;
    }
    public void setTitle(String value) {
        this.title = value;
    }


    public int getYear() {
        return this.year;
    }
    public void setYear(int value) {
        this.year = value;
    }


    public String getCategory() {
        return this.categories;
    }
    public void setCategory(String value) {
        this.categories = value;
    }


    public float getRating() {
        return this.rating;
    }
    public void setRating(float value) {
        this.rating = value;
    }


    public float getPersonalrating() {
        return this.personalrating;
    }
    public void setPersonalrating(float value) {
        this.personalrating = value;
    }


    public String getFilelink() {
        return this.filelink;
    }
    public void setFilelink(String value) {
        this.filelink = value;
    }


    public Timestamp getLastview() {
        return this.lastview;
    }
    public void setLastview(Timestamp lastview) {
        this.lastview = lastview;
    }


    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}