package immersive_melodies.mixin;

import immersive_melodies.client.animation.EntityModelAnimator;
import immersive_melodies.client.animation.accessors.ArmsAndHeadAccessor;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.entity.mob.IllagerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerEntityModel.class)
public class IllagerEntityModelMixin<T extends IllagerEntity> {
    @Shadow @Final private ModelPart head;

    @Shadow @Final private ModelPart hat;

    @Shadow @Final private ModelPart leftArm;

    @Shadow @Final private ModelPart rightArm;

    @Inject(method = "setAngles(Lnet/minecraft/entity/mob/IllagerEntity;FFFFF)V", at = @At("TAIL"))
    public void immersiveMelodies$injectSetAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        EntityModelAnimator.setAngles(new ArmsAndHeadAccessor<>(entity, head, hat, leftArm, rightArm));
    }
}
