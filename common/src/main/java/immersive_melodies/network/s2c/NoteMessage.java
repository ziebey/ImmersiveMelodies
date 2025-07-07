package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.cobalt.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NoteMessage extends Message {
    public final int entity;
    public final int tone;
    public final int velocity;

    public NoteMessage(int entity, int tone, int velocity) {
        this.entity = entity;
        this.tone = tone;
        this.velocity = velocity;
    }

    public NoteMessage(FriendlyByteBuf b) {
        this.entity = b.readInt();
        this.tone = b.readInt();
        this.velocity = b.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(entity);
        b.writeInt(tone);
        b.writeInt(velocity);
    }

    @Override
    public void receive(Player e) {
        Common.networkManager.handleNoteMessage(this);
    }
}