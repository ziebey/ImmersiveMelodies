package immersive_melodies.mixin;

import immersive_melodies.Config;
import immersive_melodies.item.InstrumentItem;
import immersive_melodies.util.EntityEquiper;
import immersive_melodies.util.Utils;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow
    protected abstract Vec3i getPickupReach();

    @Shadow
    protected abstract void pickUpItem(ServerLevel world, ItemEntity item);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void immersiveMelodies$injectInteract(Player player, InteractionHand hand, Vec3 pos, CallbackInfoReturnable<InteractionResult> cir) {
        // Mob.interact is also invoked on the client for interaction prediction; only act on the server
        if (this.level().isClientSide()) {
            return;
        }

        if (!Utils.hasCommandLevel(player, Config.getInstance().rightClickToDropEntityInstrumentPermissionLevel)) {
            return;
        }

        for (ItemStack handItem : new ItemStack[]{this.getItemBySlot(EquipmentSlot.MAINHAND), this.getItemBySlot(EquipmentSlot.OFFHAND)}) {
            if (handItem.getItem() instanceof InstrumentItem) {
                ItemEntity itemEntity = spawnAtLocation((ServerLevel) this.level(), handItem.copyAndClear());
                if (itemEntity != null) {
                    itemEntity.setThrower(this);
                }
                cir.setReturnValue(InteractionResult.CONSUME);
                break;
            }
        }
    }

    @Inject(method = "baseTick()V", at = @At("TAIL"))
    private void immersiveMelodies$injectBaseTick(CallbackInfo ci) {
        if (Config.getInstance().forceMobsToPickUp && EntityEquiper.canPickUp(this) && !this.level().isClientSide() && this.isAlive() && !this.dead) {
            Vec3i vec3i = this.getPickupReach();
            for (ItemEntity itementity : this.level().getEntities(EntityTypeTest.forClass(ItemEntity.class), this.getBoundingBox().inflate(vec3i.getX(), vec3i.getY(), vec3i.getZ()), item -> true)) {
                if ((itementity.getOwner() == null || !itementity.getOwner().getUUID().equals(getUUID())) && !itementity.isRemoved() && !itementity.getItem().isEmpty() && itementity.getItem().getItem() instanceof InstrumentItem) {
                    pickUpItem((ServerLevel) this.level(), itementity);
                }
            }
        }
    }

    @Inject(method = "canReplaceCurrentItem", at = @At("HEAD"), cancellable = true)
    private void immersiveMelodies$injectCanReplaceCurrentItem(ItemStack newStack, ItemStack oldStack, EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if (newStack.getItem() instanceof InstrumentItem && !(oldStack.getItem() instanceof InstrumentItem)) {
            cir.setReturnValue(true);
        } else if (oldStack.getItem() instanceof InstrumentItem) {
            cir.setReturnValue(false);
        }
    }
}
