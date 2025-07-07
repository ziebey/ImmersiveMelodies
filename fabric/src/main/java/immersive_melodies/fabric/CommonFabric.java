package immersive_melodies.fabric;

import immersive_melodies.*;
import immersive_melodies.fabric.cobalt.network.NetworkHandlerImpl;
import immersive_melodies.fabric.cobalt.registration.RegistrationImpl;
import immersive_melodies.fabric.resources.FabricMelody;
import immersive_melodies.resources.ServerMelodyManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.CreativeModeTab;

public final class CommonFabric implements ModInitializer {
    static {
        new NetworkHandlerImpl();
        new RegistrationImpl();
    }

    @Override
    public void onInitialize() {
        Items.bootstrap();
        Messages.bootstrap();
        Sounds.bootstrap();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricMelody());

        ServerLifecycleEvents.SERVER_STARTING.register(server -> ServerMelodyManager.server = server);

        CreativeModeTab group = FabricItemGroup.builder()
                .title(ItemGroups.getDisplayName())
                .icon(ItemGroups::getIcon)
                .displayItems((enabledFeatures, entries) -> entries.acceptAll(Items.getSortedItems()))
                .build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Common.locate("group"), group);
    }
}

