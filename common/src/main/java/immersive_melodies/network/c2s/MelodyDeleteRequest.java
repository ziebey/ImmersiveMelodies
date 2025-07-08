package immersive_melodies.network.c2s;

import immersive_melodies.network.ImmersivePayload;
import immersive_melodies.network.Network;
import immersive_melodies.network.s2c.MelodyListMessage;
import immersive_melodies.resources.ServerMelodyManager;
import immersive_melodies.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MelodyDeleteRequest implements ImmersivePayload {
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
    public void handle(Player e) {
        if (Utils.canDelete(identifier, e)) {
            ServerMelodyManager.deleteMelody(identifier);

            Network.sendToPlayer(new MelodyListMessage(e), (ServerPlayer) e);
        }
    }
}
