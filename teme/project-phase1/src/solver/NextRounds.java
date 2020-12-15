package solver;

import entities.Consumer;
import entities.CostChange;
import entities.Distributor;
import entities.NewConsumer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public final class NextRounds {
    /**
     *
     * @param numberOfTurns - numrul de luni
     * @param consumers - lista de consumatori
     * @param distributors - lista de distribuitori
     * @param newConsumers - lista cu noii consumatori
     * @param costChanges - lista cu schimbarile aduse distribuitorilor
     */
    public void otherRounds(
            final int numberOfTurns,
            final ArrayList<Consumer> consumers,
            final ArrayList<Distributor> distributors,
            final ArrayList<NewConsumer> newConsumers,
            final ArrayList<CostChange> costChanges) {
        int temp1 = 0;
//      Parcurg fiecare luna
        for (int i = 0; i < numberOfTurns; i++) {
            for (Distributor distributor : distributors) {
                distributor.setLastTurnContracts(0);
            }
//          Adaug noii consumatori din luna curenta
            if (temp1 < newConsumers.size() && newConsumers.get(temp1).getMonth() == i) {
                consumers.add(new Consumer(
                        newConsumers.get(temp1).getId(),
                        newConsumers.get(temp1).getInitialBudget(),
                        newConsumers.get(temp1).getMonthlyIncome()
                ));
                temp1++;
            }
//          Adaug schimbarile de cost din luna curenta
            for (CostChange costChange : costChanges) {
                if (costChange.getMonth() == i) {
                    for (Distributor distributor : distributors) {
                        if (distributor.getId() == costChange.getId()) {
                            distributor.setInitialInfrastructureCost(
                                    costChange.getInfrastructureCost()
                            );
                            distributor.setInitialProductionCost(
                                    costChange.getProductionCost()
                            );
                        }
                    }
                }
            }
//          Setez pretul conractului pentru distribuitor
            for (Distributor distributor : distributors) {
                if (!distributor.isBankrupt()) {
                    distributor.setProfit(0.2 * distributor.getInitialProductionCost());
                    if (distributor.getNumberOfContracts() == 0) {
                        distributor.setContractPrice(
                                distributor.getInitialInfrastructureCost()
                                + distributor.getInitialProductionCost()
                                + distributor.getProfit()
                        );
                    } else {
                        distributor.setContractPrice(
                                Math.round(
                                        Math.floor(
                                                (distributor.getInitialInfrastructureCost()
                                                        / distributor.getNumberOfContracts())
                                                        + distributor.getInitialProductionCost()
                                                        + distributor.getProfit()
                                        )
                                )
                        );
                    }
                }
            }
//          Ordonez distribuitorii dupa pretul contractului
            LinkedHashMap<Integer, Double> distributorsPrices = new LinkedHashMap<>();
            for (Distributor distributor : distributors) {
                if (!distributor.isBankrupt()) {
                    distributorsPrices.put(distributor.getId(), distributor.getContractPrice());
                }
            }
            Map<Integer, Double> sortedContracts;
            sortedContracts =
                    distributorsPrices.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(
                                    toMap(Map.Entry::getKey, Map.Entry::getValue,
                                            (e1, e2) -> e2, LinkedHashMap::new));
//          Apelez functia ce se ocupa de prelucrarea consumatorilor
            for (Consumer consumer : consumers) {
                ConsumerCheck consumerCheck = new ConsumerCheck();
                consumerCheck.consumerCheck(
                        consumer,
                        sortedContracts,
                        distributors
                );
            }
//          Apelez functia ce se ocupa de prelucrarea distribuitorilor
            for (Distributor distributor : distributors) {
                DistributorCheck distributorCheck = new DistributorCheck();
                distributorCheck.distributorCheck(
                        distributor,
                        consumers
                );
            }
        }
    }
}
