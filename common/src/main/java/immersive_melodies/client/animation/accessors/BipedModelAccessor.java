package immersive_melodies.client.animation.accessors;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

public class BipedModelAccessor<T extends LivingEntity> extends ArmsAndHeadAccessor<T> {
    private final HumanoidModel<?> model;
    private final T entity;

    public BipedModelAccessor(HumanoidModel<?> model, T entity) {
        super(entity, model.head, model.hat, model.leftArm, model.rightArm);
        this.model = model;
        this.entity = entity;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    public HumanoidModel<?> getModel() {
        return model;
    }
}
