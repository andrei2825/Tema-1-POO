package solver;

import entities.Consumer;
import entities.Distributor;

import java.util.ArrayList;

public class DistributorCheck {
    /**
     *
     * @param distributor - distribuitor
     * @param consumers - lista de consumatori
     */
    public void distributorCheck(
            final Distributor distributor,
            final ArrayList<Consumer> consumers
    ) {
//      Verific daca distribuitorul mai este in joc
        if (!distributor.isBankrupt()) {
//          Calculez suma pe care trebuie sa o plateasca distribuitorul in luna curenta
            if (distributor.getLastTurnContracts() != 0) {
                distributor.setMonthlyCost(
                        distributor.getInitialInfrastructureCost()
                                + distributor.getInitialProductionCost()
                                * (distributor.getLastTurnContracts()
                                + distributor.getNumberOfContracts())
                );
            } else {
                distributor.setMonthlyCost(
                        distributor.getInitialInfrastructureCost()
                                + distributor.getInitialProductionCost()
                                * distributor.getNumberOfContracts()
                );
            }
//          Daca distribuitorul nu are buget suficient, este eliminat din joc
            if (!(distributor.getBudget() >= distributor.getMonthlyCost())) {
                distributor.setBankrupt(true);
                for (Consumer consumer : consumers) {
                    if (!consumer.isBankrupt()) {
                        if (consumer.getDistributorId() == distributor.getId()) {
                            consumer.setDistributorId(-1);
                            consumer.setOldBill(0);
                            consumer.setContractLength(0);
                            distributor.getContracts().remove(consumer);
                        }
                    }
                }
            }
//          Calculez noul buget al distribuitorului
            distributor.setBudget(
                    distributor.getBudget()
                            - distributor.getMonthlyCost()
            );
        }
    }
}
