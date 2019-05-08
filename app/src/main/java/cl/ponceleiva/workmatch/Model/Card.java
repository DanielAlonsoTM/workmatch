package cl.ponceleiva.workmatch.Model;

public class Card {

    public String title, image, userId;

    public Card(String title, String image, String userId) {
        this.title = title;
        this.image = image;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
