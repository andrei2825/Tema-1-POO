package entities;

import fileio.Input;

import java.util.ArrayList;
import java.util.Map;

public final class User {
  private final String username;
  private final String subscriptionType;
  private final ArrayList<String> favoriteMovies;
  private final Map<String, Integer> history;
  private final ArrayList<Integer> rated;
  private int ratedIndex;
  private int numRatings;
  private String prevUser;

  public User(final int index, final Input input) {
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
    this.prevUser = null;
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

  public String getPrevUser() {
    return prevUser;
  }

  public void setPrevUser(final String prevUser) {
    this.prevUser = prevUser;
  }

  public int getRatedIndex() {
    return ratedIndex;
  }

  public void setRatedIndex(final int ratedIndex) {
    this.ratedIndex = ratedIndex;
  }

  public int getNumRatings() {
    return numRatings;
  }

  public void setNumRatings(final int numRatings) {
    this.numRatings = numRatings;
  }
}
