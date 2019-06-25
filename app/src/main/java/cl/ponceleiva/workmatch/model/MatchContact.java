package cl.ponceleiva.workmatch.model;

public class MatchContact {
    private String image, name, idUser;

    public MatchContact(String image, String name, String idUser) {
        this.image = image;
        this.name = name;
        this.idUser = idUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
