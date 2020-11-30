package solver;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.ArrayList;

public final class Solve {
    private final Commands commands = new Commands();
    private final Queries queries = new Queries();
    private final Recommendations recommendations = new Recommendations();

    /**
     *
     * @param action - action got from input
     * @param actors - list of actors
     * @param movies - list of movies
     * @param shows - list of shows
     * @param users - list of users
     * @return <-- returns a string after processing the action
     */

    //In aceasta metoda se stabileste tipul comenzii primite si apeleaza o metoda dintr-o clasa
    //corespunzatoare

    public String solve(
        final ActionInputData action,
        final ArrayList<User> users,
        final ArrayList<Movie> movies,
        final ArrayList<Show> shows,
        final ArrayList<Actor> actors) {
        //Switch ce apelewaza metodele necesare rezolvarii
        return switch (action.getActionType()) {
            case "command" -> commands.command(action, users, movies, shows);
            case "query" -> queries.query(action, actors, movies, shows, users);
            case "recommendation" -> recommendations.recommendation(action, movies, shows, users);
            default -> action.getActionType();
        };
    }
}
