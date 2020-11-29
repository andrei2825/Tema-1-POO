package entities;

import actor.ActorsAwards;
import fileio.Input;

import java.util.ArrayList;
import java.util.Map;

public final class Actor {
  private final String name;
  private final String careerDescription;
  private final ArrayList<String> filmography;
  private final Map<ActorsAwards, Integer> awards;
  private int numAwards;
  private double avgRating;

  public Actor(final int index, final Input input) {
    this.name = input.getActors().get(index).getName();
    this.careerDescription = input.getActors().get(index).getCareerDescription();
    this.filmography = input.getActors().get(index).getFilmography();
    this.awards = input.getActors().get(index).getAwards();
    this.avgRating = 0;
    this.numAwards = 0;
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

  public void setAvgRating(final double avgRating) {
    this.avgRating = avgRating;
  }

  public int getNumAwards() {
    return numAwards;
  }

  public void setNumAwards(final int numAwards) {
    this.numAwards = numAwards;
  }
}
