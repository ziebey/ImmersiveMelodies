package immersive_melodies.network.s2c;

import immersive_melodies.network.FragmentedMessage;
import immersive_melodies.resources.ClientMelodyManager;
import immersive_melodies.resources.Melody;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class MelodyResponse extends FragmentedMessage {
    public MelodyResponse(FriendlyByteBuf b) {
        super(b);
    }

    public MelodyResponse(ResourceLocation identifier, byte[] fragment, int length) {
        super(identifier.toString(), fragment, length);
    }

    @Override
    protected void finish(Player e, String name, Melody melody) {
        ClientMelodyManager.setMelody(new ResourceLocation(name), melody);
    }
}
