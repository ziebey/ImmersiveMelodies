package immersive_melodies;

import immersive_melodies.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class Sounds {
    static Supplier<SoundEvent> register(String namespace, String path) {
        ResourceLocation id = ResourceLocation.tryBuild(namespace, path);
        return Registration.register(BuiltInRegistries.SOUND_EVENT, id, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void bootstrap() {
        // nop
    }

    public static class Instrument {
        List<Supplier<SoundEvent>> octaves = new LinkedList<>();

        public Instrument(String namespace, String name) {
            for (int octave = 1; octave <= 8; octave++) {
                octaves.add(register(namespace, name + ".c" + octave));
            }
        }

        public SoundEvent get(int octave) {
            return octaves.get(octave - 1).get();
        }
    }
}
