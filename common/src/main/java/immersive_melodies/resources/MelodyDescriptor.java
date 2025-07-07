package immersive_melodies.resources;

import net.minecraft.network.FriendlyByteBuf;

public class MelodyDescriptor {
    private final String name;

    public MelodyDescriptor(String name) {
        this.name = name;
    }

    public MelodyDescriptor(FriendlyByteBuf b) {
        this.name = b.readUtf();
    }

    public String getName() {
        return name;
    }

    public void encodeLite(FriendlyByteBuf b) {
        b.writeUtf(name);
    }
}