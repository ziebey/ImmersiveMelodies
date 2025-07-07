package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.Config;
import immersive_melodies.cobalt.network.Message;
import immersive_melodies.resources.MelodyDescriptor;
import immersive_melodies.resources.MelodyLoader;
import immersive_melodies.resources.ServerMelodyManager;
import immersive_melodies.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class MelodyListMessage extends Message {
    private final Map<ResourceLocation, MelodyDescriptor> melodies = new HashMap<>();

    public MelodyListMessage(Player receiver) {
        //datapack melodies
        for (Map.Entry<ResourceLocation, MelodyLoader.LazyMelody> lazyMelodyEntry : ServerMelodyManager.getDatapackMelodies().entrySet()) {
            melodies.put(lazyMelodyEntry.getKey(), lazyMelodyEntry.getValue().getDescriptor());
        }

        //custom melodies
        if (Config.getInstance().showOtherPlayersMelodies) {
            this.melodies.putAll(ServerMelodyManager.getIndex().getMelodies());
        } else {
            ServerMelodyManager.getIndex().getMelodies().forEach((id, desc) -> {
                if (Utils.ownsMelody(id, receiver)) {
                    this.melodies.put(id, desc);
                }
            });
        }
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(melodies.size());
        for (Map.Entry<ResourceLocation, MelodyDescriptor> entry : melodies.entrySet()) {
            b.writeResourceLocation(entry.getKey());
            entry.getValue().encodeLite(b);
        }
    }

    public MelodyListMessage(FriendlyByteBuf b) {
        int size = b.readInt();
        for (int i = 0; i < size; i++) {
            melodies.put(b.readResourceLocation(), new MelodyDescriptor(b));
        }
    }

    @Override
    public void receive(Player e) {
        Common.networkManager.handleMelodyListMessage(this);
    }

    public Map<ResourceLocation, MelodyDescriptor> getMelodies() {
        return melodies;
    }
}
