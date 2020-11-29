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


        if (action.getObjectType().equals("actors")) {
            return queryActors.queryActors(action, actors, movies, shows, users);
        }

        if (action.getObjectType().equals("movies")) {
            return queryMovies.queryMovies(action, actors, movies, shows, users);
        }

        if (action.getObjectType().equals("shows")) {
            return  queryShows.queryShows(action, actors, movies, shows, users);
        }

        if (action.getObjectType().equals("users")) {
            return queryUsers.queryUsers(action, actors, movies, shows, users);
        }
        return null;
    }
}
