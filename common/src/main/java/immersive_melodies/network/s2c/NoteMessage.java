package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.network.ImmersivePayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record NoteMessage(int entity, int tone, int velocity) implements ImmersivePayload {
    public NoteMessage(FriendlyByteBuf b) {
        this(b.readInt(), b.readInt(), b.readInt());
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(entity);
        b.writeInt(tone);
        b.writeInt(velocity);
    }

    @Override
    public void handle(Player e) {
        Common.networkManager.handleNoteMessage(this);
    }
}