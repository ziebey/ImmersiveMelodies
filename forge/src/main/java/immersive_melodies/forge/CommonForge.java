package immersive_melodies.forge;

import immersive_melodies.Common;
import immersive_melodies.ItemGroups;
import immersive_melodies.Items;
import immersive_melodies.Sounds;
import immersive_melodies.network.ImmersivePayload;
import immersive_melodies.network.Network;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB;

@Mod(Common.MOD_ID)
@Mod.EventBusSubscriber(modid = Common.MOD_ID, bus = Bus.MOD)
public final class CommonForge {
    private static <T> void registerHelper(RegisterEvent event, Registry<T> register, Consumer<Common.RegisterHelper<T>> consumer) {
        event.register(register.key(), registry -> consumer.accept(registry::register));
    }

    public CommonForge() {
        DEF_REG.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        registerHelper(event, BuiltInRegistries.ITEM, Items::registerItems);
        registerHelper(event, BuiltInRegistries.SOUND_EVENT, Sounds::registerSounds);
    }

    public static final DeferredRegister<CreativeModeTab> DEF_REG = DeferredRegister.create(CREATIVE_MODE_TAB, Common.MOD_ID);

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> TAB = DEF_REG.register(Common.MOD_ID, () -> CreativeModeTab.builder()
            .title(ItemGroups.getDisplayName())
            .icon(ItemGroups::getIcon)
            .displayItems((featureFlags, output) -> output.acceptAll(Items.getSortedItems()))
            .build()
    );

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Common.locate("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 0;

    static class ForgeRegistrar implements Network.Registrar {
        @Override
        public <T extends ImmersivePayload> void register(Class<T> msg, Function<FriendlyByteBuf, T> constructor) {
            INSTANCE.registerMessage(
                    id++,
                    msg,
                    ImmersivePayload::encode,
                    constructor,
                    (m, ctx) -> {
                        ctx.get().enqueueWork(() -> m.handle(ctx.get().getSender()));
                        ctx.get().setPacketHandled(true);
                    }
            );
        }
    }

    static {
        Network.register(new ForgeRegistrar());
        Network.registerSender((payload, player) -> INSTANCE.sendTo(payload, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
        Network.registerClientSender(INSTANCE::sendToServer);
    }
}
