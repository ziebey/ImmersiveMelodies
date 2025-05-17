package immersive_melodies.network.c2s;

import immersive_melodies.cobalt.network.Message;
import immersive_melodies.cobalt.network.NetworkHandler;
import immersive_melodies.network.s2c.NoteMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class NoteBroadcastRequest extends Message {
    public final int tone;
    public final int velocity;

    public NoteBroadcastRequest(int tone, int velocity) {
        this.tone = tone;
        this.velocity = velocity;
    }

    public NoteBroadcastRequest(PacketByteBuf b) {
        this.tone = b.readInt();
        this.velocity = b.readInt();
    }

    @Override
    public void encode(PacketByteBuf b) {
        b.writeInt(tone);
        b.writeInt(velocity);
    }

    @Override
    public void receive(PlayerEntity e) {
        if (e instanceof ServerPlayerEntity se) {
            se.getServerWorld().getPlayers().stream()
                    .filter(player -> player != e && player.squaredDistanceTo(e) < 64)
                    .forEach(player -> {
                        NetworkHandler.sendToPlayer(new NoteMessage(player.getId(), tone, velocity), player);
                    });
        }
    }
}