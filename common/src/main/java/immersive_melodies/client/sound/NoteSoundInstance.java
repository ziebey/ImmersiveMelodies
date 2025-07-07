package immersive_melodies.client.sound;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class NoteSoundInstance extends EntityBoundSoundInstance implements CancelableSoundInstance {
    long age;
    long length;
    long sustain;
    long fallOff;

    public NoteSoundInstance(SoundEvent sound, SoundSource category, float volume, float pitch, long length, long sustain, Entity entity) {
        super(sound, category, volume, pitch, entity, 1);

        this.length = length + sustain;
        this.sustain = sustain;
        this.fallOff = Math.max(50, sustain);
    }

    @Override
    public void tick() {
        super.tick();

        // Fade out
        age += 50;
        if (age >= length) {
            this.stop();
        }

        // todo Pitch bend
    }

    @Override
    public float getVolume() {
        float f = Math.min(1.0f, ((float) (length - age)) / fallOff);
        return volume * f;
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public void cancel() {
        this.age = length - fallOff;
    }
}
