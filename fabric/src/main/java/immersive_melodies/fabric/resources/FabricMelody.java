package immersive_melodies.fabric.resources;

import immersive_melodies.Common;
import immersive_melodies.resources.MelodyLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricMelody extends MelodyLoader implements IdentifiableResourceReloadListener {
    private static final ResourceLocation ID = Common.locate("melodies");

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }
}
