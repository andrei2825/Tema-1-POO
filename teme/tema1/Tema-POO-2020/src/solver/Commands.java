package solver;

import entities.Movie;
import entities.Show;
import fileio.ActionInputData;
import entities.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Commands {

    public Commands() {

    }
    public String command(ActionInputData action, ArrayList<User> users,
                          ArrayList<Movie> movies, ArrayList<Show> shows) {
        String favorite = "favorite";
        String view = "view";
        String rating = "rating";
        if (action.getType() != null) {
            if (action.getType().equals(favorite)) {
                String username = action.getUsername();
                for (User user : users) {
                    int viewed = 0;
                    if (username.equals(user.getUsername())) {
                        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                            if (entry.getKey().equals(action.getTitle())) {
                                viewed = 1;
                                break;
                            }
                        }
                        if (viewed == 1) {
                            for (String movie : user.getFavoriteMovies()) {
                                if (movie.equals(action.getTitle())) {
                                    return "error -> " + action.getTitle() +  " is already in favourite list";
                                }
                            }

                            user.getHistory().put(action.getTitle(), 1);
                            user.getFavoriteMovies().add(action.getTitle());
                            return "success -> " + action.getTitle() + " was added as favourite";
                        } else {
                            return "error -> " + action.getTitle() + " is not seen";
                        }
                    }
                }
            }
            else if (action.getType().equals(view)) {
                String username = action.getUsername();
                for (User user : users) {
                    if (username.equals(user.getUsername())) {
                        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                            if (entry.getKey().equals(action.getTitle())) {
                                for (Movie movie : movies) {
                                    if (action.getTitle().equals(movie.getName())) {
                                        movie.numViews += 1;
                                        break;
                                    }
                                }
                                for (Show show : shows) {
                                    if (action.getTitle().equals(show.getTitle())) {
                                        show.numViews += 1;
                                        break;
                                    }
                                }
                                entry.setValue(entry.getValue() + 1);
                                return "success -> " + action.getTitle() +
                                        " was viewed with total views of " + entry.getValue();
                            }
                        }
                        for (Movie movie : movies) {
                            if (action.getTitle().equals(movie.getName())) {
                                movie.numViews += 1;
                                break;
                            }
                        }
                        for (Show show : shows) {
                            if (action.getTitle().equals(show.getTitle())) {
                                show.numViews += 1;
                                break;
                            }
                        }
                        user.getHistory().put(action.getTitle(), 1);
                        return "success -> " + action.getTitle() +  " was viewed with total views of 1";

                    }
                }
            }
            else if (action.getType().equals(rating)) {
                String username = action.getUsername();
                for (User user : users) {
                    if (username.equals(user.getUsername())) {
                        int index = 0;
                        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                            if (entry.getKey().equals(action.getTitle())) {
                                user.ratedIndex = index;
                                if (user.getRated().get(user.ratedIndex) == 0) {
                                    if (action.getSeasonNumber() == 0) {
                                        for (Movie movie : movies) {
                                            if (movie.getName().equals(action.getTitle())) {
                                                user.getRated().set(index, 1);
                                                movie.setRating(movie.getRating() + action.getGrade());
                                                movie.setNumRatings(movie.getNumRatings() + 1);
                                                return "success -> " + action.getTitle() + " was rated with " +
                                                        action.getGrade() + " by " + username;
                                            }
                                        }
                                    } else {
                                        for (Show show : shows) {
                                            if (show.getTitle().equals(action.getTitle())) {
                                                    show.getRating().set(action.getSeasonNumber() - 1, action.getGrade());
                                                    show.getNumRatings().set(action.getSeasonNumber() - 1,
                                                            show.getNumRatings().get(action.getSeasonNumber() - 1) + 1);
                                                    return "success -> " + action.getTitle() +
                                                            " was rated with " + action.getGrade() + " by " + username;
                                            }
                                        }
                                    }
                                }
                                else {
                                    return "error -> " + action.getTitle() + " has been already rated";
                                }
                            }
                            index += 1;
                        }
                        return "error -> " + action.getTitle() + " is not seen";
                    }
                }
            }
        }
        return action.getType();
    }
}
