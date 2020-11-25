package entities;

import actor.ActorsAwards;
import fileio.Input;

import java.util.ArrayList;
import java.util.Map;

public class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;
    private double avgRating;

    public Actor(int index, Input input) {
        this.name = input.getActors().get(index).getName();
        this.careerDescription = input.getActors().get(index).getCareerDescription();
        this.filmography = input.getActors().get(index).getFilmography();
        this.awards = input.getActors().get(index).getAwards();
        this.avgRating = 0;
    }
    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }


}
