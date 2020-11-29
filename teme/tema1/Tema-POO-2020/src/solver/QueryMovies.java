package solver;

import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class QueryMovies {
    public String  queryMovies(ActionInputData action, ArrayList<Actor> actors,
                               ArrayList<Movie> movies, ArrayList<Show> shows, ArrayList<User> users) {
        int count = action.getNumber();
        if (action.getSortType().equals("asc")) {
            movies.sort(Comparator.comparing(Movie::getName));
        }
        else {
            movies.sort(Comparator.comparing(Movie::getName).reversed());
        }
        //TODO RATING METHOD
        if (action.getCriteria().equals("ratings")) {
            Map<Double, String> videoRating = new HashMap<Double, String>();
            for (Movie movie : movies) {
                videoRating.put(movie.getRating(), movie.getName());
            }
            Map<Double, String> treeMap;
            if (action.getSortType().equals("asc")) {
                treeMap = new TreeMap<Double, String>(videoRating);
            } else {
                treeMap = new TreeMap<Double, String>(
                        new Comparator<Double>() {
                            @Override
                            public int compare(Double aDouble, Double t1) {
                                return t1.compareTo(aDouble);
                            }
                        }
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
                    if (movie.getName().equals(rating.getValue())) {
                        if (movie.getGenres().toString().equals(action.getFilters().get(1).get(0))) {
                            if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                                ratedVideosLen += 1;
                                ratedVideos.append(rating.getValue());
                                ratedVideos.append(", ");
                                count -= 1;
                            }
                            else if (action.getFilters().get(0).get(0) == null) {
                                ratedVideosLen += 1;
                                ratedVideos.append(rating.getValue());
                                ratedVideos.append(", ");
                                count -= 1;
                            }
                        }
                        if (action.getFilters().get(1).get(0).length() == 0 ||
                                action.getFilters().get(1).get(0) == null) {
                            if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                                ratedVideosLen += 1;
                                ratedVideos.append(rating.getValue());
                                ratedVideos.append(", ");
                                count -= 1;
                            }
                            else if (action.getFilters().get(0).get(0) == null) {
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

        //TODO FAVORITE METHOOD

        if (action.getCriteria().equals("favorite")) {
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
            Map<String , Integer> videoFavorite = new HashMap<String, Integer>();
            for (Movie movie : movies) {
                for (String genre : movie.getGenres()) {
                    if (action.getFilters().get(1).get(0) == null) {
                        if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                            if (movie.getNumFavorite() > 0) {
                                videoFavorite.put(movie.getName(), movie.getNumFavorite());
                            }
                        }
                        else if (action.getFilters().get(0).get(0) == null) {
                            if (movie.getNumFavorite() > 0) {
                                videoFavorite.put(movie.getName(), movie.getNumFavorite());
                            }
                        }
                    }
                    else if (action.getFilters().get(1).get(0).toLowerCase().equals(genre.toLowerCase())) {
                        if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                            if (movie.getNumFavorite() > 0) {
                                videoFavorite.put(movie.getName(), movie.getNumFavorite());
                            }
                        }
                        else if (action.getFilters().get(0).get(0) == null) {
                            if (movie.getNumFavorite() > 0) {
                                videoFavorite.put(movie.getName(), movie.getNumFavorite());
                            }
                        }
                    }
                }
            }
            Map<String, Integer> sorted;
            if (action.getSortType().equals("asc")) {
                sorted = videoFavorite
                        .entrySet()
                        .stream()
                        .sorted(comparingByValue())
                        .collect( toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
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
            StringBuilder favVideos = new StringBuilder();
            int favFid = 0;
            favVideos.append("Query result: [");
            for (Map.Entry<String , Integer> favoriteVid : sorted.entrySet()) {
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


        //TODO LENGTH METHOOD


        if (action.getCriteria().equals("longest")) {
            Map<String, Integer> videoLength = new HashMap<String, Integer>();
            for (Movie movie : movies) {
                for (String genre : movie.getGenres()) {
                    if (action.getFilters().get(1).get(0) == null) {
                        if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                            videoLength.put(movie.getName(), movie.getDuration());
                        }
                        else if (action.getFilters().get(0).get(0) == null) {
                            videoLength.put(movie.getName(), movie.getDuration());
                        }
                    }
                    else if (action.getFilters().get(1).get(0).toLowerCase().equals(genre.toLowerCase())) {
                        if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                            videoLength.put(movie.getName(), movie.getDuration());
                        }
                        else if (action.getFilters().get(0).get(0) == null) {
                            videoLength.put(movie.getName(), movie.getDuration());
                        }
                    }
                }
            }
            Map<String, Integer> sorted;
            if (action.getSortType().equals("asc")) {
                sorted = videoLength
                        .entrySet()
                        .stream()
                        .sorted(comparingByValue())
                        .collect( toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
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
            StringBuilder videoLen = new StringBuilder();
            int index = 0;
            videoLen.append("Query result: [");
            for (Map.Entry<String , Integer> favoriteVid : sorted.entrySet()) {
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

        //TODO MOST_VIEWED METHOOD

        if (action.getCriteria().equals("most_viewed")) {
            for (User user : users) {
                for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                    for (Movie movie : movies) {
                        if (entry.getKey().equals(movie.getName())) {
                            movie.numViews += entry.getValue();
                            break;
                        }
                    }
                }
            }
            Map<String, Integer> videoViews = new HashMap<String, Integer>();
            for (Movie movie : movies) {
                for (String genre : movie.getGenres()) {
                    try {
                        if (action.getFilters().get(1).get(0).toLowerCase().equals(genre.toLowerCase())) {
                            if (String.valueOf(movie.getYear()).equals(action.getFilters().get(0).get(0))) {
                                if (movie.numViews > 0) {
                                    videoViews.put(movie.getName(), movie.numViews);
                                }
                            }
                            else if (action.getFilters().get(0).get(0) == null) {
                                if (movie.numViews > 0) {
                                    videoViews.put(movie.getName(), movie.numViews);
                                }
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }

                }
            }
            Map<String, Integer> sorted;
            if (action.getSortType().equals("asc")) {
                sorted = videoViews
                        .entrySet()
                        .stream()
                        .sorted(comparingByValue())
                        .collect( toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
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
            for (Map.Entry<String , Integer> favoriteVid : sorted.entrySet()) {
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
        return null;
    }
}
