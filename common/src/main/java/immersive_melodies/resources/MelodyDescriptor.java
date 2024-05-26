package immersive_melodies.resources;

import immersive_melodies.Common;
import net.minecraft.network.PacketByteBuf;

public class MelodyDescriptor {
    private final String name;

    public MelodyDescriptor(String name) {
        this.name = name;
    }

    public MelodyDescriptor(PacketByteBuf b) {
        String name;
        try {
            name = b.readString();
        } catch (Exception e) {
            Common.LOGGER.error(e);
            name = "null";
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void encodeLite(PacketByteBuf b) {
        b.writeString(name);
    }
}