package immersive_melodies.network.s2c;

import immersive_melodies.network.FragmentedMessage;
import immersive_melodies.resources.ClientMelodyManager;
import immersive_melodies.resources.Melody;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record MelodyResponse(String name, byte[] fragment, int length) implements FragmentedMessage {
    public MelodyResponse(FriendlyByteBuf b) {
        this(
                b.readUtf(),
                b.readByteArray(),
                b.readVarInt()
        );
    }

    @Override
    public void finish(Player e, String name, Melody melody) {
        ClientMelodyManager.setMelody(new ResourceLocation(name), melody);
    }
}
