package solver;

import entities.Movie;
import entities.User;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public final class QueryMovies {
    /**
     *
     * @param action - action got from input
     * @param movies - list of movies
     * @param users - list of users
     * @return <-- returns a string after processing the queries for movies
     */
    public String queryMovies(
        final ActionInputData action,
        final ArrayList<Movie> movies,
        final ArrayList<User> users) {

        if (action.getSortType().equals("asc")) {
            movies.sort(Comparator.comparing(Movie::getName));
        } else {
            movies.sort(Comparator.comparing(Movie::getName).reversed());
        }

        return switch (action.getCriteria()) {
            case "ratings" -> ratingMethod(action, movies);
            case "favorite" -> favoriteMethod(action, movies, users);
            case "longest" -> longestMethod(action, movies);
            case "most_viewed" -> mostViewedMethod(action, movies, users);
            default -> null;
        };
    }

    private String ratingMethod(
        final ActionInputData action,
        final ArrayList<Movie> movies) {
        int count = action.getNumber();
        //Creez un map in care pun toate filmele
        Map<Double, String> videoRating = new HashMap<>();
        for (Movie movie : movies) {
            videoRating.put(movie.getRating(), movie.getName());
        }
        //Ordonez acest map
        Map<Double, String> treeMap;
        if (action.getSortType().equals("asc")) {
            treeMap = new TreeMap<>(videoRating);
        } else {
            treeMap = new TreeMap<>(
                    Comparator.reverseOrder()
            );
            treeMap.putAll(videoRating);
        }
        StringBuilder ratedVideos = new StringBuilder();
        int ratedVideosLen = 0;
        ratedVideos.append("Query result: [");
        for (Map.Entry<Double, String> rating : treeMap.entrySet()) {
            if (count == 0) {
                break;
            }
            for (Movie movie : movies) {
                //Aplic filtrele de gen si an
                if (movie.getName().equals(rating.getValue())) {
                    if (movie.getGenres().toString()
                            .equals(action.getFilters().get(1).get(0))) {
                        if (String.valueOf(movie.getYear())
                                .equals(action.getFilters().get(0).get(0))) {
                            ratedVideosLen += 1;
                            ratedVideos.append(rating.getValue());
                            ratedVideos.append(", ");
                            count -= 1;
                        } else if (action.getFilters().get(0).get(0) == null) {
                            ratedVideosLen += 1;
                            ratedVideos.append(rating.getValue());
                            ratedVideos.append(", ");
                            count -= 1;
                        }
                    }
                    if (action.getFilters().get(1).get(0).length() == 0
                            || action.getFilters().get(1).get(0) == null) {
                        if (String.valueOf(movie.getYear())
                                .equals(action.getFilters().get(0).get(0))) {
                            ratedVideosLen += 1;
                            ratedVideos.append(rating.getValue());
                            ratedVideos.append(", ");
                            count -= 1;
                        } else if (action.getFilters().get(0).get(0) == null) {
                            ratedVideosLen += 1;
                            ratedVideos.append(rating.getValue());
                            ratedVideos.append(", ");
                            count -= 1;
                        }
                    }
                }
            }
        }
        if (ratedVideosLen == 0) {
            ratedVideos.append("]");
            return ratedVideos.toString();
        }
        ratedVideos.deleteCharAt(ratedVideos.length() - 1);
        ratedVideos.deleteCharAt(ratedVideos.length() - 1);
        ratedVideos.append("]");
        return ratedVideos.toString();
    }

    private String favoriteMethod(
        final ActionInputData action,
        final ArrayList<Movie> movies,
        final ArrayList<User> users) {
        int count = action.getNumber();
        //Incarc campul din clasa movie cu numarul de aparitii
        // al acestuia in listele de favorite ale utilizatorilor
        for (User user : users) {
            for (String favorite : user.getFavoriteMovies()) {
                for (Movie movie : movies) {
                    if (movie.getName().equals(favorite)) {
                        movie.setNumFavorite(movie.getNumFavorite() + 1);
                        break;
                    }
                }
            }
        }
        //Creez un LinkedHashMap pentru a pastra ordinea
        //de introducere a datelor in memorie
        LinkedHashMap<String, Integer> videoFavorite = new LinkedHashMap<>();
        for (Movie movie : movies) {
            for (String genre : movie.getGenres()) {
                //Aplic filtrele si adaug filmele in map
                if (action.getFilters().get(1).get(0) == null) {
                    if (String.valueOf(movie.getYear())
                            .equals(action.getFilters().get(0).get(0))) {
                        if (movie.getNumFavorite() > 0) {
                            videoFavorite.put(movie.getName(), movie.getNumFavorite());
                        }
                    } else if (action.getFilters().get(0).get(0) == null) {
                        if (movie.getNumFavorite() > 0) {
                            videoFavorite.put(movie.getName(), movie.getNumFavorite());
                        }
                    }
                } else if (action.getFilters().get(1).get(0)
                        .toLowerCase().equals(genre.toLowerCase())) {
                    if (String.valueOf(movie.getYear())
                            .equals(action.getFilters().get(0).get(0))) {
                        if (movie.getNumFavorite() > 0) {
                            videoFavorite.put(movie.getName(), movie.getNumFavorite());
                        }
                    } else if (action.getFilters().get(0).get(0) == null) {
                        if (movie.getNumFavorite() > 0) {
                            videoFavorite.put(movie.getName(), movie.getNumFavorite());
                        }
                    }
                }
            }
        }
        //Sortez mapul
        Map<String, Integer> sorted;
        if (action.getSortType().equals("asc")) {
            sorted = videoFavorite
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

        } else {
            sorted = videoFavorite
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

        }
        //Returnez primele n filme din lista
        StringBuilder favVideos = new StringBuilder();
        int favFid = 0;
        favVideos.append("Query result: [");
        for (Map.Entry<String, Integer> favoriteVid : sorted.entrySet()) {
            if (count == 0) {
                break;
            }
            for (Movie movie : movies) {
                if (movie.getName().equals(favoriteVid.getKey())) {
                    favFid += 1;
                    favVideos.append(favoriteVid.getKey());
                    favVideos.append(", ");
                    count -= 1;
                }
            }
        }
        if (favFid == 0) {
            favVideos.append("]");
            return favVideos.toString();
        }
        favVideos.deleteCharAt(favVideos.length() - 1);
        favVideos.deleteCharAt(favVideos.length() - 1);
        favVideos.append("]");
        return favVideos.toString();
    }

    private String longestMethod(
        final ActionInputData action,
        final ArrayList<Movie> movies) {
        int count = action.getNumber();
        Map<String, Integer> videoLength = new HashMap<>();
        //Adaug elementele filtrate intr-un map cu numele si durata lor
        for (Movie movie : movies) {
            for (String genre : movie.getGenres()) {
                if (action.getFilters().get(1).get(0) == null) {
                    if (String.valueOf(movie.getYear())
                            .equals(action.getFilters().get(0).get(0))) {
                        videoLength.put(movie.getName(), movie.getDuration());
                    } else if (action.getFilters().get(0).get(0) == null) {
                        videoLength.put(movie.getName(), movie.getDuration());
                    }
                } else if (action.getFilters().get(1).get(0).toLowerCase()
                        .equals(genre.toLowerCase())) {
                    if (String.valueOf(movie.getYear())
                            .equals(action.getFilters().get(0).get(0))) {
                        videoLength.put(movie.getName(), movie.getDuration());
                    } else if (action.getFilters().get(0).get(0) == null) {
                        videoLength.put(movie.getName(), movie.getDuration());
                    }
                }
            }
        }
        //Sortez elementele din map
        Map<String, Integer> sorted;
        if (action.getSortType().equals("asc")) {
            sorted = videoLength
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

        } else {
            sorted = videoLength
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

        }
        //Returnez primele n elemente din map
        StringBuilder videoLen = new StringBuilder();
        int index = 0;
        videoLen.append("Query result: [");
        for (Map.Entry<String, Integer> favoriteVid : sorted.entrySet()) {
            if (count == 0) {
                break;
            }
            for (Movie movie : movies) {
                if (movie.getName().equals(favoriteVid.getKey())) {
                    index += 1;
                    videoLen.append(favoriteVid.getKey());
                    videoLen.append(", ");
                    count -= 1;
                }
            }
        }
        if (index == 0) {
            videoLen.append("]");
            return videoLen.toString();
        }
        videoLen.deleteCharAt(videoLen.length() - 1);
        videoLen.deleteCharAt(videoLen.length() - 1);
        videoLen.append("]");
        return videoLen.toString();
    }

    private String mostViewedMethod(
        final ActionInputData action,
        final ArrayList<Movie> movies,
        final ArrayList<User> users) {
        int count = action.getNumber();
        for (User user : users) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                for (Movie movie : movies) {
                    if (entry.getKey().equals(movie.getName())) {
                        movie.setNumViews(movie.getNumViews() + entry.getValue());
                        break;
                    }
                }
            }
        }
        Map<String, Integer> videoViews = new HashMap<>();
        for (Movie movie : movies) {
            for (String genre : movie.getGenres()) {
                try {
                    if (action.getFilters().get(1).get(0).toLowerCase()
                            .equals(genre.toLowerCase())) {
                        if (String.valueOf(movie.getYear())
                                .equals(action.getFilters().get(0).get(0))) {
                            if (movie.getNumViews() > 0) {
                                videoViews.put(movie.getName(), movie.getNumViews());
                            }
                        } else if (action.getFilters().get(0).get(0) == null) {
                            if (movie.getNumViews() > 0) {
                                videoViews.put(movie.getName(), movie.getNumViews());
                            }
                        }
                    }
                } catch (Exception ignored) {
                }

            }
        }
        Map<String, Integer> sorted;
        if (action.getSortType().equals("asc")) {
            sorted = videoViews
                    .entrySet()
                    .stream()
                    .sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                            LinkedHashMap::new));

        } else {
            sorted = videoViews
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

        }
        StringBuilder favVideos = new StringBuilder();
        int favFid = 0;
        favVideos.append("Query result: [");
        for (Map.Entry<String, Integer> favoriteVid : sorted.entrySet()) {
            if (count == 0) {
                break;
            }
            for (Movie movie : movies) {
                if (movie.getName().equals(favoriteVid.getKey())) {
                    favFid += 1;
                    favVideos.append(favoriteVid.getKey());
                    favVideos.append(", ");
                    count -= 1;
                }
            }
        }
        if (favFid == 0) {
            favVideos.append("]");
            return favVideos.toString();
        }
        favVideos.deleteCharAt(favVideos.length() - 1);
        favVideos.deleteCharAt(favVideos.length() - 1);
        favVideos.append("]");
        return favVideos.toString();
    }

}
