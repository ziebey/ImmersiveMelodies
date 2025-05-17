package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.cobalt.network.Message;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public class NoteMessage extends Message {
    public final int entity;
    public final int tone;
    public final int velocity;

    public NoteMessage(int entity, int tone, int velocity) {
        this.entity = entity;
        this.tone = tone;
        this.velocity = velocity;
    }

    public NoteMessage(PacketByteBuf b) {
        this.entity = b.readInt();
        this.tone = b.readInt();
        this.velocity = b.readInt();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeInt(entity);
        b.writeInt(tone);
        b.writeInt(velocity);
    }

    @Override
    public void receive(PlayerEntity e) {
        Common.networkManager.handleNoteMessage(this);
    }
}