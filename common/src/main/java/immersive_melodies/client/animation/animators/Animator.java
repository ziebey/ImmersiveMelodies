package immersive_melodies.client.animation.animators;

import immersive_melodies.client.MelodyProgress;
import immersive_melodies.client.animation.accessors.ModelAccessor;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public interface Animator {
    /**
     @deprecated Use set head and hand angles respectively, since not all entities are bipeds
     */
    @Deprecated(since = "2.0.1")
    default void setAngles(ModelPart left, ModelPart right, BipedEntityModel<?> model, LivingEntity entity, MelodyProgress progress, float time) {
        // no-op

    }

    default <T extends Entity> void setAngles(ModelAccessor<T> accessor, MelodyProgress progress, float time) {
        // no-op
    }
}
