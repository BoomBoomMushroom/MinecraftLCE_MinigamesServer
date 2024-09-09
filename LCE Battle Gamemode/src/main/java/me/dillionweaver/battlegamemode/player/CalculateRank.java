package me.dillionweaver.battlegamemode.player;

public class CalculateRank {

    public static final int PERFECT_DAMAGE = 0;
    public static final int DEATH_DAMAGE = 3;

    public static String getRank(int damageTaken, long timeTakenMs, long AVERAGE_TIME_MS) {
        double secondsAfterAverage = Math.ceil((timeTakenMs - AVERAGE_TIME_MS) / 1000);
        float weightedDamage = (float) (damageTaken + (timeTakenMs < AVERAGE_TIME_MS ? 0 : secondsAfterAverage / 5f));

        if (weightedDamage == PERFECT_DAMAGE) {
            return "S";
        } else if (weightedDamage <= 5) {
            return "A";
        } else if (weightedDamage <= 10) {
            return "B";
        } else if (weightedDamage <= 15) {
            return "C";
        } else if (weightedDamage <= 20) {
            return "D";
        } else if (weightedDamage <= 25) {
            return "E";
        } else {
            return "F";
        }
    }
}
