package immersive_melodies.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class CustomInventoryModels {
    private static final Map<Item, ModelResourceLocation> handIdentifierCache = new HashMap<>();
    private static final Map<Item, ModelResourceLocation> identifierCache = new HashMap<>();

    public static ModelResourceLocation computeHandIdentifier(ResourceLocation id) {
        return new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_hand"), "inventory");
    }

    public static ModelResourceLocation computeIdentifier(ResourceLocation id) {
        return new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath()), "inventory");
    }

    public static ModelResourceLocation computeHandIdentifier(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return computeHandIdentifier(id);
    }

    public static ModelResourceLocation computeIdentifier(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return computeIdentifier(id);
    }

    public static ModelResourceLocation getHandIdentifier(Item item) {
        return handIdentifierCache.computeIfAbsent(item, CustomInventoryModels::computeHandIdentifier);
    }

    public static ModelResourceLocation getIdentifier(Item item) {
        return identifierCache.computeIfAbsent(item, CustomInventoryModels::computeIdentifier);
    }
}
