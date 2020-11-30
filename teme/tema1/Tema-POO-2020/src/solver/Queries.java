package solver;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.ArrayList;

public final class Queries {
    private final QueryMovies queryMovies;
    private final QueryActors queryActors;
    private final QueryShows queryShows;
    private final QueryUsers queryUsers;

    public Queries() {
        queryMovies = new QueryMovies();
        queryActors = new QueryActors();
        queryShows = new QueryShows();
        queryUsers = new QueryUsers();
    }
    /**
     *
     * @param action - action got from input
     * @param actors - list of actors
     * @param movies - list of movies
     * @param shows - list of shows
     * @param users - list of users
     * @return <-- returns a string after processing the queries
     */
    public String query(
        final ActionInputData action,
        final ArrayList<Actor> actors,
        final ArrayList<Movie> movies,
        final ArrayList<Show> shows,
        final ArrayList<User> users) {
        //Apelez metodele necesare pentru rezolvarea fiecarui tip de query
        return switch (action.getObjectType()) {
            case "actors" -> queryActors.queryActors(action, actors, movies, shows);
            case "movies" -> queryMovies.queryMovies(action, movies, users);
            case "shows" -> queryShows.queryShows(action, shows, users);
            case "users" -> queryUsers.queryUsers(action, users);
            default -> null;
        };
    }
}
