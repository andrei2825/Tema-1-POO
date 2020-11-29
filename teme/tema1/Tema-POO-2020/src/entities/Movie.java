package entities;

import fileio.Input;

import java.util.ArrayList;

public final class Movie {
  private final String title;
  private final ArrayList<String> genres;
  private final int year;
  private final ArrayList<String> cast;
  private final int duration;
  private int numViews;
  private double rating;
  private int numRatings;
  private int numFavorite;

  public Movie(final int index, final Input input) {
    this.title = input.getMovies().get(index).getTitle();
    this.genres = input.getMovies().get(index).getGenres();
    this.year = input.getMovies().get(index).getYear();
    this.cast = input.getMovies().get(index).getCast();
    this.duration = input.getMovies().get(index).getDuration();
    this.numViews = 0;
    this.numRatings = 0;
    this.numFavorite = 0;
  }

  public String getName() {
    return title;
  }

  public ArrayList<String> getGenres() {
    return genres;
  }

  public int getYear() {
    return year;
  }

  public ArrayList<String> getCast() {
    return cast;
  }

  public int getDuration() {
    return duration;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(final double rating) {
    this.rating = rating;
  }

  public int getNumRatings() {
    return numRatings;
  }

  public void setNumRatings(final int numRatings) {
    this.numRatings = numRatings;
  }

  public int getNumFavorite() {
    return numFavorite;
  }

  public void setNumFavorite(final int numFavorite) {
    this.numFavorite = numFavorite;
  }

  public int getNumViews() {
    return numViews;
  }

  public void setNumViews(final int numViews) {
    this.numViews = numViews;
  }
}
