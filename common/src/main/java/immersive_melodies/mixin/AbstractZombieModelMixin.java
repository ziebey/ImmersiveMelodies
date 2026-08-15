package immersive_melodies.mixin;

import immersive_melodies.client.RenderStateEntityCache;
import immersive_melodies.client.animation.EntityModelAnimator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.zombie.AbstractZombieModel;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractZombieModel.class)
public abstract class AbstractZombieModelMixin extends HumanoidModel<ZombieRenderState> {
    public AbstractZombieModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)V", at = @At("HEAD"), cancellable = true)
    public void immersiveMelodies$injectSetupAnim(ZombieRenderState renderState, CallbackInfo ci) {
        if (RenderStateEntityCache.get(renderState) instanceof LivingEntity entity && EntityModelAnimator.getInstrument(entity) != null) {
            super.setupAnim(renderState);
            ci.cancel();
        }
    }
}
