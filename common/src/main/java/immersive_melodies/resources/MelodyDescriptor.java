package immersive_melodies.resources;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class MelodyDescriptor {
    public static final Codec<MelodyDescriptor> CODEC = Codec.STRING.xmap(MelodyDescriptor::new, MelodyDescriptor::getName);

    private final String name;

    public MelodyDescriptor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static StreamCodec<FriendlyByteBuf, MelodyDescriptor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, MelodyDescriptor::getName,
            MelodyDescriptor::new
    );
}