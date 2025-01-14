package dk.easv.moviemakers.BE;

import java.sql.Timestamp;

public class Movies {
    private int id;
    private String title;
    private int year;
    private String categories;
    private float rating;
    private float personalrating;
    private String filelink;
    private Timestamp lastview;
    private String address;
    //private int time;



    public Movies(int id, String title, int year, String categories, float rating, float personalrating, String filelink, Timestamp lastview, String address){
        this.id = id;
        this.title = title;
        this.year = year;
        this.categories = categories;
        this.rating = rating;
        this.personalrating = personalrating;
        this.filelink = filelink;
        this.lastview = lastview;
        this.address = address;
        //this.time = time;

    }

    //Getters and setters
    public int getid() {return this.id;}
    public void setid(int value) {this.id = value;}

    public String gettitle() {return this.title;}
    public void settitle(String value) {this.title = value;}

    public int getyear() {return this.year;}
    public void setyear(int value) {this.year = value;}

    public String getcategory() {return this.categories;}
    public void setcategory(String value) {this.categories = value;}

    public float getrating() {return this.rating;}
    public void setrating(float value) {this.rating = value;}

    public float getpersonalrating() {return this.personalrating;}
    public void setpersonalrating(float value) {this.personalrating = value;}

    public String getfilelink() {return this.filelink;}
    public void setfilelink(String value) {this.filelink = value;}

    public Timestamp getLastview() {return this.lastview;}
    public void setLastview(Timestamp value) {this.lastview = value;}

    public String getAddress() {return address; }
    public void setAddress(String address) {this.address = address; }

    //public int getTime() {return this.time;}

    //public void setTime(int value) {this.time = value;}

}
