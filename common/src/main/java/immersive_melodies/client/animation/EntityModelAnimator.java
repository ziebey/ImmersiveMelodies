package immersive_melodies.client.animation;

import immersive_melodies.client.MelodyProgress;
import immersive_melodies.client.MelodyProgressManager;
import immersive_melodies.client.animation.accessors.ModelAccessor;
import immersive_melodies.item.InstrumentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EntityModelAnimator {
    public static Item getInstrument(LivingEntity entity) {
        for (ItemStack handItem : new ItemStack[]{entity.getItemBySlot(EquipmentSlot.MAINHAND), entity.getItemBySlot(EquipmentSlot.OFFHAND)}) {
            if (handItem.getItem() instanceof InstrumentItem instrument && instrument.isPlaying(handItem)) {
                return handItem.getItem();
            }
        }

        for (ItemStack handItem : new ItemStack[]{entity.getItemBySlot(EquipmentSlot.MAINHAND), entity.getItemBySlot(EquipmentSlot.OFFHAND)}) {
            if (handItem.getItem() instanceof InstrumentItem) {
                return handItem.getItem();
            }
        }

        return null;
    }

    public static <T extends LivingEntity> void setAngles(ModelAccessor<T> accessor) {
        T entity = accessor.getEntity();
        Item item = getInstrument(entity);
        if (item != null) {
            float time = (Minecraft.getInstance().isPaused() ? 0.0f : Minecraft.getInstance().getFrameTimeNs() / 1_000_000_000.0f) + entity.tickCount;

            MelodyProgress progress = MelodyProgressManager.INSTANCE.getProgress(entity);
            progress.visualTick(time);

            // Apply animations
            ItemAnimators.get(BuiltInRegistries.ITEM.getKey(item)).setAngles(accessor, progress, time);
        }
    }
}
