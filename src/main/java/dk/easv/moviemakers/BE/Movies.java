package dk.easv.moviemakers.BE;

public class Movies {
    private int id;
    private String title;
    private int year;
    private String category;
    private float rating;
    private float personalrating;
    private String filelink;
    //private int time;



    public Movies(int id, String title, int year, String category, float rating, float personalrating){
        this.id = id;
        this.title = title;
        this.year = year;
        this.category = category;
        this.rating = rating;
        this.personalrating = personalrating;
        this.filelink = filelink;
        //this.time = time;


    }
    //Getters and setters
    public int getid() {return this.id;}

    public void setid(int value) {this.id = value;}

    public String gettitle() {return this.title;}

    public void settitle(String value) {this.title = value;}

    public int getyear() {return this.year;}

    public void setyear(int value) {this.year = value;}

    public String getcategory() {return this.category;}

    public void setcategory(String value) {this.category = value;}

    public float getrating() {return this.rating;}

    public void setrating(float value) {this.rating = value;}

    public float getpersonalrating() {return this.personalrating;}

    public void setpersonalrating(float value) {this.personalrating = value;}

    public String getfilelink() {return this.filelink;}

    public void setfilelink(String value) {this.filelink = value;}

    //public int getTime() {return this.time;}

    //public void setTime(int value) {this.time = value;}

}
