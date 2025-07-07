package immersive_melodies.network.c2s;

import immersive_melodies.cobalt.network.Message;
import immersive_melodies.cobalt.network.NetworkHandler;
import immersive_melodies.network.s2c.MelodyListMessage;
import immersive_melodies.resources.ServerMelodyManager;
import immersive_melodies.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MelodyDeleteRequest extends Message {
    private final ResourceLocation identifier;

    public MelodyDeleteRequest(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    public MelodyDeleteRequest(FriendlyByteBuf b) {
        this.identifier = b.readResourceLocation();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeResourceLocation(identifier);
    }

    @Override
    public void receive(Player e) {
        if (Utils.canDelete(identifier, e)) {
            ServerMelodyManager.deleteMelody(identifier);

            NetworkHandler.sendToPlayer(new MelodyListMessage(e), (ServerPlayer) e);
        }
    }
}
