package cl.ponceleiva.workmatch.model;

public class Card {

    public String title, image, announceId;

    public Card(String title, String image, String announceId) {
        this.title = title;
        this.image = image;
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

    public String getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(String announceId) {
        this.announceId = announceId;
    }
}
