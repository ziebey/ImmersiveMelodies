package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.cobalt.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class OpenGuiRequest extends Message {
    public final Type gui;

    public OpenGuiRequest(OpenGuiRequest.Type gui) {
        this.gui = gui;
    }

    public OpenGuiRequest(FriendlyByteBuf b) {
        this.gui = b.readEnum(OpenGuiRequest.Type.class);
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeEnum(gui);
    }

    @Override
    public void receive(Player e) {
        Common.networkManager.handleOpenGuiRequest(this);
    }

    public enum Type {
        SELECTOR,
    }
}
