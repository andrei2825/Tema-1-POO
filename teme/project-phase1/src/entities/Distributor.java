package entities;

import java.util.ArrayList;

public final class Distributor {
    private int id;
    private int contractLength;
    private double initialBudget;
    private double initialInfrastructureCost;
    private double initialProductionCost;
    private double profit;
    private double contractPrice;
    private double monthlyCost;
    private int numberOfContracts;
    private ArrayList<Consumer> contracts;
    private double budget;
    private boolean isBankrupt;
    private int lastTurnContracts;

    public Distributor(
            final int id,
            final int contractLength,
            final double initialBudget,
            final int initialInfrastructureCost,
            final int initialProductionCost) {
        this.id = id;
        this.contractLength = contractLength;
        this.initialBudget = initialBudget;
        this.initialInfrastructureCost = initialInfrastructureCost;
        this.initialProductionCost = initialProductionCost;
        this.profit = 0;
        this.contractPrice = 0;
        this.monthlyCost = 0;
        this.numberOfContracts = 0;
        this.budget = initialBudget;
        this.isBankrupt = false;
        this.contracts = new ArrayList<>();
        this.lastTurnContracts = 0;
    }

    public int getLastTurnContracts() {
        return lastTurnContracts;
    }

    public void setLastTurnContracts(final int lastTurnContracts) {
        this.lastTurnContracts = lastTurnContracts;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }

    public double getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final double initialBudget) {
        this.initialBudget = initialBudget;
    }

    public double getInitialInfrastructureCost() {
        return initialInfrastructureCost;
    }

    public void setInitialInfrastructureCost(final double initialInfrastructureCost) {
        this.initialInfrastructureCost = initialInfrastructureCost;
    }

    public double getInitialProductionCost() {
        return initialProductionCost;
    }

    public void setInitialProductionCost(final double initialProductionCost) {
        this.initialProductionCost = initialProductionCost;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(final double profit) {
        this.profit = profit;
    }

    public double getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(final double contractPrice) {
        this.contractPrice = contractPrice;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(final double monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public int getNumberOfContracts() {
        return numberOfContracts;
    }

    public void setNumberOfContracts(final int numberOfContracts) {
        this.numberOfContracts = numberOfContracts;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(final double budget) {
        this.budget = budget;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public ArrayList<Consumer> getContracts() {
        return contracts;
    }

    public void setContracts(final ArrayList<Consumer> contracts) {
        this.contracts = contracts;
    }
}
