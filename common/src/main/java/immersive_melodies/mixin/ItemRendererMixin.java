package immersive_melodies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import immersive_melodies.client.CustomInventoryModels;
import immersive_melodies.item.InstrumentItem;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Unique
    private BakedModel immersive_melodies$replaceModel;

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    void immersiveMelodies$injectGetModel(ItemStack stack, Level world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.getItem() instanceof InstrumentItem) {
            cir.setReturnValue(itemModelShaper.getModelManager().getModel(CustomInventoryModels.getHandIdentifier(stack.getItem())));
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At("HEAD"))
    void immersiveMelodies$modifyVariable(ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        boolean bl = renderMode == ItemDisplayContext.GUI || renderMode == ItemDisplayContext.GROUND || renderMode == ItemDisplayContext.FIXED;
        if (bl && stack.getItem() instanceof InstrumentItem) {
            immersive_melodies$replaceModel = itemModelShaper.getModelManager().getModel(CustomInventoryModels.getIdentifier(stack.getItem()));
        } else {
            immersive_melodies$replaceModel = null;
        }
    }

    @ModifyVariable(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    BakedModel immersiveMelodies$modifyVariable(BakedModel model) {
        return immersive_melodies$replaceModel == null ? model : immersive_melodies$replaceModel;
    }
}
