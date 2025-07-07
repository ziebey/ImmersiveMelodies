package immersive_melodies.network.c2s;

import immersive_melodies.cobalt.network.Message;
import immersive_melodies.item.InstrumentItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemActionMessage extends Message {
    private final int slot;
    private final State state;
    private final ResourceLocation melody;

    public ItemActionMessage(State state, ResourceLocation melody) {
        LocalPlayer player = Minecraft.getInstance().player;
        slot = player == null ? -1 : player.getInventory().selected;
        this.state = state;
        this.melody = melody;
    }

    public ItemActionMessage(State state) {
        LocalPlayer player = Minecraft.getInstance().player;
        slot = player == null ? -1 : player.getInventory().selected;
        this.state = state;
        this.melody = new ResourceLocation("empty");
    }

    public ItemActionMessage(FriendlyByteBuf b) {
        slot = b.readInt();
        state = b.readEnum(State.class);
        melody = b.readResourceLocation();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(slot);
        b.writeEnum(state);
        b.writeResourceLocation(melody);
    }

    @Override
    public void receive(Player e) {
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