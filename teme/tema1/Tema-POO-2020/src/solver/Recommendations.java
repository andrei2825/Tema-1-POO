package solver;

import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public final class Recommendations {
  /**
   *
   * @param action - action got from input
   * @param movies - list of movies
   * @param shows - list of shows
   * @param users - list of users
   * @return <-- returns a string after processing the recommendations
   */
  public String recommendation(
      final ActionInputData action,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows,
      final ArrayList<User> users) {
    if (action.getType().equals("standard")) {
      return standardMethod(action, movies, shows, users);
    }

    if (action.getType().equals("best_unseen")) {
      return bestUnseenMethod(action, movies, shows, users);
    }

    if (action.getType().equals("popular")) {
      return popularMethod(action, movies, shows, users);
    }

    if (action.getType().equals("favorite")) {
      return favoriteMethod(action, movies, shows, users);
    }

    if (action.getType().equals("search")) {
      return searchMethod(action, movies, shows, users);
    }

    return null;
  }

  private String standardMethod(
      final ActionInputData action,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows,
      final ArrayList<User> users) {
    for (User user : users) {
      if (user.getUsername().equals(action.getUsername())) {
        for (Movie movie : movies) {
          int coinFlip = 0;
          for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(movie.getName())) {
              coinFlip = 1;
              break;
            }
          }
          if (coinFlip == 0) {
            return "StandardRecommendation result: " + movie.getName();
          }
        }
        for (Show show : shows) {
          int coinFlip = 0;
          for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(show.getTitle())) {
              coinFlip = 1;
              break;
            }
          }
          if (coinFlip == 0) {
            return "StandardRecommendation result: " + show.getTitle();
          }
        }
        return "StandardRecommendation cannot be applied!";
      }
    }
    return null;
  }

  private String bestUnseenMethod(
      final ActionInputData action,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows,
      final ArrayList<User> users) {
    LinkedHashMap<String, Double> videoRatings = new LinkedHashMap<>();
    for (Movie movie : movies) {
      videoRatings.put(movie.getName(), movie.getRating());
    }
    for (Show show : shows) {
      double showRating = 0;
      for (int i = 0; i < show.getNumberOfSeasons(); i++) {
        showRating += show.getRating().get(i);
      }
      showRating /= show.getNumberOfSeasons();
      videoRatings.put(show.getTitle(), showRating);
    }

    Map<String, Double> sorted;
    sorted =
        videoRatings.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .collect(
                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

    for (User user : users) {
      if (user.getUsername().equals(action.getUsername())) {
        for (Map.Entry<String, Double> rating : sorted.entrySet()) {
          int coinFlip = 0;
          for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(rating.getKey())) {
              coinFlip = 1;
              break;
            }
          }
          if (coinFlip == 0) {
            return "BestRatedUnseenRecommendation result: " + rating.getKey();
          }
        }
      }
    }
    return "BestRatedUnseenRecommendation cannot be applied!";
  }

  private String popularMethod(
      final ActionInputData action,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows,
      final ArrayList<User> users) {
    for (User user : users) {
      if (user.getUsername().equals(action.getUsername())) {
        if (user.getSubscriptionType().equals("BASIC")) {
          return "PopularRecommendation cannot be applied!";
        } else if (user.getSubscriptionType().equals("PREMIUM")) {
          LinkedHashMap<String, Integer> genre = new LinkedHashMap<>();
          for (Movie movie : movies) {
            for (String gen : movie.getGenres()) {
              genre.put(gen, 0);
            }
          }
          for (Show show : shows) {
            for (String gen : show.getGenres()) {
              genre.put(gen, 0);
            }
          }
          for (Map.Entry<String, Integer> entry : genre.entrySet()) {
            int views = 0;
            for (Movie movie : movies) {
              for (String gen : movie.getGenres()) {
                if (entry.getKey().equals(gen)) {
                  views += movie.getNumViews();
                }
              }
            }
            for (Show show : shows) {
              for (String gen : show.getGenres()) {
                if (entry.getKey().equals(gen)) {
                  views += show.getNumViews();
                }
              }
            }
            genre.replace(entry.getKey(), views);
          }

          Map<String, Integer> sorted;
          sorted =
              genre.entrySet().stream()
                  .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                  .collect(
                      toMap(
                          Map.Entry::getKey,
                          Map.Entry::getValue,
                          (e1, e2) -> e2,
                          LinkedHashMap::new));

          for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            for (Movie movie : movies) {
              int coinFlip1 = 0;
              for (String gen : movie.getGenres()) {
                if (entry.getKey().equals(gen)) {
                  coinFlip1 = 1;
                  break;
                }
              }
              if (coinFlip1 == 1) {
                int coinFlip2 = 0;
                for (Map.Entry<String, Integer> historyVideo : user.getHistory().entrySet()) {
                  if (movie.getName().equals(historyVideo.getKey())) {
                    coinFlip2 = 1;
                    break;
                  }
                }
                if (coinFlip2 == 0) {
                  return "PopularRecommendation result: " + movie.getName();
                }
              }
            }
            for (Show show : shows) {
              int coinFlip1 = 0;
              for (String gen : show.getGenres()) {
                if (entry.getKey().equals(gen)) {
                  coinFlip1 = 1;
                  break;
                }
              }
              if (coinFlip1 == 1) {
                int coinFlip2 = 0;
                for (Map.Entry<String, Integer> historyVideo : user.getHistory().entrySet()) {
                  if (show.getTitle().equals(historyVideo.getKey())) {
                    coinFlip2 = 1;
                    break;
                  }
                }
                if (coinFlip2 == 0) {
                  return "PopularRecommendation result: " + show.getTitle();
                }
              }
            }
          }
          return "PopularRecommendation cannot be applied!";
        }
      }
    }
    return null;
  }

  private String favoriteMethod(
      final ActionInputData action,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows,
      final ArrayList<User> users) {
    for (User user : users) {
      if (user.getUsername().equals(action.getUsername())) {
        if (user.getSubscriptionType().equals("BASIC")) {
          return "FavoriteRecommendation cannot be applied!";
        } else if (user.getSubscriptionType().equals("PREMIUM")) {
          LinkedHashMap<String, Integer> favorite = new LinkedHashMap<>();
          for (Movie movie : movies) {
            int count = 0;
            for (User user1 : users) {
              for (String fav : user1.getFavoriteMovies()) {
                if (movie.getName().equals(fav)) {
                  count += 1;
                  break;
                }
              }
            }
            if (count > 0) {
              favorite.put(movie.getName(), count);
            }
          }
          for (Show show : shows) {
            int count = 0;
            for (User user1 : users) {
              for (String fav : user1.getFavoriteMovies()) {
                if (show.getTitle().equals(fav)) {
                  count += 1;
                  break;
                }
              }
            }
            if (count > 0) {
              favorite.put(show.getTitle(), count);
            }
          }
          Map<String, Integer> sorted;
          sorted =
              favorite.entrySet().stream()
                  .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                  .collect(
                      toMap(
                          Map.Entry::getKey,
                          Map.Entry::getValue,
                          (e1, e2) -> e2,
                          LinkedHashMap::new));

          for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            int coinFlip = 0;
            for (Map.Entry<String, Integer> historyVideo : user.getHistory().entrySet()) {
              if (entry.getKey().equals(historyVideo.getKey())) {
                coinFlip = 1;
                break;
              }
            }
            if (coinFlip == 0) {
              return "FavoriteRecommendation result: " + entry.getKey();
            }
          }
          return "FavoriteRecommendation cannot be applied!";
        }
      }
    }
    return null;
  }

  private String searchMethod(
      final ActionInputData action,
      final ArrayList<Movie> movies,
      final ArrayList<Show> shows,
      final ArrayList<User> users) {
    for (User user : users) {
      if (user.getUsername().equals(action.getUsername())) {
        if (user.getSubscriptionType().equals("BASIC")) {
          return "SearchRecommendation cannot be applied!";
        } else if (user.getSubscriptionType().equals("PREMIUM")) {
          movies.sort(Comparator.comparing(Movie::getName));
          shows.sort(Comparator.comparing(Show::getTitle));
          LinkedHashMap<String, Double> searched = new LinkedHashMap<>();
          for (Movie movie : movies) {
            for (String gen : movie.getGenres()) {
              if (action.getGenre().toLowerCase().equals(gen.toLowerCase())) {
                searched.put(movie.getName(), movie.getRating());
                break;
              }
            }
          }
          for (Show show : shows) {
            for (String gen : show.getGenres()) {
              if (action.getGenre().toLowerCase().equals(gen.toLowerCase())) {
                double showRating = 0;
                for (int i = 0; i < show.getNumberOfSeasons(); i++) {
                  showRating += show.getRating().get(i);
                }
                showRating /= show.getNumberOfSeasons();
                searched.put(show.getTitle(), showRating);
                break;
              }
            }
          }
          Map<String, Double> sorted;
          sorted =
              searched.entrySet().stream()
                  .sorted(comparingByValue())
                  .collect(
                      toMap(
                          Map.Entry::getKey,
                          Map.Entry::getValue,
                          (e1, e2) -> e2,
                          LinkedHashMap::new));

          StringBuilder searchResults = new StringBuilder();
          searchResults.append("SearchRecommendation result: [");
          int len = 0;
          for (Map.Entry<String, Double> entry : sorted.entrySet()) {
            int coinFip = 0;
            for (Map.Entry<String, Integer> historyVideo : user.getHistory().entrySet()) {
              if (entry.getKey().equals(historyVideo.getKey())) {
                coinFip = 1;
                break;
              }
            }
            if (coinFip == 0) {
              len += 1;
              searchResults.append(entry.getKey());
              searchResults.append(", ");
            }
          }
          if (len == 0) {
            return "SearchRecommendation cannot be applied!";
          }
          searchResults.deleteCharAt(searchResults.length() - 1);
          searchResults.deleteCharAt(searchResults.length() - 1);
          searchResults.append("]");
          return searchResults.toString();
        }
      }
    }
    return null;
  }
}
