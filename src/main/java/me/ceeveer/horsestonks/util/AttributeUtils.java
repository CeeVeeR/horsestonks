package me.ceeveer.horsestonks.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AttributeUtils {
    public static final List<RegistryEntry<EntityAttribute>> HORSE_ATTRIBUTES = Arrays.asList(
            EntityAttributes.MAX_HEALTH,
            EntityAttributes.JUMP_STRENGTH,
            EntityAttributes.MOVEMENT_SPEED
    );

    private static final Random RANDOM = new Random();

    public static double getRandomGrowth(RegistryEntry<EntityAttribute> attributeEntry) {
        EntityAttribute attribute = attributeEntry.value();

        if (attribute.equals(EntityAttributes.MAX_HEALTH.value())) {
            return 1 + (2.5 + (5 - 2.5) * RANDOM.nextDouble()) / 100.0; // 2.5% - 5%
        } else if (attribute.equals(EntityAttributes.JUMP_STRENGTH.value())) {
            return 1 + (1 + (7.9 - 1) * RANDOM.nextDouble()) / 100.0; // 1% - 7.9%
        } else if (attribute.equals(EntityAttributes.MOVEMENT_SPEED.value())) {
            return 1 + (5 + (15 - 5) * RANDOM.nextDouble()) / 100.0; // 5% - 15%
        } else {
            return 1.0;
        }
    }
}
