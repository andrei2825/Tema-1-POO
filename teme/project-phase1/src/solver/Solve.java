package solver;

import entities.Consumer;
import entities.CostChange;
import entities.Distributor;
import entities.NewConsumer;

import java.util.ArrayList;

public final class Solve {
    /**
     *
     * @param numberOfTurns - numarul de runde ale jocului
     * @param consumers - lista de consumers
     * @param distributors - lista de distributors
     * @param newConsumers - lista de newConsumers
     * @param costChanges - lista de schimbari de cost
     */
    public void game(
            final int numberOfTurns,
            final ArrayList<Consumer> consumers,
            final ArrayList<Distributor> distributors,
            final ArrayList<NewConsumer> newConsumers,
            final ArrayList<CostChange> costChanges) {
//      Apelez metoda care prelucreaza datele prntru prima runda
        FirstRound firstRound = new FirstRound();
        firstRound.round1(
                consumers,
                distributors
        );
//      Apelez metoda care prelucreaza datele prntru urmatoarele numberOfTurns runde

        NextRounds nextRounds = new NextRounds();
        nextRounds.otherRounds(
                numberOfTurns,
                consumers,
                distributors,
                newConsumers,
                costChanges
        );
    }
}
