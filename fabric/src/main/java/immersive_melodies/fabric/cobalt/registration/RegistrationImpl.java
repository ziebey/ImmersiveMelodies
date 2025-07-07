package immersive_melodies.fabric.cobalt.registration;

import immersive_melodies.cobalt.registration.Registration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistrationImpl extends Registration.Impl {
    @Override
    public <T> Supplier<T> register(Registry<? super T> registry, ResourceLocation id, Supplier<T> obj) {
        T register = Registry.register(registry, id, obj.get());
        return () -> register;
    }
}
