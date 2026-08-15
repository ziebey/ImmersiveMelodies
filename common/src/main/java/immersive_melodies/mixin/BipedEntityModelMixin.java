package immersive_melodies.mixin;

import immersive_melodies.client.RenderStateEntityCache;
import immersive_melodies.client.animation.EntityModelAnimator;
import immersive_melodies.client.animation.accessors.BipedModelAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class BipedEntityModelMixin {
    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    public void immersiveMelodies$injectSetupAnim(HumanoidRenderState renderState, CallbackInfo ci) {
        if (RenderStateEntityCache.get(renderState) instanceof LivingEntity entity) {
            //noinspection unchecked
            EntityModelAnimator.setAngles(new BipedModelAccessor<>((HumanoidModel<?>) (Object) this, entity));
        }
    }
}
