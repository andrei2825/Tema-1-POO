package entities;

import entertainment.Season;
import fileio.Input;

import java.util.ArrayList;

public class Show {
    private final String title;
    private final ArrayList<String> genres;
    private final int year;
    private final ArrayList<String> cast;
    private final int numberOfSeasons;
    private final ArrayList<Season> season;
    private final ArrayList<Double> rating;
    private final ArrayList<Integer> numRatings;
    public int numViews;
    private int numFavorite;

    public Show(int index, Input input) {
        this.title = input.getSerials().get(index).getTitle();
        this.genres = input.getSerials().get(index).getGenres();
        this.year = input.getSerials().get(index).getYear();
        this.cast = input.getSerials().get(index).getCast();
        this.numberOfSeasons = input.getSerials().get(index).getNumberSeason();
        this.season = input.getSerials().get(index).getSeasons();
        rating = new ArrayList<>();
        for (int i = 0 ; i < season.size(); i++) {
            rating.add((double) 0);
        }
        numRatings = new ArrayList<>();
        for (int i = 0 ; i < season.size(); i++) {
            numRatings.add(0);
        }
        this.numViews = 0;
        this.numFavorite = 0;
    }

    public String getTitle() {
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

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeason() {
        return season;
    }

    public ArrayList<Double> getRating() {
        return rating;
    }

    public ArrayList<Integer> getNumRatings() {
        return numRatings;
    }

    public int getNumFavorite() {
        return numFavorite;
    }

    public void setNumFavorite(int numFavorite) {
        this.numFavorite = numFavorite;
    }
}
