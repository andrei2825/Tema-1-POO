package solver;

import entities.Consumer;
import entities.Distributor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public final class FirstRound {
    /**
     *
     * @param consumers - lista de consumatori
     * @param distributors - lista de distribuitori
     */
    public void round1(
            final ArrayList<Consumer> consumers,
            final ArrayList<Distributor> distributors) {
        for (Distributor distributor : distributors) {
            distributor.setProfit(
                    (int) Math.round(Math.floor(
                            0.2 * distributor.getInitialProductionCost())
                    )
            );
            distributor.setContractPrice(
                    distributor.getInitialInfrastructureCost()
                            + distributor.getInitialProductionCost()
                            + distributor.getProfit()
            );
        }

        for (Consumer consumer : consumers) {
            LinkedHashMap<Integer, Double> priceList = new LinkedHashMap<>();
            for (Distributor distributor : distributors) {
                priceList.put(distributor.getId(), distributor.getContractPrice());
            }
            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
            Map<Integer, Double> sorted;
            sorted =
                    priceList.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                            .collect(
                                    toMap(Map.Entry::getKey, Map.Entry::getValue,
                                            (e1, e2) -> e2, LinkedHashMap::new));
            consumer.setDistributorId(sorted.entrySet().stream().findFirst().get().getKey());
            for (Distributor distributor : distributors) {
                if (distributor.getId() == consumer.getDistributorId()) {
                    consumer.setContractLength(distributor.getContractLength());
                    consumer.setContractPrice(distributor.getContractPrice());
                    distributor.getContracts().add(consumer);
                    distributor.setNumberOfContracts(distributor.getNumberOfContracts() + 1);
                    if (consumer.getContractPrice() <= consumer.getBudget()) {
                        consumer.setContractLength(consumer.getContractLength() - 1);
                        distributor.setBudget(
                                distributor.getBudget()
                                        + consumer.getContractPrice()
                        );
                        consumer.setBudget(
                                consumer.getBudget()
                                        - consumer.getContractPrice()
                        );
                        consumer.setOldBill(0);
                    } else {
                        consumer.setContractLength(consumer.getContractLength() - 1);
                        consumer.setOldBill(consumer.getContractPrice());
                    }
                    break;
                }
            }
        }

        for (Distributor distributor : distributors) {
            distributor.setMonthlyCost(
                    distributor.getInitialInfrastructureCost()
                            + distributor.getInitialProductionCost()
                            * distributor.getContracts().size()
            );
            if (distributor.getMonthlyCost() <= distributor.getBudget()) {
                distributor.setBudget(distributor.getBudget() - distributor.getMonthlyCost());
            } else {
                distributor.setBankrupt(true);
                distributor.setBudget(distributor.getBudget() - distributor.getMonthlyCost());
                distributor.setNumberOfContracts(0);
                distributor.getContracts().clear();
                for (Consumer consumer : consumers) {
                    if (!consumer.isBankrupt()) {
                        if (consumer.getDistributorId() == distributor.getId()) {
                            consumer.setDistributorId(-1);
                            consumer.setOldBill(0);
                            consumer.setContractLength(0);
                            consumer.setContractPrice(0);
                            distributor.getContracts().remove(consumer);
                        }
                    }
                }
            }
        }
    }
}
