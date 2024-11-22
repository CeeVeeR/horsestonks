package me.ceeveer.horsestonks.breeding;

public enum BreedingOutcome {
    BAD_STEED(0.10, 1.00),
    NORMAL_STEED(0.75, 1.02),
    GOOD_STEED(0.10, 1.05),
    SUPER_STEED(0.05, 1.10);

    private final double chance;
    private final double effectMultiplier;

    BreedingOutcome(double chance, double effectMultiplier) {
        this.chance = chance;
        this.effectMultiplier = effectMultiplier;
    }

    public double getChance() {
        return chance;
    }

    public double getEffectMultiplier() {
        return effectMultiplier;
    }
}
