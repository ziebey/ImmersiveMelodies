package immersive_melodies;

import immersive_melodies.client.sound.CancelableSoundInstance;
import immersive_melodies.client.sound.SoundManagerImpl;
import immersive_melodies.cobalt.network.NetworkHandler;
import immersive_melodies.item.InstrumentItem;
import immersive_melodies.network.ClientNetworkManager;
import immersive_melodies.network.c2s.ItemActionMessage;
import immersive_melodies.network.c2s.NoteBroadcastRequest;
import immersive_melodies.resources.Note;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
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
        if (client.player != null && !client.isPaused()) {
            if (playNote(client.player, tone, velocity)) {
                NetworkHandler.sendToServer(new NoteBroadcastRequest(tone, velocity));
            }
        }
    }

    public static boolean playNote(Entity entity, int tone, int velocity) {
        for (ItemStack stack : entity.getItemsEquipped()) {
            if (stack.getItem() instanceof InstrumentItem instrument) {
                if (entity instanceof ClientPlayerEntity && instrument.isPlaying(stack)) {
                    NetworkHandler.sendToServer(new ItemActionMessage(ItemActionMessage.State.PAUSE));
                }

                if (velocity > 0) {
                    if (!playingSounds.containsKey(tone)) {
                        Note note = new Note(tone, velocity, 0, 10_000, 200);
                        CancelableSoundInstance soundInstance = instrument.playNote(entity, note, 0);
                        playingSounds.put(tone, soundInstance);
                        return true;
                    }
                } else {
                    CancelableSoundInstance soundInstance = playingSounds.get(tone);
                    if (soundInstance != null) {
                        soundInstance.stop();
                        playingSounds.remove(tone);
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }
}
