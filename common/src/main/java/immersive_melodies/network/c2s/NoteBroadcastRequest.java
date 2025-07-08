package immersive_melodies.network.c2s;

import immersive_melodies.network.ImmersivePayload;
import immersive_melodies.network.Network;
import immersive_melodies.network.s2c.NoteMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class NoteBroadcastRequest implements ImmersivePayload {
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
    public void handle(Player e) {
        if (e instanceof ServerPlayer se) {
            se.serverLevel().players().stream()
                    .filter(player -> player != e && player.distanceToSqr(e) < 64)
                    .forEach(player -> {
                        Network.sendToPlayer(new NoteMessage(player.getId(), tone, velocity), player);
                    });
        }
    }
}