package immersive_melodies;

import immersive_melodies.client.sound.CancelableSoundInstance;
import immersive_melodies.client.sound.SoundManagerImpl;
import immersive_melodies.cobalt.network.NetworkHandler;
import immersive_melodies.item.InstrumentItem;
import immersive_melodies.network.ClientNetworkManager;
import immersive_melodies.network.c2s.ItemActionMessage;
import immersive_melodies.resources.Note;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Client {
    public static void postLoad() {
        MinecraftClient client = MinecraftClient.getInstance();
        Common.networkManager = new ClientNetworkManager();
        Common.soundManager = new SoundManagerImpl(client);

        MidiListener.launch();
    }

    private static final Map<Integer, CancelableSoundInstance> playingSounds = new ConcurrentHashMap<>();

    public static void playNote(int tone, int velocity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.world != null && !client.isPaused()) {
            for (ItemStack stack : client.player.getItemsEquipped()) {
                if (stack.getItem() instanceof InstrumentItem instrument) {
                    NetworkHandler.sendToServer(new ItemActionMessage(ItemActionMessage.State.PAUSE));

                    if (velocity > 0) {
                        if (!playingSounds.containsKey(tone)) {
                            Note note = new Note(tone, velocity, 0, 10_000, 200);
                            CancelableSoundInstance soundInstance = instrument.playNote(client.player, note, 0);
                            playingSounds.put(tone, soundInstance);
                            // TODO: Also send to server
                        }
                    } else {
                        CancelableSoundInstance soundInstance = playingSounds.get(tone);
                        if (soundInstance != null) {
                            soundInstance.stop();
                            playingSounds.remove(tone);
                            // TODO: Also send to server
                        }
                    }
                    break;
                }
            }
        }
    }
}
