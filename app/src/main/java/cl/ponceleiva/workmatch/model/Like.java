package cl.ponceleiva.workmatch.model;

public class Like {
    private String name, image, idUser, idAnnounce;

    public Like(String name, String image, String idUser, String idAnnounce) {
        this.name = name;
        this.image = image;
        this.idUser = idUser;
        this.idAnnounce = idAnnounce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdAnnounce() {
        return idAnnounce;
    }

    public void setIdAnnounce(String idAnnounce) {
        this.idAnnounce = idAnnounce;
    }
}
