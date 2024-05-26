package immersive_melodies.resources;

import immersive_melodies.Common;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.level.storage.LevelStorage;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ServerMelodyManager {
    static final Random RANDOM = new Random();

    public static MinecraftServer server;
    private static Map<Identifier, MelodyLoader.LazyMelody> datapackMelodies = new HashMap<>();
    private static File directory = new File("data/melodies");

    public static void instantiate(ServerWorld world, LevelStorage.Session session) {
        directory = session.getWorldDirectory(world.getRegistryKey()).resolve("data/melodies").toFile();
    }

    private static File getFile(String id) {
        File file = new File(directory, id.replace(":", "/") + ".bin");
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        return file;
    }

    public static CustomServerMelodiesIndex getIndex() {
        return server.getOverworld().getPersistentStateManager().getOrCreate(CustomServerMelodiesIndex::fromNbt, CustomServerMelodiesIndex::new, "immersive_melodies");
    }

    public static MelodyTrackSettings getSettings() {
        return server.getOverworld().getPersistentStateManager().getOrCreate(MelodyTrackSettings::fromNbt, MelodyTrackSettings::new, "immersive_melodies_settings");
    }

    public static Map<Identifier, MelodyLoader.LazyMelody> getDatapackMelodies() {
        return datapackMelodies;
    }

    public static void setDatapackMelodies(Map<Identifier, MelodyLoader.LazyMelody> datapackMelodies) {
        ServerMelodyManager.datapackMelodies = datapackMelodies;
    }

    public static Identifier getRandomMelody() {
        Object[] datapack = getDatapackMelodies().keySet().toArray();
        Object[] custom = getIndex().melodies.keySet().toArray();
        int i = RANDOM.nextInt(datapack.length + custom.length);
        if (i < datapack.length) {
            return (Identifier) datapack[i];
        } else {
            return (Identifier) custom[i - datapack.length];
        }
    }

    /**
     * Registers a melody to the server and saves it to disk.
     *
     * @param identifier The identifier of the melody to register.
     * @param melody     The melody to register.
     */
    public static void registerMelody(Identifier identifier, Melody melody) {
        getIndex().getMelodies().put(identifier, melody);
        getIndex().setDirty(true);

        try {
            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
            melody.encode(buffer);

            // Write to disk
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(getFile(identifier.toString())));
            bos.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a melody from the server.
     *
     * @param identifier The identifier of the melody to delete.
     */
    public static void deleteMelody(Identifier identifier) {
        getIndex().getMelodies().remove(identifier);
        getIndex().setDirty(true);

        try {
            Files.delete(getFile(identifier.toString()).toPath());
        } catch (IOException e) {
            Common.LOGGER.error("Couldn't delete melody {} ({})", identifier, e);
        }
    }

    public static Melody getMelody(Identifier identifier) {
        if (datapackMelodies.containsKey(identifier)) {
            return datapackMelodies.get(identifier).get();
        } else {
            Melody melody = Melody.DEFAULT;
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(getFile(identifier.toString())));
                melody = new Melody(new PacketByteBuf(Unpooled.wrappedBuffer(bis.readAllBytes())));
            } catch (IOException e) {
                Common.LOGGER.error("Couldn't load melody {} ({})", identifier, e);
                deleteMelody(identifier);
            }
            return melody;
        }
    }

    /**
     * The melody index, containing only important information about the melodies.
     */
    public static class CustomServerMelodiesIndex extends PersistentState {
        final Map<Identifier, MelodyDescriptor> melodies = new HashMap<>();

        public static CustomServerMelodiesIndex fromNbt(NbtCompound nbt) {
            CustomServerMelodiesIndex c = new CustomServerMelodiesIndex();
            for (String key : nbt.getKeys()) {
                PacketByteBuf buffer = new PacketByteBuf(Unpooled.wrappedBuffer(nbt.getByteArray(key)));
                c.melodies.put(new Identifier(key), new MelodyDescriptor(buffer));
            }
            return c;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            NbtCompound c = new NbtCompound();
            for (Map.Entry<Identifier, MelodyDescriptor> entry : melodies.entrySet()) {
                PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                entry.getValue().encodeLite(buffer);
                c.putByteArray(entry.getKey().toString(), buffer.array());
            }
            return c;
        }

        public Map<Identifier, MelodyDescriptor> getMelodies() {
            return melodies;
        }
    }

    /**
     * Stores the settings for the melody tracks.
     */
    public static class MelodyTrackSettings extends PersistentState {
        final Map<Identifier, Map<UUID, Set<Integer>>> enabledTracks = new HashMap<>();

        public static MelodyTrackSettings fromNbt(NbtCompound nbt) {
            MelodyTrackSettings c = new MelodyTrackSettings();
            for (String key : nbt.getKeys()) {
                NbtCompound map = nbt.getCompound(key);
                Map<UUID, Set<Integer>> m = new HashMap<>();
                for (String k : map.getKeys()) {
                    NbtCompound set = map.getCompound(k);
                    Set<Integer> s = new HashSet<>();
                    for (String i : set.getKeys()) {
                        s.add(set.getInt(i));
                    }
                    m.put(UUID.fromString(k), s);
                }
                c.enabledTracks.put(new Identifier(key), m);
            }
            return c;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            NbtCompound c = new NbtCompound();
            for (Map.Entry<Identifier, Map<UUID, Set<Integer>>> entry : enabledTracks.entrySet()) {
                NbtCompound map = new NbtCompound();
                for (Map.Entry<UUID, Set<Integer>> e : entry.getValue().entrySet()) {
                    NbtCompound set = new NbtCompound();
                    for (int i : e.getValue()) {
                        set.putInt(e.getKey().toString(), i);
                    }
                    map.put(e.getKey().toString(), set);
                }
                c.put(entry.getKey().toString(), map);
            }
            return c;
        }

        public void enableTrack(Identifier melody, UUID player, int track) {
            enabledTracks.computeIfAbsent(melody, k -> new HashMap<>()).computeIfAbsent(player, k -> new HashSet<>()).add(track);
            setDirty(true);
        }

        public void disableTrack(Identifier melody, UUID player, int track) {
            Map<UUID, Set<Integer>> uuidSetMap = enabledTracks.computeIfAbsent(melody, k -> new HashMap<>());
            uuidSetMap.computeIfAbsent(player, k -> new HashSet<>()).remove(track);
            setDirty(true);
        }

        public Set<Integer> getEnabledTracks(Identifier name, UUID player) {
            Melody melody = getMelody(name);
            int primaryId = melody.getTracks().indexOf(melody.getPrimaryTrack());
            Map<UUID, Set<Integer>> playerSettings = enabledTracks.getOrDefault(name, Collections.emptyMap());
            return playerSettings.getOrDefault(player, playerSettings.values().stream().findFirst().orElse(Set.of(primaryId)));
        }
    }
}
