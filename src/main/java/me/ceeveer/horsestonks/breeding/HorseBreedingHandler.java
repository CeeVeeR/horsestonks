package me.ceeveer.horsestonks.breeding;

import me.ceeveer.horsestonks.util.AttributeUtils;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Random;

public class HorseBreedingHandler {
    private final Random random = new Random();

    public void applyBreedingAttributes(AbstractHorseEntity parent1, AbstractHorseEntity parent2, AbstractHorseEntity child) {
        BreedingOutcome outcome = determineOutcome();

        List<RegistryEntry<EntityAttribute>> attributes = AttributeUtils.HORSE_ATTRIBUTES;

        RegistryEntry<EntityAttribute> badAttribute = null;
        if (outcome == BreedingOutcome.BAD_STEED) {
            // Randomly select one attribute to be the bad one
            badAttribute = attributes.get(random.nextInt(attributes.size()));
        }

        for (RegistryEntry<EntityAttribute> attributeEntry : attributes) {
            double newValue;

            if (attributeEntry.equals(badAttribute)) {
                // For the bad attribute, take the worst parent stat
                double baseValue = getWorstParentValue(parent1, parent2, attributeEntry);
                newValue = applyGrowthAndOutcome(baseValue, attributeEntry, outcome);
            } else {
                // For other attributes, take the best parent stat
                double baseValue = getBestParentValue(parent1, parent2, attributeEntry);
                newValue = applyGrowthAndOutcome(baseValue, attributeEntry, outcome);
            }

            setChildAttribute(child, attributeEntry, newValue);
        }
    }

    private BreedingOutcome determineOutcome() {
        double chance = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (BreedingOutcome outcome : BreedingOutcome.values()) {
            cumulativeProbability += outcome.getChance();
            if (chance <= cumulativeProbability) {
                return outcome;
            }
        }
        return BreedingOutcome.NORMAL_STEED;
    }

    private double getBestParentValue(AbstractHorseEntity parent1, AbstractHorseEntity parent2, RegistryEntry<EntityAttribute> attributeEntry) {
        double parentValue1 = parent1.getAttributeBaseValue(attributeEntry);
        double parentValue2 = parent2.getAttributeBaseValue(attributeEntry);
        return Math.max(parentValue1, parentValue2);
    }

    private double getWorstParentValue(AbstractHorseEntity parent1, AbstractHorseEntity parent2, RegistryEntry<EntityAttribute> attributeEntry) {
        double parentValue1 = parent1.getAttributeBaseValue(attributeEntry);
        double parentValue2 = parent2.getAttributeBaseValue(attributeEntry);
        return Math.min(parentValue1, parentValue2);
    }

    private double applyGrowthAndOutcome(double baseValue, RegistryEntry<EntityAttribute> attributeEntry, BreedingOutcome outcome) {
        // Apply random growth within specified range
        double growthMultiplier = AttributeUtils.getRandomGrowth(attributeEntry);

        // Apply outcome effect multiplier
        double outcomeMultiplier = outcome.getEffectMultiplier();

        return baseValue * growthMultiplier * outcomeMultiplier;
    }

    private void setChildAttribute(AbstractHorseEntity child, RegistryEntry<EntityAttribute> attributeEntry, double value) {
        EntityAttributeInstance childAttr = child.getAttributeInstance(attributeEntry);
        if (childAttr != null) {
            childAttr.setBaseValue(value);
        }
    }
}
