package main;

import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entities.Actor;
import entities.Movie;
import entities.Show;
import entities.User;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        ArrayList<Actor> actors = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<Show> shows = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        List<ActionInputData> commands = input.getCommands();
        for (int i = 0; i < input.getActors().size(); i++) {
            Actor actor = new Actor(i, input);
            actors.add(i, actor);
        }
        for (int i = 0; i < input.getMovies().size(); i++) {
            Movie movie = new Movie(i, input);
            movies.add(i, movie);
        }
        for (int i = 0; i < input.getSerials().size(); i++) {
            Show show = new Show(i, input);
            shows.add(i, show);
        }
        for (int i = 0; i < input.getUsers().size(); i++) {
            User user = new User(i, input);
            users.add(i, user);
            System.out.println(users.get(i).getHistory());
        }

        for (ActionInputData command : commands) {
            JSONObject object = fileWriter.writeFile(command.getActionId(),
                    command.getCriteria(), command.getTitle());
            arrayResult.add(object);
        }

        fileWriter.closeJSON(arrayResult);
    }
}
