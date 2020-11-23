package entities;

import fileio.Input;

import java.util.ArrayList;
import java.util.Map;

public class User {
    private String username;
    private String subscriptionType;
    private ArrayList<String> favoriteMovies;
    private Map<String, Integer> history;

    public User(int index, Input input) {
        this.username = input.getUsers().get(index).getUsername();
        this.subscriptionType = input.getUsers().get(index).getSubscriptionType();
        this.favoriteMovies = input.getUsers().get(index).getFavoriteMovies();
        this.history = input.getUsers().get(index).getHistory();
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
}
