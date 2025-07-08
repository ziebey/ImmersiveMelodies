package immersive_melodies.network.c2s;

import immersive_melodies.network.ImmersivePayload;
import immersive_melodies.network.PacketSplitter;
import immersive_melodies.resources.Melody;
import immersive_melodies.resources.ServerMelodyManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class MelodyRequest implements ImmersivePayload {
    private final ResourceLocation identifier;

    public MelodyRequest(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    public MelodyRequest(FriendlyByteBuf b) {
        this.identifier = b.readResourceLocation();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeResourceLocation(identifier);
    }

    @Override
    public void handle(Player e) {
        Melody melody = ServerMelodyManager.getMelody(identifier);
        PacketSplitter.sendToPlayer(identifier, melody, (ServerPlayer) e);
    }
}
