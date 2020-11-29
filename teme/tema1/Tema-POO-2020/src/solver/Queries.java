package solver;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.*;

public class Queries {
    QueryMovies queryMovies;
    QueryActors queryActors;
    QueryShows queryShows;
    QueryUsers queryUsers;
    public Queries() {
        queryMovies = new QueryMovies();
        queryActors = new QueryActors();
        queryShows = new QueryShows();
        queryUsers = new QueryUsers();
    }
    public String  query(ActionInputData action, ArrayList<Actor> actors,
                         ArrayList<Movie> movies, ArrayList<Show> shows, ArrayList<User> users) {


        return switch (action.getObjectType()) {
            case "actors" -> queryActors.queryActors(action, actors, movies, shows);
            case "movies" -> queryMovies.queryMovies(action, movies, users);
            case "shows" -> queryShows.queryShows(action, shows, users);
            case "users" -> queryUsers.queryUsers(action, users);
            default -> null;
        };
    }
}
