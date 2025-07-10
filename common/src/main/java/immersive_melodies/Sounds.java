package immersive_melodies;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Sounds {
    Map<ResourceLocation, SoundEvent> sounds = new ConcurrentHashMap<>();

    static SoundEvent register(String namespace, String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, path);
        SoundEvent event = SoundEvent.createVariableRangeEvent(id);
        sounds.put(id, event);
        return event;
    }

    class Instrument {
        List<SoundEvent> octaves = new LinkedList<>();

        public Instrument(String namespace, String name) {
            for (int octave = 1; octave <= 8; octave++) {
                octaves.add(register(namespace, name + ".c" + octave));
            }
        }

        public SoundEvent get(int octave) {
            return octaves.get(octave - 1);
        }
    }

    static void registerSounds(Common.RegisterHelper<SoundEvent> helper) {
        sounds.forEach(helper::register);
    }
}
