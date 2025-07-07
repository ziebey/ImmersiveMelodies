package immersive_melodies.mixin;

import immersive_melodies.client.animation.EntityModelAnimator;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinModel.class)
public abstract class PiglinEntityModelMixin<T extends Mob> extends PlayerModel<T> {
    public PiglinEntityModelMixin(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    @Inject(method = "setupAnim*", at = @At("HEAD"), cancellable = true)
    public void immersiveMelodies$injectSetAnim(T entity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (EntityModelAnimator.getInstrument(entity) != null) {
            super.setupAnim(entity, f, g, h, i, j);
            ci.cancel();
        }
    }
}
