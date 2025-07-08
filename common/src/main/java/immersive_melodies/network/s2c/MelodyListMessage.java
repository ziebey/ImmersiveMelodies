package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.Config;
import immersive_melodies.network.ImmersivePayload;
import immersive_melodies.resources.MelodyDescriptor;
import immersive_melodies.resources.MelodyLoader;
import immersive_melodies.resources.ServerMelodyManager;
import immersive_melodies.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public record MelodyListMessage(Map<ResourceLocation, MelodyDescriptor> melodies) implements ImmersivePayload {
    public MelodyListMessage(Player receiver) {
        this(createMelodiesMap(receiver));
    }

    private static Map<ResourceLocation, MelodyDescriptor> createMelodiesMap(Player receiver) {
        Map<ResourceLocation, MelodyDescriptor> melodies = new HashMap<>();
        //datapack melodies
        for (Map.Entry<ResourceLocation, MelodyLoader.LazyMelody> lazyMelodyEntry : ServerMelodyManager.getDatapackMelodies().entrySet()) {
            melodies.put(lazyMelodyEntry.getKey(), lazyMelodyEntry.getValue().getDescriptor());
        }

        //custom melodies
        if (Config.getInstance().showOtherPlayersMelodies) {
            melodies.putAll(ServerMelodyManager.getIndex().getMelodies());
        } else {
            ServerMelodyManager.getIndex().getMelodies().forEach((id, desc) -> {
                if (Utils.ownsMelody(id, receiver)) {
                    melodies.put(id, desc);
                }
            });
        }
        return melodies;
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
        this(readMelodies(b));
    }

    private static Map<ResourceLocation, MelodyDescriptor> readMelodies(FriendlyByteBuf b) {
        int size = b.readInt();
        Map<ResourceLocation, MelodyDescriptor> melodies = new HashMap<>();
        for (int i = 0; i < size; i++) {
            melodies.put(b.readResourceLocation(), new MelodyDescriptor(b));
        }
        return melodies;
    }

    @Override
    public void handle(Player e) {
        Common.networkManager.handleMelodyListMessage(this);
    }
}
