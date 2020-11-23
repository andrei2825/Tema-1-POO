package entities;

import entertainment.Season;
import fileio.Input;

import java.util.ArrayList;
import java.util.Map;

public class Show {
    private String title;
    private ArrayList<String> genres;
    private int year;
    private ArrayList<String> cast;
    private int numberOfSeasons;
    private ArrayList<Season> season;

    public Show(int index, Input input) {
        this.title = input.getSerials().get(index).getTitle();
        this.genres = input.getSerials().get(index).getGenres();
        this.year = input.getSerials().get(index).getYear();
        this.cast = input.getSerials().get(index).getCast();
        this.numberOfSeasons = input.getSerials().get(index).getNumberSeason();
        this.season = input.getSerials().get(index).getSeasons();
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
}
