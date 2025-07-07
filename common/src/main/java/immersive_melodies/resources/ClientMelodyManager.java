package immersive_melodies.resources;

import immersive_melodies.cobalt.network.NetworkHandler;
import immersive_melodies.network.c2s.MelodyRequest;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientMelodyManager {
    static final Map<ResourceLocation, Melody> melodies = new HashMap<>();
    static final Map<ResourceLocation, MelodyDescriptor> melodiesList = new HashMap<>();
    static final Set<ResourceLocation> requested = new HashSet<>();

    public static Map<ResourceLocation, MelodyDescriptor> getMelodiesList() {
        return melodiesList;
    }

    public static Melody getMelody(ResourceLocation identifier) {
        if (!melodies.containsKey(identifier) && !requested.contains(identifier)) {
            NetworkHandler.sendToServer(new MelodyRequest(identifier));
            requested.add(identifier);
        }
        return melodies.getOrDefault(identifier, Melody.DEFAULT);
    }

    public static void setMelody(ResourceLocation identifier, Melody melody) {
        melodies.put(identifier, melody);
    }
}
