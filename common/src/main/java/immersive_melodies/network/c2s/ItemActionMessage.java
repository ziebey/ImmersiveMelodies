package immersive_melodies.network.c2s;

import immersive_melodies.item.InstrumentItem;
import immersive_melodies.network.ImmersivePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ItemActionMessage(int slot, State state, ResourceLocation melody) implements ImmersivePayload {
    public static ItemActionMessage fromStateAndMelody(State state, ResourceLocation melody) {
        LocalPlayer player = Minecraft.getInstance().player;
        int slot = player == null ? -1 : player.getInventory().selected;
        return new ItemActionMessage(slot, state, melody);
    }

    public static ItemActionMessage fromState(State state) {
        LocalPlayer player = Minecraft.getInstance().player;
        int slot = player == null ? -1 : player.getInventory().selected;
        return new ItemActionMessage(slot, state, new ResourceLocation("empty"));
    }

    public ItemActionMessage(FriendlyByteBuf b) {
        this(b.readInt(), b.readEnum(State.class), b.readResourceLocation());
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(slot);
        b.writeEnum(state);
        b.writeResourceLocation(melody);
    }

    @Override
    public void handle(Player e) {
        ItemStack stack = e.getInventory().getItem(slot);
        if (stack.getItem() instanceof InstrumentItem instrument) {
            switch (state) {
                case PLAY -> instrument.play(stack, melody, e.level(), e);
                case CONTINUE -> instrument.play(stack);
                case PAUSE -> instrument.pause(stack);
            }
        }
    }

    public enum State {
        PLAY,
        CONTINUE,
        PAUSE
    }
}