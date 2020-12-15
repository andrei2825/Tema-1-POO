package input;

public final class MonthlyUpdatesInputData {
    private NewConsumerInputData[] newConsumers;
    private  CostChangesInputData[] costsChanges;

    public NewConsumerInputData[] getNewConsumers() {
        return newConsumers;
    }

    public void setNewConsumers(final NewConsumerInputData[] newConsumers) {
        this.newConsumers = newConsumers;
    }

    public CostChangesInputData[] getCostsChanges() {
        return costsChanges;
    }

    public void setCostsChanges(final CostChangesInputData[] costsChanges) {
        this.costsChanges = costsChanges;
    }
}
