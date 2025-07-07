package immersive_melodies.mixin;

import immersive_melodies.Items;
import immersive_melodies.client.CustomInventoryModels;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelBakery.class)
public abstract class ModelLoaderMixin {
    @Shadow
    protected abstract void loadTopLevel(ModelResourceLocation modelId);

    @Inject(method = "loadTopLevel", at = @At("HEAD"))
    void immersiveMelodies$injectModelLoaderInit(ModelResourceLocation modelId, CallbackInfo ci) {
        if (modelId == ItemRenderer.SPYGLASS_IN_HAND_MODEL) {
            for (ResourceLocation identifier : Items.customInventoryModels) {
                ModelResourceLocation modelIdentifier = CustomInventoryModels.computeHandIdentifier(identifier);
                loadTopLevel(modelIdentifier);
            }
        }
    }
}
