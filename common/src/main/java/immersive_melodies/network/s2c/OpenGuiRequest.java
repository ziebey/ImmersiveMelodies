package immersive_melodies.network.s2c;

import immersive_melodies.Common;
import immersive_melodies.network.ImmersivePayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record OpenGuiRequest(Type gui) implements ImmersivePayload {
    public OpenGuiRequest(FriendlyByteBuf b) {
        this(b.readEnum(Type.class));
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeEnum(gui);
    }

    @Override
    public void handle(Player e) {
        Common.networkManager.handleOpenGuiRequest(this);
    }

    public enum Type {
        SELECTOR,
    }
}
