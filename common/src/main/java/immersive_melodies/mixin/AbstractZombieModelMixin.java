package immersive_melodies.mixin;

import immersive_melodies.client.animation.EntityModelAnimator;
import net.minecraft.client.model.AbstractZombieModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class AbstractZombieModelMixin<T extends Monster> extends HumanoidModel<T> {
    public AbstractZombieModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim*", at = @At("HEAD"), cancellable = true)
    public void immersiveMelodies$injectSetupAnim(T entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (EntityModelAnimator.getInstrument(entity) != null) {
            super.setupAnim(entity, f, g, h, i, j);
            ci.cancel();
        }
    }
}
