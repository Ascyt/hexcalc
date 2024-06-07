package at.htlleonding.hexcalc.controller;

public class HistoryItem {
    public final String expression;
    public final String evaluation;

    public HistoryItem(String expression, String evaluation) {
        this.expression = expression;
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return evaluation;
    }
}
