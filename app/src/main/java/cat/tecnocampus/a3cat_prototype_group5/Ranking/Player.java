package cat.tecnocampus.a3cat_prototype_group5.Ranking;

public class Player {
    private String name,surname;
    private int score;

    public Player(String name, int score, String surname) {
        this.name = name;
        this.surname = surname;
        this.score = score;
    }

    public String getName() {
        return name+" "+surname;
    }

    public int getScore() {
        return score;
    }
}