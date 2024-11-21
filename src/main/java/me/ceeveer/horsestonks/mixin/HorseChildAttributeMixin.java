package me.ceeveer.horsestonks.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseChildAttributeMixin extends AnimalEntity {

    protected HorseChildAttributeMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "setChildAttributes", at = @At(value = "TAIL"))
    protected void onSetChildAttributes(PassiveEntity mate, AbstractHorseEntity child, CallbackInfo ci) {
        if (!(mate instanceof AbstractHorseEntity horseMate)) {
            return;
        }

        List<RegistryEntry<EntityAttribute>> attributes = Arrays.asList(
                EntityAttributes.MAX_HEALTH,
                EntityAttributes.JUMP_STRENGTH,
                EntityAttributes.MOVEMENT_SPEED
        );

        OutcomeType outcomeType = determineOutcomeType();

        if (outcomeType == OutcomeType.BAD_STEED) {
            // Randomly select one attribute to be the bad one
            RegistryEntry<EntityAttribute> badAttribute = attributes.get(this.random.nextInt(attributes.size()));
            for (RegistryEntry<EntityAttribute> attribute : attributes) {
                if (attribute.equals(badAttribute)) {
                    processBadAttribute(horseMate, child, attribute);
                } else {
                    processNormalAttribute(horseMate, child, attribute, 0);
                }
            }
        } else {
            double increasePercentage = outcomeType == OutcomeType.SUPER_STEED ? 0.4 : 0.2;
            for (RegistryEntry<EntityAttribute> attribute : attributes) {
                processNormalAttribute(horseMate, child, attribute, increasePercentage);
            }
        }
    }

    @Unique
    private OutcomeType determineOutcomeType() {
        double chance = this.random.nextDouble();
        if (chance < 0.05) {
            return OutcomeType.SUPER_STEED; // 5% chance
        } else if (chance < 0.95) {
            return OutcomeType.NORMAL_STEED; // 90% chance
        } else {
            return OutcomeType.BAD_STEED; // 5% chance
        }
    }

    @Unique
    private void processNormalAttribute(AbstractHorseEntity horseMate, AbstractHorseEntity child, RegistryEntry<EntityAttribute> attribute, double increasePercentage) {
        double parentValue1 = this.getAttributeBaseValue(attribute);
        double parentValue2 = horseMate.getAttributeBaseValue(attribute);

        // Take the best stat from the parents
        double bestValue = Math.max(parentValue1, parentValue2);

        // Increase by the specified percentage
        double increasedValue = bestValue * (1 + increasePercentage);

        setChildAttribute(child, attribute, increasedValue);
    }

    @Unique
    private void processBadAttribute(AbstractHorseEntity horseMate, AbstractHorseEntity child, RegistryEntry<EntityAttribute> attribute) {
        double parentValue1 = this.getAttributeBaseValue(attribute);
        double parentValue2 = horseMate.getAttributeBaseValue(attribute);

        // Take the lower stat from the parents
        double lowerValue = Math.min(parentValue1, parentValue2);

        // Increase by 20%
        double increasedValue = lowerValue * 1.2;

        setChildAttribute(child, attribute, increasedValue);
    }

    @Unique
    private void setChildAttribute(AbstractHorseEntity child, RegistryEntry<EntityAttribute> attribute, double value) {
        EntityAttributeInstance childAttr = child.getAttributeInstance(attribute);
        if (childAttr != null) {
            childAttr.setBaseValue(value);
        }
    }
}
