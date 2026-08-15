package immersive_melodies.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

import java.util.WeakHashMap;

/**
 * During the extraction phase, the vanilla renderer only passes around an {@link EntityRenderState},
 * which no longer references the entity it was extracted from. This cache bridges the two during
 * entity model animation.
 */
public class RenderStateEntityCache {
    private static final WeakHashMap<EntityRenderState, Entity> CACHE = new WeakHashMap<>();

    public static void set(EntityRenderState state, Entity entity) {
        if (entity != null) {
            CACHE.put(state, entity);
        }
    }

    public static Entity get(EntityRenderState state) {
        return CACHE.get(state);
    }
}
