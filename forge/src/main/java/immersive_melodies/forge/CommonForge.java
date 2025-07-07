package immersive_melodies.forge;

import immersive_melodies.*;
import immersive_melodies.forge.cobalt.network.NetworkHandlerImpl;
import immersive_melodies.forge.cobalt.registration.RegistrationImpl;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

@Mod(Common.MOD_ID)
@Mod.EventBusSubscriber(modid = Common.MOD_ID, bus = Bus.MOD)
public final class CommonForge {
    static {
        RegistrationImpl.bootstrap();
        new NetworkHandlerImpl();
    }

    public CommonForge() {
        Items.bootstrap();
        Messages.bootstrap();
        Sounds.bootstrap();

        DEF_REG.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final DeferredRegister<CreativeModeTab> DEF_REG = DeferredRegister.create(CREATIVE_MODE_TAB, Common.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> TAB = DEF_REG.register(Common.MOD_ID, () -> CreativeModeTab.builder()
            .title(ItemGroups.getDisplayName())
            .icon(ItemGroups::getIcon)
            .displayItems((featureFlags, output) -> output.acceptAll(Items.getSortedItems()))
            .build()
    );
}
