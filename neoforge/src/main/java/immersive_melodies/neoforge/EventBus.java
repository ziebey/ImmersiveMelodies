package immersive_melodies.neoforge;

import immersive_melodies.Client;
import immersive_melodies.Common;
import immersive_melodies.resources.MelodyLoader;
import immersive_melodies.resources.ServerMelodyManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

@EventBusSubscriber(modid = Common.MOD_ID)
public class EventBus {
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ServerMelodyManager.server = event.getServer();
    }

    public static boolean firstLoad = true;

    @SubscribeEvent
    public static void onClientStart(ClientTickEvent.Pre event) {
        //forge decided to be funny and won't trigger the client load event
        if (firstLoad) {
            Client.postLoad();
            firstLoad = false;
        }
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new MelodyLoader());
    }
}
