package me.ceeveer.horsestonks.mixin;

import me.ceeveer.horsestonks.breeding.HorseBreedingHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public abstract class HorseChildAttributeMixin extends AnimalEntity {

    protected HorseChildAttributeMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private final HorseBreedingHandler breedingHandler = new HorseBreedingHandler();

    @Inject(method = "setChildAttributes", at = @At(value = "TAIL"))
    protected void onSetChildAttributes(PassiveEntity mate, AbstractHorseEntity child, CallbackInfo ci) {
        if (!(mate instanceof AbstractHorseEntity horseMate)) {
            return;
        }

        breedingHandler.applyBreedingAttributes((AbstractHorseEntity) (Object) this, horseMate, child);
    }
}
