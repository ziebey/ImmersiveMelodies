package immersive_melodies.network;

import immersive_melodies.cobalt.network.Message;
import immersive_melodies.resources.Melody;
import io.netty.buffer.Unpooled;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public abstract class FragmentedMessage extends Message {
    private final String name;
    private final byte[] fragment;
    private final int length;

    private static final Map<String, Queue<byte[]>> buffer = new ConcurrentHashMap<>();

    public FragmentedMessage(String name, byte[] fragment, int length) {
        this.name = name;
        this.fragment = fragment;
        this.length = length;
    }

    public FragmentedMessage(FriendlyByteBuf b) {
        this.name = b.readUtf();
        this.fragment = b.readByteArray();
        this.length = b.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeUtf(name);
        b.writeByteArray(fragment);
        b.writeInt(length);
    }

    @Override
    public void receive(Player e) {
        String identifier = (e == null ? "local" : e.getStringUUID()) + ":" + name;
        Queue<byte[]> byteBuffer = buffer.computeIfAbsent(identifier, k -> new ConcurrentLinkedQueue<>());
        byteBuffer.add(fragment);

        if (byteBuffer.stream().mapToInt(f -> f.length).sum() >= length) {
            // Assemble
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            for (byte[] b : byteBuffer) {
                buffer.writeBytes(b);
            }

            finish(e, name, new Melody(buffer));

            FragmentedMessage.buffer.remove(identifier);
        }
    }

    protected abstract void finish(Player e, String name, Melody melody);
}
