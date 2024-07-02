package immersive_melodies.client.animation;

import immersive_melodies.client.MelodyProgress;
import immersive_melodies.client.MelodyProgressManager;
import immersive_melodies.client.animation.accessors.BipedModelAccessor;
import immersive_melodies.client.animation.accessors.ModelAccessor;
import immersive_melodies.item.InstrumentItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class EntityModelAnimator {
    public static Item getInstrument(Entity entity) {
        for (ItemStack handItem : entity.getHandItems()) {
            if (handItem.getItem() instanceof InstrumentItem) {
                return handItem.getItem();
            }
        }
        return null;
    }

    private static boolean isInMainHand(LivingEntity entity) {
        return entity.getMainHandStack().getItem() instanceof InstrumentItem;
    }

    @Deprecated
    public static ModelPart getLeftArm(BipedEntityModel<?> model, LivingEntity entity) {
        return isInMainHand(entity) ? model.leftArm : model.rightArm;
    }

    @Deprecated
    public static ModelPart getRightArm(BipedEntityModel<?> model, LivingEntity entity) {
        return isInMainHand(entity) ? model.rightArm : model.leftArm;
    }

    public static <T extends Entity> void setAngles(ModelAccessor<T> accessor) {
        T entity = accessor.getEntity();
        Item item = getInstrument(entity);
        if (item != null) {
            float time = (MinecraftClient.getInstance().isPaused() ? 0.0f : MinecraftClient.getInstance().getTickDelta()) + entity.age;

            MelodyProgress progress = MelodyProgressManager.INSTANCE.getProgress(entity);
            progress.visualTick(time);

            // todo @deprecated, remove in 0.3.0
            if (accessor instanceof BipedModelAccessor<?> bipedModelAccessor && entity instanceof LivingEntity livingEntity) {
                BipedEntityModel<?> bipedModel = bipedModelAccessor.getModel();
                ModelPart left = getLeftArm(bipedModel, livingEntity);
                ModelPart right = getRightArm(bipedModel, livingEntity);
                ItemAnimators.get(Registries.ITEM.getId(item)).setAngles(left, right, bipedModel, livingEntity, progress, time);
            }

            // Apply animations
            ItemAnimators.get(Registries.ITEM.getId(item)).setAngles(accessor, progress, time);
        }
    }
}
