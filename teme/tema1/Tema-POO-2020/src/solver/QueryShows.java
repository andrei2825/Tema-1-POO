package solver;

import entertainment.Season;
import entities.Show;
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

public final class QueryShows {
    /**
     *
     * @param action - action got from input
     * @param shows - list of shows
     * @param users - list of users
     * @return <-- returns a string after processing the queries for shows
     */
    public String queryShows(
        final ActionInputData action,
        final ArrayList<Show> shows,
        final ArrayList<User> users) {
        if (action.getSortType().equals("asc")) {
            shows.sort(Comparator.comparing(Show::getTitle));
        } else {
            shows.sort(Comparator.comparing(Show::getTitle).reversed());
        }

        return switch (action.getCriteria()) {
            case "ratings" -> ratingMethod(action, shows);
            case "favorite" -> favoriteMethod(action, shows, users);
            case "longest" -> longestMethod(action, shows);
            case "most_viewed" -> mostViewedMethod(action, shows, users);
            default -> null;
        };
    }

    private String ratingMethod(
        final ActionInputData action,
        final ArrayList<Show> shows) {
        int count = action.getNumber();
        Map<Double, String> videoRating = new HashMap<>();
        for (Show show : shows) {
            double rate = 0;
            for (int i = 0; i < show.getNumberOfSeasons(); i++) {
                rate += show.getRating().get(i);
            }
            videoRating.put(rate / show.getNumberOfSeasons(), show.getTitle());
        }
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
            for (Show show : shows) {
                if (show.getTitle().equals(rating.getValue())) {
                    if (show.getGenres().toString()
                            .equals(action.getFilters().get(1).get(0))) {
                        if (String.valueOf(show.getYear())
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
        final ArrayList<Show> shows,
        final ArrayList<User> users) {
        int count = action.getNumber();
        for (User user : users) {
            for (String favorite : user.getFavoriteMovies()) {
                for (Show show : shows) {
                    if (show.getTitle().equals(favorite)) {
                        show.setNumFavorite(show.getNumFavorite() + 1);
                        break;
                    }
                }
            }
        }
        Map<String, Integer> videoFavorite = new HashMap<>();
        for (Show show : shows) {
            for (String genre : show.getGenres()) {
                if (action.getFilters().get(1).get(0) == null) {
                    if (String.valueOf(show.getYear())
                            .equals(action.getFilters().get(0).get(0))) {
                        if (show.getNumFavorite() > 0) {
                            videoFavorite.put(show.getTitle(), show.getNumFavorite());
                        }
                    } else if (action.getFilters().get(0).get(0) == null) {
                        if (show.getNumFavorite() > 0) {
                            videoFavorite.put(show.getTitle(), show.getNumFavorite());
                        }
                    }
                } else if (action.getFilters().get(1).get(0).toLowerCase()
                        .equals(genre.toLowerCase())) {
                    if (String.valueOf(show.getYear())
                            .equals(action.getFilters().get(0).get(0))) {
                        if (show.getNumFavorite() > 0) {
                            videoFavorite.put(show.getTitle(), show.getNumFavorite());
                        }
                    } else if (action.getFilters().get(0).get(0) == null) {
                        if (show.getNumFavorite() > 0) {
                            videoFavorite.put(show.getTitle(), show.getNumFavorite());
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
        StringBuilder favVideos = new StringBuilder();
        int favFid = 0;
        favVideos.append("Query result: [");
        for (Map.Entry<String, Integer> favoriteVid : sorted.entrySet()) {
            if (count == 0) {
                break;
            }
            for (Show show : shows) {
                if (show.getTitle().equals(favoriteVid.getKey())) {
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
        final ArrayList<Show> shows) {
        int count = action.getNumber();
        Map<String, Integer> videoLength = new HashMap<>();
        for (Show show : shows) {
            for (String genre : show.getGenres()) {
                try {
                    if (action.getFilters().get(1).get(0).toLowerCase()
                            .equals(genre.toLowerCase())) {
                        if (String.valueOf(show.getYear())
                                .equals(action.getFilters().get(0).get(0))) {
                            int duration = 0;
                            for (Season season : show.getSeason()) {
                                duration += season.getDuration();
                            }
                            videoLength.put(show.getTitle(), duration);
                        } else if (action.getFilters().get(0).get(0) == null) {
                            int duration = 0;
                            for (Season season : show.getSeason()) {
                                duration += season.getDuration();
                            }
                            videoLength.put(show.getTitle(), duration);
                        }
                    }
                } catch (Exception ignored) {
                }

            }
        }
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
        StringBuilder videoLen = new StringBuilder();
        int index = 0;
        videoLen.append("Query result: [");
        for (Map.Entry<String, Integer> favoriteVid : sorted.entrySet()) {
            if (count == 0) {
                break;
            }
            for (Show show : shows) {
                if (show.getTitle().equals(favoriteVid.getKey())) {
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
        final ArrayList<Show> shows,
        final ArrayList<User> users) {
        int count = action.getNumber();
        for (User user : users) {
            for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
                for (Show show : shows) {
                    if (entry.getKey().equals(show.getTitle())) {
                        show.setNumViews(show.getNumViews() + entry.getValue());
                        break;
                    }
                }
            }
        }
        Map<String, Integer> videoViews = new HashMap<>();
        for (Show show : shows) {
            for (String genre : show.getGenres()) {
                try {
                    if (action.getFilters().get(1).get(0).toLowerCase()
                            .equals(genre.toLowerCase())) {
                        if (String.valueOf(show.getYear())
                                .equals(action.getFilters().get(0).get(0))) {
                            if (show.getNumViews() > 0) {
                                videoViews.put(show.getTitle(), show.getNumViews());
                            }
                        } else if (action.getFilters().get(0).get(0) == null) {
                            if (show.getNumViews() > 0) {
                                videoViews.put(show.getTitle(), show.getNumViews());
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
            for (Show show : shows) {
                if (show.getTitle().equals(favoriteVid.getKey())) {
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
