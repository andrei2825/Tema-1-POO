package solver;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.Comparator;

public class Queries {
    public Queries() {

    }
    public String  query(ActionInputData action, ArrayList<Actor> actors,
                         ArrayList<Movie> movies, ArrayList<Show> shows, ArrayList<User> users) {
        if (action.getObjectType().equals("actors")) {
            if (action.getCriteria().equals("average")){
                actors.sort(Comparator.comparing(Actor::getName));
                for(Actor actor : actors) {
                    double avg = 0;
                    int num = 0;
                    for (int i = 0; i < actor.getFilmography().size(); i++) {
                        for (Movie movie : movies) {
                            if (movie.getName().equals(actor.getFilmography().get(i))) {
                                if (movie.getRating() != 0) {
                                    num += 1;
                                    if (movie.getNumRatings() != 0){
                                        avg += (movie.getRating()/movie.getNumRatings());
                                    }
                                }
                                break;
                            }
                        }
                        for (Show show : shows) {
                            if (show.getTitle().equals(actor.getFilmography().get(i))) {
                                double avgSeasons = 0;
                                for (int j = 0; j < show.getNumberOfSeasons(); j++) {
                                    if (show.getNumRatings().get(j) != 0){
                                        avgSeasons += show.getRating().get(j)/show.getNumRatings().get(j);
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
                    }
                    else {
                        actor.setAvgRating(avg / num);
                    }


                }
                if (action.getSortType().equals("asc")) {
                    actors.sort(Comparator.comparing(Actor::getAvgRating));
                    StringBuilder actorList = new StringBuilder();
                    actorList.append("Query result: [");
                    int count = action.getNumber();
                    for (Actor actor : actors) {
                        if (actor.getAvgRating() > 0){
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
                else {

                }
            }
        }
        return null;
    }
}
