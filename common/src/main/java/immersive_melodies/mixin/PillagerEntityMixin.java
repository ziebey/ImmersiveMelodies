package immersive_melodies.mixin;

import immersive_melodies.item.InstrumentItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Pillager.class)
public abstract class PillagerEntityMixin extends AbstractIllager {
    protected PillagerEntityMixin(EntityType<? extends AbstractIllager> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "pickUpItem", at = @At("HEAD"), cancellable = true)
    private void immersiveMelodies$injectLoot(ItemEntity item, CallbackInfo ci) {
        if (item.getItem().getItem() instanceof InstrumentItem) {
            super.pickUpItem(item);
            ci.cancel();
        }
    }
}
