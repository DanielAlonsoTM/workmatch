package cl.ponceleiva.workmatch.model;

public class Announce {
    private String announceId, title, image, date;

    public Announce(String announceId, String title, String image, String date) {
        this.announceId = announceId;
        this.title = title;
        this.image = image;
        this.date = date;
    }

    public String getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(String announceId) {
        this.announceId = announceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
