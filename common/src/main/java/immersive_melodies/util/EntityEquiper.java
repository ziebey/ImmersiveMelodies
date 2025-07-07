package immersive_melodies.util;

import immersive_melodies.Config;
import immersive_melodies.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EntityEquiper {
    public static void equip(Entity entity) {
        if (shouldEquip(entity)) {
            Item item = Items.items.get(entity.level().getRandom().nextInt(Items.items.size())).get();
            entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(item));
            if (entity instanceof Mob mobEntity) {
                mobEntity.setDropChance(EquipmentSlot.MAINHAND, Config.getInstance().mobInstrumentDropFactor);
            }
        }
    }

    public static boolean shouldEquip(Entity entity) {
        String id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        return Config.getInstance().mobInstrumentFactors.containsKey(id) && entity.level().getRandom().nextFloat() < Config.getInstance().mobInstrumentFactors.get(id);
    }

    public static boolean canPickUp(Entity entity) {
        String id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
        return Config.getInstance().mobInstrumentFactors.containsKey(id) && Config.getInstance().mobInstrumentFactors.get(id) > 0;
    }
}
