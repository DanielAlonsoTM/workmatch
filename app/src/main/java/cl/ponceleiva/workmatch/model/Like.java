package cl.ponceleiva.workmatch.model;

public class Like {
    private String name, image, idProfessional, idAnnounce;

    public Like(String name, String image, String idProfessional, String idAnnounce) {
        this.name = name;
        this.image = image;
        this.idProfessional = idProfessional;
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

    public String getIdProfessional() {
        return idProfessional;
    }

    public void setIdProfessional(String idProfessional) {
        this.idProfessional = idProfessional;
    }

    public String getIdAnnounce() {
        return idAnnounce;
    }

    public void setIdAnnounce(String idAnnounce) {
        this.idAnnounce = idAnnounce;
    }
}
