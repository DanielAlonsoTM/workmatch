package cl.ponceleiva.workmatch.model;

public class MatchContact {
    private String image, name, matchId;

    public MatchContact(String image, String name, String matchId) {
        this.image = image;
        this.name = name;
        this.matchId = matchId;
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

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }
}
