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
        if (action.getActionType().equals("command")) {
            return commands.command(action, users, movies, shows);
        }
        else if (action.getActionType().equals("query")) {
            return queries.query(action, actors, movies, shows, users);
        }
        else if (action.getActionType().equals("recommendation")) {
            return recommendations.recommendation(action, actors, movies, shows, users);
        }
        return action.getActionType();
    }
}
