import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Consumer;
import entities.CostChange;
import entities.Distributor;
import entities.NewConsumer;
import input.CostChangesInputData;
import input.InputLoader;
import input.MonthlyUpdatesInputData;
import input.NewConsumerInputData;
import org.json.simple.JSONArray;
import solver.Solve;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Main {

    /**
     *
     * @param args locatia fisierelor de in si out
     * @throws Exception -
     *
     * In main am realizat citirea, scrierea si am adaugat toate datele
     * in entitatile corespunzatoare pentru a putea lucra cu ele.
     *
     */
    public static void main(String[] args) throws Exception {

//        Citirea
        File file = new File(args[0]);
        ObjectMapper objectMapper = new ObjectMapper();
        InputLoader inputLoader = objectMapper.readValue(file, InputLoader.class);

//        Adaugarea datelor in obiecte
        int numberOfTurns;
        ArrayList<Consumer> consumers = new ArrayList<>();
        ArrayList<Distributor> distributors = new ArrayList<>();
        ArrayList<NewConsumer> newConsumers = new ArrayList<>();
        ArrayList<CostChange> costChanges = new ArrayList<>();

        numberOfTurns = inputLoader.getNumberOfTurns();
        for (int i = 0; i < inputLoader.getInitialData().getConsumers().size(); i++) {
            Consumer consumer = new Consumer(
                    inputLoader.getInitialData().getConsumers().get(i).getId(),
                    inputLoader.getInitialData().getConsumers().get(i).getInitialBudget(),
                    inputLoader.getInitialData().getConsumers().get(i).getMonthlyIncome()
            );
            consumers.add(consumer);
        }

        for (int i = 0; i < inputLoader.getInitialData().getDistributors().size(); i++) {
            Distributor distributor = new Distributor(
                    inputLoader.getInitialData().
                            getDistributors().get(i).getId(),
                    inputLoader.getInitialData().
                            getDistributors().get(i).getContractLength(),
                    inputLoader.getInitialData().
                            getDistributors().get(i).getInitialBudget(),
                    inputLoader.getInitialData().
                            getDistributors().get(i).getInitialInfrastructureCost(),
                    inputLoader.getInitialData().
                            getDistributors().get(i).getInitialProductionCost()
            );
            distributors.add(distributor);
        }
        int index = 0;
        for (MonthlyUpdatesInputData monthlyUpdatesInputData
                : inputLoader.getMonthlyUpdates()) {
            for (NewConsumerInputData indexNewConsumer
                    : monthlyUpdatesInputData.getNewConsumers()) {
                NewConsumer newConsumer = new NewConsumer(
                        index,
                        indexNewConsumer.getId(),
                        indexNewConsumer.getInitialBudget(),
                        indexNewConsumer.getMonthlyIncome()
                );
                newConsumers.add(newConsumer);

            }

            for (CostChangesInputData costChangesInputData
                    : monthlyUpdatesInputData.getCostsChanges()) {
                CostChange costChange = new CostChange(
                        index,
                        costChangesInputData.getId(),
                        costChangesInputData.getInfrastructureCost(),
                        costChangesInputData.getProductionCost()
                );
                costChanges.add(costChange);
            }
            index++;
        }
//        Creez un obiect solve care contine clasa game
        Solve solve = new Solve();
        solve.game(
                numberOfTurns,
                consumers,
                distributors,
                newConsumers,
                costChanges
        );
//        Afisare
        ObjectMapper objectMapper1 = new ObjectMapper();
        JSONArray consumerObj = new JSONArray();
        for (Consumer consumer : consumers) {
            LinkedHashMap<String, Object> consumerData = new LinkedHashMap<>();
            consumerData.put("id", consumer.getId());
            consumerData.put("isBankrupt", consumer.isBankrupt());
            consumerData.put("budget", (long) consumer.getBudget());
            consumerObj.add(consumerData);
        }

        JSONArray distributorObj = new JSONArray();
        for (Distributor distributor : distributors) {
            LinkedHashMap<String, Object> distributorData = new LinkedHashMap<>();
            distributorData.put("id", distributor.getId());
            distributorData.put("budget", (long) distributor.getBudget());
            distributorData.put("isBankrupt", distributor.isBankrupt());
            JSONArray contractObj = new JSONArray();
            for (Consumer consumer : distributor.getContracts()) {
                LinkedHashMap<String, Object> contractData = new LinkedHashMap<>();
                contractData.put("consumerId", consumer.getId());
                contractData.put("price", (long) consumer.getContractPrice());
                contractData.put("remainedContractMonths", consumer.getContractLength());
                contractObj.add(contractData);
            }

            distributorData.put("contracts", contractObj);
            distributorObj.add(distributorData);
        }

        LinkedHashMap<String, JSONArray> output = new LinkedHashMap<>();
        output.put("consumers", consumerObj);
        output.put("distributors", distributorObj);


        objectMapper1.writerWithDefaultPrettyPrinter().
                writeValue(new File(args[1]), output);
    }
}
