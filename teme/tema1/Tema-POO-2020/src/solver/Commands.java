package solver;

import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.Map;

public final class Commands {

  /**
   * @param action - action got from input
   * @param users - list of users
   * @param movies - list of movies
   * @param shows - list of shows
   * @return <-- returns a string after processing the commands
   */
  public String command(
      final ActionInputData action,
      final ArrayList<User> users,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows) {
    if (action.getType() != null) {
      //Verific tipul comenzii
      if (action.getType().equals("favorite")) {
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
            //Verific daca videoclipul a fost sau nu vazut
            if (viewed == 1) {
              for (String movie : user.getFavoriteMovies()) {
                if (movie.equals(action.getTitle())) {
                  return "error -> " + action.getTitle() + " is already in favourite list";
                }
              }
              //Adaug videoclipul in lista de favorite
              user.getFavoriteMovies().add(action.getTitle());
              return "success -> " + action.getTitle() + " was added as favourite";
            } else {
              return "error -> " + action.getTitle() + " is not seen";
            }
          }
        }
      }
      if (action.getType().equals("view")) {
        String username = action.getUsername();
        for (User user : users) {
          if (username.equals(user.getUsername())) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
              if (entry.getKey().equals(action.getTitle())) {
                for (Movie movie : movies) {
                  //Cresc numarul de vizionari al unui film
                  if (action.getTitle().equals(movie.getName())) {
                    movie.setNumViews(movie.getNumViews() + 1);
                    break;
                  }
                }
                for (Show show : shows) {
                  if (action.getTitle().equals(show.getTitle())) {
                    show.setNumViews(show.getNumViews() + 1);
                    break;
                  }
                }
                //Incrementrez numarul de vizionari din istoric
                user.getHistory().put(action.getTitle(), entry.getValue() + 1);
                return "success -> "
                    + action.getTitle()
                    + " was viewed with total views of "
                    + entry.getValue();
              }
            }
            for (Movie movie : movies) {
              if (action.getTitle().equals(movie.getName())) {
                movie.setNumViews(movie.getNumViews() + 1);
                break;
              }
            }
            for (Show show : shows) {
              if (action.getTitle().equals(show.getTitle())) {
                show.setNumViews(show.getNumViews() + 1);
                break;
              }
            }
            //Adaug videoclipul in istoric
            user.getHistory().put(action.getTitle(), 1);
            return "success -> " + action.getTitle() + " was viewed with total views of 1";
          }
        }
      }
      if (action.getType().equals("rating")) {
        String username = action.getUsername();
        for (User user : users) {
          if (username.equals(user.getUsername())) {
            //Index va retine pozitia din istoric a videoclipului curent
            int index = 0;
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
              if (entry.getKey().equals(action.getTitle())) {
                user.setRatedIndex(index);
                //Verific daca videoclipul a mai primit rating de la acest utilizator
                if (user.getRated().get(user.getRatedIndex()) == 0) {
                  if (action.getSeasonNumber() == 0) {
                    for (Movie movie : movies) {
                      if (movie.getName().equals(action.getTitle())) {
                        //Notez videoclipul ca rated si returnez rezulatatul
                        user.getRated().set(index, 1);
                        movie.setRating(movie.getRating() + action.getGrade());
                        movie.setNumRatings(movie.getNumRatings() + 1);
                        user.setNumRatings(user.getNumRatings() + 1);
                        return "success -> "
                            + action.getTitle()
                            + " was rated with "
                            + action.getGrade()
                            + " by "
                            + username;
                      }
                    }
                  } else {
                    for (Show show : shows) {
                      if (show.getTitle().equals(action.getTitle())) {
                        show.getRating()
                            .set(
                                action.getSeasonNumber() - 1,
                                show.getRating().get(action.getSeasonNumber() - 1)
                                    + action.getGrade());
                        show.getNumRatings()
                            .set(
                                action.getSeasonNumber() - 1,
                                show.getNumRatings().get(action.getSeasonNumber() - 1) + 1);
                        user.setNumRatings(user.getNumRatings() + 1);
                        return "success -> "
                            + action.getTitle()
                            + " was rated with "
                            + action.getGrade()
                            + " by "
                            + username;
                      }
                    }
                  }
                } else {
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
