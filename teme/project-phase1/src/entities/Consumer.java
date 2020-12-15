package entities;

public final class Consumer {
    private int id;
    private double initialBudget;
    private double budget;
    private double monthlyIncome;
    private double oldBill;
    private int distributorId;
    private boolean isBankrupt;
    private int contractLength;
    private double contractPrice;

    public Consumer(
            final int id,
            final int initialBudget,
            final int monthlyIncome) {
        this.id = id;
        this.initialBudget = initialBudget;
        this.monthlyIncome = monthlyIncome;
        this.oldBill = 0;
        this.budget = initialBudget;
        this.distributorId = -1;
        this.isBankrupt = false;
        this.contractLength = 0;
        this.contractPrice = 0;
    }

    public double getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(final double contractPrice) {
        this.contractPrice = contractPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public double getInitialBudget() {
        return initialBudget;
    }

    public void setInitialBudget(final double initialBudget) {
        this.initialBudget = initialBudget;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(final double budget) {
        this.budget = budget;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(final double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public double getOldBill() {
        return oldBill;
    }

    public void setOldBill(final double oldBill) {
        this.oldBill = oldBill;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(final int distributorId) {
        this.distributorId = distributorId;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(final boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public int getContractLength() {
        return contractLength;
    }

    public void setContractLength(final int contractLength) {
        this.contractLength = contractLength;
    }
}
