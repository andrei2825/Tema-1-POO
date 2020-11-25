package entities;

import fileio.Input;

import java.util.ArrayList;

public class Movie {
    private String title;
    private ArrayList<String> genres;
    private int year;
    private ArrayList<String> cast;
    private int duration;
    private double rating;
    public int numViews;
    private int numRatings;

    public Movie(int index, Input input) {
        this.title = input.getMovies().get(index).getTitle();
        this.genres = input.getMovies().get(index).getGenres();
        this.year = input.getMovies().get(index).getYear();
        this.cast = input.getMovies().get(index).getCast();
        this.duration = input.getMovies().get(index).getDuration();
        this.numViews = 0;
        this.numRatings = 0;

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

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }
}
