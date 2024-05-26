package immersive_melodies.network.c2s;

import immersive_melodies.cobalt.network.Message;
import immersive_melodies.item.InstrumentItem;
import immersive_melodies.resources.ServerMelodyManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class TrackToggleMessage extends Message {
    private final Identifier melody;
    private final int track;
    private final boolean enabled;

    public TrackToggleMessage(Identifier melody, int track, boolean enabled) {
        this.melody = melody;
        this.track = track;
        this.enabled = enabled;
    }

    public TrackToggleMessage(PacketByteBuf b) {
        melody = b.readIdentifier();
        track = b.readInt();
        enabled = b.readBoolean();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeIdentifier(melody);
        b.writeInt(track);
        b.writeBoolean(enabled);
    }

    @Override
    public void receive(PlayerEntity e) {
        if (enabled) {
            ServerMelodyManager.getSettings().enableTrack(melody, e.getUuid(), track);
        } else {
            ServerMelodyManager.getSettings().disableTrack(melody, e.getUuid(), track);
        }

        e.getHandItems().forEach(stack -> {
            if (stack.getItem() instanceof InstrumentItem item) {
                item.refreshTracks(stack, e);
            }
        });
    }
}
