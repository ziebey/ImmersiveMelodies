package immersive_melodies.network.c2s;

import immersive_melodies.item.InstrumentItem;
import immersive_melodies.network.ImmersivePayload;
import immersive_melodies.resources.ServerMelodyManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class TrackToggleMessage implements ImmersivePayload {
    private final ResourceLocation melody;
    private final int track;
    private final boolean enabled;

    public TrackToggleMessage(ResourceLocation melody, int track, boolean enabled) {
        this.melody = melody;
        this.track = track;
        this.enabled = enabled;
    }

    public TrackToggleMessage(FriendlyByteBuf b) {
        melody = b.readResourceLocation();
        track = b.readInt();
        enabled = b.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeResourceLocation(melody);
        b.writeInt(track);
        b.writeBoolean(enabled);
    }

    @Override
    public void handle(Player e) {
        e.getHandSlots().forEach(stack -> {
            if (stack.getItem() instanceof InstrumentItem item) {
                ServerMelodyManager.MelodyTrackSettings settings = ServerMelodyManager.getSettings();
                String identifier = ServerMelodyManager.getIdentifier(e, item);
                if (enabled) {
                    settings.enableTrack(melody, identifier, track);
                } else {
                    settings.disableTrack(melody, identifier, track);
                }

                item.refreshTracks(stack, e);
            }
        });
    }
}
