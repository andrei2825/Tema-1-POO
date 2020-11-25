package entities;

import fileio.Input;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String subscriptionType;
    private ArrayList<String> favoriteMovies;
    private Map<String, Integer> history;
    private ArrayList<Integer> rated;
    public int ratedIndex;
    public int numRatings;
    public int prevSeasonRated;

    public User(int index, Input input) {
        this.username = input.getUsers().get(index).getUsername();
        this.subscriptionType = input.getUsers().get(index).getSubscriptionType();
        this.favoriteMovies = input.getUsers().get(index).getFavoriteMovies();
        this.history = input.getUsers().get(index).getHistory();
        this.numRatings = 0;
        this.rated = new ArrayList<>();
        for (int i = 0; i < history.size() + 1; i++) {
            rated.add(0);
        }
        this.ratedIndex = 0;
        this.prevSeasonRated = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<Integer> getRated() {
        return rated;
    }
}
