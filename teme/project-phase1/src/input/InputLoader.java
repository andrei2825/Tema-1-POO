package input;

public final class InputLoader {

    private int numberOfTurns;
    private InitialData initialData;
    private MonthlyUpdatesInputData[] monthlyUpdates;

    public int getNumberOfTurns() {
        return numberOfTurns;
    }

    public void setNumberOfTurns(final int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }

    public InitialData getInitialData() {
        return initialData;
    }

    public void setInitialData(final InitialData initialData) {
        this.initialData = initialData;
    }

    public MonthlyUpdatesInputData[] getMonthlyUpdates() {
        return monthlyUpdates;
    }

    public void setMonthlyUpdates(final MonthlyUpdatesInputData[] monthlyUpdates) {
        this.monthlyUpdates = monthlyUpdates;
    }

}
