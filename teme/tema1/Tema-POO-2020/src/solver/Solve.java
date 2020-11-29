package solver;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.ArrayList;

public class Solve {
    Commands commands = new Commands();
    Queries queries = new Queries();
    Recommendations recommendations = new Recommendations();
    public String solve(ActionInputData action, ArrayList<User> users,
                          ArrayList<Movie> movies, ArrayList<Show> shows, ArrayList<Actor> actors) {
        return switch (action.getActionType()) {
            case "command" -> commands.command(action, users, movies, shows);
            case "query" -> queries.query(action, actors, movies, shows, users);
            case "recommendation" -> recommendations.recommendation(action, movies, shows, users);
            default -> action.getActionType();
        };
    }
}
