package immersive_melodies.client.animation.accessors;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class BipedModelAccessor<T extends LivingEntity> extends ArmsAndHeadAccessor<T> {
    private final BipedEntityModel<T> model;
    private final T entity;

    public BipedModelAccessor(BipedEntityModel<T> model, T entity) {
        super(entity, model.head, model.hat, model.leftArm, model.rightArm);
        this.model = model;
        this.entity = entity;
    }

    @Override
    public T getEntity() {
        return entity;
    }

    public BipedEntityModel<T> getModel() {
        return model;
    }
}
