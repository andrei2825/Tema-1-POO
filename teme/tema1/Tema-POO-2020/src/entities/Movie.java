package entities;

import fileio.Input;

import java.util.ArrayList;

public class Movie {
    private String title;
    private ArrayList<String> genres;
    private int year;
    private ArrayList<String> cast;
    private int duration;
    private int rating;

    public Movie(int index, Input input) {
        this.title = input.getMovies().get(index).getTitle();
        this.genres = input.getMovies().get(index).getGenres();
        this.year = input.getMovies().get(index).getYear();
        this.cast = input.getMovies().get(index).getCast();
        this.duration = input.getMovies().get(index).getDuration();
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

    public int getRating() {
        return rating;
    }
}
