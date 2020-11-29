package solver;

import entities.User;
import fileio.ActionInputData;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public final class QueryUsers {
  /**
   *
   * @param action - action got from input
   * @param users - list of users
   * @return <-- returns a string after processing the queries for users
   */
  public String queryUsers(
      final ActionInputData action,
      final ArrayList<User> users) {
    if (action.getSortType().equals("asc")) {
      users.sort(Comparator.comparing(User::getUsername));
    } else {
      users.sort(Comparator.comparing(User::getUsername).reversed());
    }
    int count = action.getNumber();
    LinkedHashMap<String, Integer> userRatings = new LinkedHashMap<>();
    for (User user : users) {
      if (user.getRatedIndex() >= 0) {
        userRatings.put(user.getUsername(), user.getNumRatings());
      }
    }
    Map<String, Integer> sorted;
    if (action.getSortType().equals("asc")) {
      sorted =
          userRatings.entrySet().stream()
              .sorted(comparingByValue())
              .collect(
                  toMap(
                      Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

    } else {
      sorted =
          userRatings.entrySet().stream()
              .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
              .collect(
                  toMap(
                      Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
    StringBuilder numRating = new StringBuilder();
    int index = 0;
    numRating.append("Query result: [");
    for (Map.Entry<String, Integer> rating : sorted.entrySet()) {
      if (count == 0) {
        break;
      }
      if (rating.getValue() > 0) {
        index += 1;
        numRating.append(rating.getKey());
        numRating.append(", ");
        count -= 1;
      }
    }
    if (index == 0) {
      numRating.append("]");
      return numRating.toString();
    }
    numRating.deleteCharAt(numRating.length() - 1);
    numRating.deleteCharAt(numRating.length() - 1);
    numRating.append("]");
    return numRating.toString();
  }
}
