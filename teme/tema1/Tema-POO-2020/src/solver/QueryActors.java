package solver;

import actor.ActorsAwards;
import entities.Actor;
import entities.Movie;
import entities.Show;
import fileio.ActionInputData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public final class QueryActors {
    private final int positionZero = 0;
    private final int positionOne = 1;
    private final int positionTwo = 2;
    private final int positionThree = 3;

    /**
     *
     * @param action - action got from input
     * @param actors - list of actors
     * @param movies - list of movies
     * @param shows - list of shows
     * @return <-- returns a string after processing the queries for actors
     */
    public String queryActors(
        final ActionInputData action,
        final ArrayList<Actor> actors,
        final ArrayList<Movie> movies,
        final ArrayList<Show> shows) {
        return switch (action.getCriteria()) {
            case "average" -> averageActors(action, actors, movies, shows);
            case "awards" -> awardsActors(action, actors);
            case "filter_description" -> filteredActors(action, actors);
            default -> null;
        };
    }

    private String averageActors(
        final ActionInputData action,
        final ArrayList<Actor> actors,
        final ArrayList<Movie> movies,
        final ArrayList<Show> shows) {
        for (Actor actor : actors) {
            double avg = 0;
            int num = 0;
            //Calculez suma ratingurilor actorului
            for (int i = 0; i < actor.getFilmography().size(); i++) {
                for (Movie movie : movies) {
                    if (movie.getName().equals(actor.getFilmography().get(i))) {
                        if (movie.getRating() != 0) {
                            num += 1;
                            if (movie.getNumRatings() != 0) {
                                avg += (movie.getRating() / movie.getNumRatings());
                            }
                        }
                        break;
                    }
                }
                for (Show show : shows) {
                    if (show.getTitle().equals(actor.getFilmography().get(i))) {
                        double avgSeasons = 0;
                        for (int j = 0; j < show.getNumberOfSeasons(); j++) {
                            if (show.getNumRatings().get(j) != 0) {
                                avgSeasons += show.getRating().get(j) / show.getNumRatings().get(j);
                            }
                        }
                        if (show.getNumberOfSeasons() != 0) {
                            avgSeasons = avgSeasons / show.getNumberOfSeasons();
                        }
                        if (avgSeasons != 0) {
                            avg += avgSeasons;
                            num += 1;
                        }
                    }
                }
            }
            //Calculez media ratingurilor actorului
            if (num == 0) {
                actor.setAvgRating(0);
            } else {
                actor.setAvgRating(avg / num);
            }
        }
        //Ordonez crescator / descrescator actorii dupa nume si rating
        if (action.getSortType().equals("asc")) {
            actors.sort(Comparator.comparing(Actor::getName));
            actors.sort(Comparator.comparing(Actor::getAvgRating));
            //Creez un stringBuilder in care voi tine stringul ce trebuie returnat
            StringBuilder actorList = new StringBuilder();
            actorList.append("Query result: [");
            int count = action.getNumber();
            for (Actor actor : actors) {
                if (actor.getAvgRating() > 0) {
                    actorList.append(actor.getName());
                    actorList.append(", ");
                    count -= 1;
                }
                if (count == 0) {
                    break;
                }
            }
            actorList.deleteCharAt(actorList.length() - 1);
            actorList.deleteCharAt(actorList.length() - 1);
            actorList.append("]");
            return actorList.toString();
        } else if (action.getSortType().equals("desc")) {
            actors.sort(Comparator.comparing(Actor::getName).reversed());
            actors.sort(Comparator.comparing(Actor::getAvgRating).reversed());
            StringBuilder actorList = new StringBuilder();
            actorList.append("Query result: [");
            int count = action.getNumber();
            for (Actor actor : actors) {
                if (actor.getAvgRating() > 0) {
                    actorList.append(actor.getName());
                    actorList.append(", ");
                    count -= 1;
                }
                if (count == 0) {
                    break;
                }
            }
            actorList.deleteCharAt(actorList.length() - 1);
            actorList.deleteCharAt(actorList.length() - 1);
            actorList.append("]");
            return actorList.toString();
        }
        return null;
    }

    private String awardsActors(
        final ActionInputData action,
        final ArrayList<Actor> actors) {

        //Ordonez actorii alfabetic

        if (action.getSortType().equals("asc")) {
            actors.sort(Comparator.comparing(Actor::getName));
        } else {
            actors.sort(Comparator.comparing(Actor::getName).reversed());
        }

        //Parcurg lista de actori si le adug numele in string builder doar celor
        // care au toate premiile in lista de awards

        StringBuilder actorList = new StringBuilder();
        actorList.append("Query result: [");
        int actorListLen = 0;
        for (Actor actor : actors) {
            for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                actor.setNumAwards(actor.getNumAwards() + entry.getValue());
            }
        }
        if (action.getSortType().equals("asc")) {
            actors.sort(Comparator.comparing(Actor::getNumAwards));
        } else if (action.getSortType().equals("desc")) {
            actors.sort(Comparator.comparing(Actor::getNumAwards).reversed());
        }
        for (Actor actor : actors) {
            if (actor.getAwards().size() < action.getFilters().get(positionThree).size()) {
                continue;
            }
            int check = 0;
            for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                for (String award : action.getFilters().get(positionThree)) {
                    if (award.equals(entry.getKey().toString())) {
                        check += 1;
                    }
                }
            }
            if (check == action.getFilters().get(positionThree).size()) {
                actorListLen += 1;
                actorList.append(actor.getName());
                actorList.append(", ");
            }
        }
        if (actorListLen == 0) {
            actorList.append("]");
            return actorList.toString();
        }
        actorList.deleteCharAt(actorList.length() - 1);
        actorList.deleteCharAt(actorList.length() - 1);
        actorList.append("]");
        return actorList.toString();
    }

    private String filteredActors(
        final ActionInputData action,
        final ArrayList<Actor> actors) {
        if (action.getSortType().equals("asc")) {
            actors.sort(Comparator.comparing(Actor::getName));
        } else {
            actors.sort(Comparator.comparing(Actor::getName).reversed());
        }
        StringBuilder actorList = new StringBuilder();
        actorList.append("Query result: [");
        int actorListLen = 0;
        for (Actor actor : actors) {
            int check = 0;
            for (String word : action.getFilters().get(2)) {
                //Impart descrierea actorului in cuvinte independente
                String[] sentence = actor.getCareerDescription().split("\\W+");
                for (String description : sentence) {
                    //Caut cuvintele din filtru si incrementez o valriabila pentru
                    //fiecare cuvant gasit
                    if (description.toLowerCase().equals(word.toLowerCase())) {
                        check += 1;
                        break;
                    }
                }
            }
            //Daca gasesc toate cuvintele, adautg actorul in stringul de retunr
            if (check == action.getFilters().get(2).size()) {
                actorListLen += 1;
                actorList.append(actor.getName());
                actorList.append(", ");
            }
        }
        if (actorListLen == 0) {
            actorList.append("]");
            return actorList.toString();
        }
        actorList.deleteCharAt(actorList.length() - 1);
        actorList.deleteCharAt(actorList.length() - 1);
        actorList.append("]");
        return actorList.toString();
    }
}
