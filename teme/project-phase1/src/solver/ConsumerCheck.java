package solver;

import entities.Consumer;
import entities.Distributor;

import java.util.ArrayList;
import java.util.Map;


public final class ConsumerCheck {
    /**
     *
     * @param consumer - consumator
     * @param sortedContracts - LinkedHashMap sortat crescator dupa pretul contractului
     * @param distributors - lista de distribuitori
     *
     */
    public void consumerCheck(
            final Consumer consumer,
            final Map<Integer, Double> sortedContracts,
            final ArrayList<Distributor> distributors
    ) {
//      Verific daca consumatorul mai este in joc
        if (!consumer.isBankrupt()) {
//          Adaug venitul consumatorului
            consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
//          Daca consumatorul isi si-a incheiat contractul sau
//          daca acessta nu a avut deja un contract, alege
//          distribuitorul cu cel mai mic pret si plateste prima luna
            if (consumer.getContractLength() == 0) {
                for (Distributor distributor : distributors) {
                    if (distributor.getId() == consumer.getDistributorId()) {
                        distributor.getContracts().remove(consumer);
                        distributor.setNumberOfContracts(
                                distributor.getNumberOfContracts() - 1);
                    }
                }
                consumer.setOldBill(0);
                consumer.setDistributorId(
                        sortedContracts.entrySet().stream().findFirst().get().getKey());
                for (Distributor distributor : distributors) {
                    if (!distributor.isBankrupt()) {
                        if (distributor.getId() == consumer.getDistributorId()) {
                            consumer.setContractLength(distributor.getContractLength());
                            consumer.setContractPrice(distributor.getContractPrice());
                            distributor.getContracts().add(consumer);
                            distributor.setNumberOfContracts(
                                    distributor.getNumberOfContracts() + 1);
                            if (consumer.getContractPrice() <= consumer.getBudget()) {
                                consumer.setContractLength(
                                        consumer.getContractLength() - 1);
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
                                consumer.setContractLength(
                                        consumer.getContractLength() - 1);
                                consumer.setOldBill(
                                        consumer.getContractPrice());
                            }
                            break;
                        }
                    }
                }
//          Daca consumatorul are deja un contract incheiat, acesta plateste
//          valoarea din luna curenta
            } else {
                for (Distributor distributor : distributors) {
                    if (!distributor.isBankrupt()) {
                        if (distributor.getId() == consumer.getDistributorId()) {
                            if (consumer.getOldBill() == 0) {
                                if (consumer.getContractPrice() <= consumer.getBudget()) {
                                    consumer.setContractLength(
                                            consumer.getContractLength() - 1);
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
                                    consumer.setContractLength(
                                            consumer.getContractLength() - 1);
                                    consumer.setOldBill(
                                            consumer.getContractPrice());
                                }
                            } else {
//                              Calculez factura pentru luna cu penalitate
                                double newBill = Math.round(Math.floor(1.2
                                        * consumer.getOldBill()))
                                        + consumer.getContractPrice();
                                if (consumer.getBudget() >= newBill) {
                                    consumer.setContractLength(
                                            consumer.getContractLength() - 1);
                                    distributor.setBudget(
                                            distributor.getBudget()
                                                    + newBill
                                    );
                                    consumer.setBudget(
                                            consumer.getBudget()
                                                    - newBill
                                    );
                                    consumer.setOldBill(0);
                                } else {
//                                  Elimin consumatorul din joc
                                    consumer.setBankrupt(true);
                                    distributor.getContracts().remove(consumer);
                                    distributor.setLastTurnContracts(
                                            distributor.getLastTurnContracts() + 1);
                                    distributor.setNumberOfContracts(
                                            distributor.getNumberOfContracts() - 1
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
