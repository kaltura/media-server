package utils;

/**
 * Created by asher.saban on 2/19/2015.
 */
public class MultiBitrateResults {

    private int tsNumber;
    private long minValue;
    private long maxValue;
    private int numComparisons;

    public MultiBitrateResults(int tsNumber) {
        this.tsNumber = tsNumber;
        this.maxValue = Long.MIN_VALUE;
        this.minValue = Long.MAX_VALUE;
        this.numComparisons = 0;
    }

    public int getTsNumber() {
        return tsNumber;
    }

    public int getNumComparisons() {
        return numComparisons;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void updateValues(long val) {
        System.out.println("ts: " + tsNumber + " val: " + val);
        numComparisons++;
        if (val > maxValue) {
            maxValue = val;
        }
        if (val < minValue) {
            minValue = val;
        }
    }
}
