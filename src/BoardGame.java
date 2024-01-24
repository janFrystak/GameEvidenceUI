public class BoardGame {
    private String name;
    private boolean bought;
    private int rating;

    public BoardGame(String name, boolean bought, int rating) {
        this.name = name;
        this.bought = bought;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
