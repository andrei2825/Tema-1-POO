package solver;

import actor.ActorsAwards;
import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.*;

public class Queries {
    public Queries() {

    }
    public String  query(ActionInputData action, ArrayList<Actor> actors,
                         ArrayList<Movie> movies, ArrayList<Show> shows, ArrayList<User> users) {
        if (action.getObjectType().equals("actors")) {
            if (action.getCriteria().equals("average")) {
                actors.sort(Comparator.comparing(Actor::getName));
                for (Actor actor : actors) {
                    double avg = 0;
                    int num = 0;
                    for (int i = 0; i < actor.getFilmography().size(); i++) {
                        for (Movie movie : movies) {
                            if (movie.getName().equals(actor.getFilmography().get(i))) {
                                if (movie.getRating() != 0) {
                                    num += 1;
                                    if (movie.getNumRatings() != 0) {
                                        avg += (movie.getRating() / movie.getNumRatings());
                                    }
                                }
                                break;
                            }
                        }
                        for (Show show : shows) {
                            if (show.getTitle().equals(actor.getFilmography().get(i))) {
                                double avgSeasons = 0;
                                for (int j = 0; j < show.getNumberOfSeasons(); j++) {
                                    if (show.getNumRatings().get(j) != 0) {
                                        avgSeasons += show.getRating().get(j) / show.getNumRatings().get(j);
                                    }
                                }
                                if (show.getNumberOfSeasons() != 0) {
                                    avgSeasons = avgSeasons / show.getNumberOfSeasons();
                                }
                                if (avgSeasons != 0) {
                                    avg += avgSeasons;
                                    num += 1;
                                }
                            }
                        }
                    }
                    if (num == 0) {
                        actor.setAvgRating(0);
                    } else {
                        actor.setAvgRating(avg / num);
                    }


                }
                if (action.getSortType().equals("asc")) {
                    actors.sort(Comparator.comparing(Actor::getAvgRating));
                    StringBuilder actorList = new StringBuilder();
                    actorList.append("Query result: [");
                    int count = action.getNumber();
                    for (Actor actor : actors) {
                        if (actor.getAvgRating() > 0) {
                            actorList.append(actor.getName());
                            actorList.append(", ");
                            count -= 1;
                        }
                        if (count == 0) {
                            break;
                        }
                    }
                    actorList.deleteCharAt(actorList.length() - 1);
                    actorList.deleteCharAt(actorList.length() - 1);
                    actorList.append("]");
                    return actorList.toString();
                } else if (action.getSortType().equals("desc")) {
                    actors.sort(Comparator.comparing(Actor::getAvgRating).reversed());
                    StringBuilder actorList = new StringBuilder();
                    actorList.append("Query result: [");
                    int count = action.getNumber();
                    for (Actor actor : actors) {
                        if (actor.getAvgRating() > 0) {
                            actorList.append(actor.getName());
                            actorList.append(", ");
                            count -= 1;
                        }
                        if (count == 0) {
                            break;
                        }
                    }
                    actorList.deleteCharAt(actorList.length() - 1);
                    actorList.deleteCharAt(actorList.length() - 1);
                    actorList.append("]");
                    return actorList.toString();
                }
            }

            if (action.getCriteria().equals("awards")) {
                actors.sort(Comparator.comparing(Actor::getName));
                StringBuilder actorList = new StringBuilder();
                actorList.append("Query result: [");
                int actorListLen = 0;
                for (Actor actor : actors) {
                    for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                        actor.setNumAwards(actor.getNumAwards() + entry.getValue());
                    }
                }
                if (action.getSortType().equals("asc")) {
                    actors.sort(Comparator.comparing(Actor::getNumAwards));
                } else if (action.getSortType().equals("desc")) {
                    actors.sort(Comparator.comparing(Actor::getNumAwards).reversed());
                }
                for (Actor actor : actors) {
                    if (actor.getAwards().size() < action.getFilters().get(3).size()) {
                        continue;
                    }
                    int check = 0;
                    for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                        for (String award : action.getFilters().get(3)) {
                            if (award.equals(entry.getKey().toString())) {
                                check += 1;
                            }
                        }
                    }
                    if (check == action.getFilters().get(3).size()) {
                        actorListLen += 1;
                        actorList.append(actor.getName());
                        actorList.append(", ");
                    }
                }
                if (actorListLen == 0) {
                    actorList.append("]");
                    return actorList.toString();
                }
                actorList.deleteCharAt(actorList.length() - 1);
                actorList.deleteCharAt(actorList.length() - 1);
                actorList.append("]");
                return actorList.toString();
            }

            if (action.getCriteria().equals("filter_description")) {
                actors.sort(Comparator.comparing(Actor::getName));
                StringBuilder actorList = new StringBuilder();
                actorList.append("Query result: [");
                int actorListLen = 0;
                for (Actor actor : actors) {
                    int check = 0;
                    for (String word : action.getFilters().get(2)) {
                        if (actor.getCareerDescription().toLowerCase().contains(word.toLowerCase())) {
                            check += 1;
                        }
                    }
                    if (check == action.getFilters().get(2).size()) {
                        actorListLen += 1;
                        actorList.append(actor.getName());
                        actorList.append(", ");
                    }
                }
                if (actorListLen == 0) {
                    actorList.append("]");
                    return actorList.toString();
                }
                actorList.deleteCharAt(actorList.length() - 1);
                actorList.deleteCharAt(actorList.length() - 1);
                actorList.append("]");
                return actorList.toString();
            }
        }

        if (action.getObjectType().equals("movies")) {
            int count = action.getNumber();
            if (action.getCriteria().equals("ratings")) {
                Map<Double, String> videoRating = new HashMap<Double, String>();
                for (Movie movie : movies) {
                    videoRating.put(movie.getRating(), movie.getName());
                }
                Map<Double, String> treeMap;
                if (action.getSortType().equals("asc")) {
                    treeMap = new TreeMap<Double, String>(videoRating);
                } else {
                    treeMap = new TreeMap<Double, String>(
                            new Comparator<Double>() {
                                @Override
                                public int compare(Double aDouble, Double t1) {
                                    return t1.compareTo(aDouble);
                                }
                            }
                        );
                    treeMap.putAll(videoRating);
                }
                StringBuilder ratedVideos = new StringBuilder();
                int ratedVideosLen = 0;
                ratedVideos.append("Query result: [");
                for (Map.Entry<Double, String> rating : treeMap.entrySet()) {
                    if (count == 0) {
                        break;
                    }
                    for (Movie movie : movies) {
                        if (movie.getName().equals(rating.getValue())) {
                            if (movie.getGenres().equals(action.getFilters().get(1).get(0))) {
                                if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                                    ratedVideosLen += 1;
                                    ratedVideos.append(rating.getValue());
                                    ratedVideos.append(", ");
                                    count -= 1;
                                }
                            }
                        }
                    }
                }
                if (ratedVideosLen == 0) {
                    ratedVideos.append("]");
                    return ratedVideos.toString();
                }
                ratedVideos.deleteCharAt(ratedVideos.length() - 1);
                ratedVideos.deleteCharAt(ratedVideos.length() - 1);
                ratedVideos.append("]");
                return ratedVideos.toString();
            }

            //TODO continue from here
        }
        if (action.getObjectType().equals("shows")) {
            int count = action.getNumber();
            Map<Double, String> videoRating = new HashMap<Double, String>();
            for (Show show : shows) {
                double rate = 0;
                for (int i = 0; i < show.getNumberOfSeasons(); i++) {
                    rate += show.getRating().get(i);
                }
                videoRating.put(rate / show.getNumberOfSeasons(), show.getTitle());
            }
            Map<Double, String> treeMap;
            if (action.getSortType().equals("asc")) {
                treeMap = new TreeMap<Double, String>(videoRating);
            } else {
                treeMap = new TreeMap<Double, String>(
                        new Comparator<Double>() {
                            @Override
                            public int compare(Double aDouble, Double t1) {
                                return t1.compareTo(aDouble);
                            }
                        }
                );
                treeMap.putAll(videoRating);
            }
            StringBuilder ratedVideos = new StringBuilder();
            int ratedVideosLen = 0;
            ratedVideos.append("Query result: [");
            for (Map.Entry<Double, String> rating : treeMap.entrySet()) {
                if (count == 0) {
                    break;
                }
                for (Show show : shows) {

                    if (show.getTitle().equals(rating.getValue())) {
                        if (show.getGenres().toString().equals(action.getFilters().get(1).get(0))) {
                            if (String.valueOf(show.getYear()).equals(action.getFilters().get(0).get(0))) {
                                ratedVideosLen += 1;
                                ratedVideos.append(rating.getValue());
                                ratedVideos.append(", ");
                                count -= 1;
                            }
                        }
                    }
                }
            }
            if (ratedVideosLen == 0) {
                ratedVideos.append("]");
                return ratedVideos.toString();
            }
            ratedVideos.deleteCharAt(ratedVideos.length() - 1);
            ratedVideos.deleteCharAt(ratedVideos.length() - 1);
            ratedVideos.append("]");
            return ratedVideos.toString();
        }


        return null;
    }
}
