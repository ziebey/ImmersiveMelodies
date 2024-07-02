package immersive_melodies.mixin;

import immersive_melodies.item.InstrumentItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PillagerEntity.class)
public abstract class PillagerEntityMixin extends IllagerEntity  {
    protected PillagerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "loot(Lnet/minecraft/entity/ItemEntity;)V", at = @At("HEAD"), cancellable = true)
    private void immersiveMelodies$injectLoot(ItemEntity item, CallbackInfo ci) {
        if (item.getStack().getItem() instanceof InstrumentItem) {
            super.loot(item);
            ci.cancel();
        }
    }
}
