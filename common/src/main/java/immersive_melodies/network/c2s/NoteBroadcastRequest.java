package immersive_melodies.network.c2s;

import immersive_melodies.cobalt.network.Message;
import immersive_melodies.cobalt.network.NetworkHandler;
import immersive_melodies.network.s2c.NoteMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class NoteBroadcastRequest extends Message {
    public final int tone;
    public final int velocity;

    public NoteBroadcastRequest(int tone, int velocity) {
        this.tone = tone;
        this.velocity = velocity;
    }

    public NoteBroadcastRequest(FriendlyByteBuf b) {
        this.tone = b.readInt();
        this.velocity = b.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(tone);
        b.writeInt(velocity);
    }

    @Override
    public void receive(Player e) {
        if (e instanceof ServerPlayer se) {
            se.serverLevel().players().stream()
                    .filter(player -> player != e && player.distanceToSqr(e) < 64)
                    .forEach(player -> {
                        NetworkHandler.sendToPlayer(new NoteMessage(player.getId(), tone, velocity), player);
                    });
        }
    }
}