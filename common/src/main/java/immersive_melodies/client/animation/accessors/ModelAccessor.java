package immersive_melodies.client.animation.accessors;

import immersive_melodies.item.InstrumentItem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ModelAccessor<T extends Entity> {
    T getEntity();

    private boolean isInMainHand() {
        if (getEntity() instanceof LivingEntity livingEntity) {
            return livingEntity.getMainHandStack().getItem() instanceof InstrumentItem;
        } else {
            return true;
        }
    }

    default boolean flipHands() {
        return isInMainHand() == isLeftHanded();
    }

    default boolean isLeftHanded() {
        return getEntity() instanceof LivingEntity livingEntity && livingEntity.getMainArm() == Arm.LEFT;
    }

    default Optional<ModelPart> getHead() {
        return Optional.empty();
    }

    default Optional<ModelPart> getHat() {
        return Optional.empty();
    }

    default Optional<ModelPart> getFlippedLeftArm() {
        return flipHands() ? getRightArm() : getLeftArm();
    }

    default Optional<ModelPart> getFlippedRightArm() {
        return flipHands() ? getLeftArm() : getRightArm();
    }

    default Optional<ModelPart> getLeftArm() {
        return Optional.empty();
    }

    default Optional<ModelPart> getRightArm() {
        return Optional.empty();
    }

    default float headYaw() {
        return getHead().map(h -> h.yaw).orElse(0.0f);
    }

    default void headYaw(float yaw) {
        getHead().ifPresent(h -> h.yaw = (flipHands() ? -yaw : yaw));
        getHat().ifPresent(h -> h.yaw = (flipHands() ? -yaw : yaw));
    }

    default float headPitch() {
        return getHead().map(h -> h.pitch).orElse(0.0f);
    }

    default void headPitch(float pitch) {
        getHead().ifPresent(h -> h.pitch = pitch);
        getHat().ifPresent(h -> h.pitch = pitch);
    }

    default float leftArmYaw() {
        return getFlippedLeftArm().map(l -> l.yaw).orElse(0.0f);
    }

    default void leftArmYaw(float yaw) {
        getFlippedLeftArm().ifPresent(l -> l.yaw = (flipHands() ? -yaw : yaw));
    }

    default float leftArmPitch() {
        return getFlippedLeftArm().map(l -> l.pitch).orElse(0.0f);
    }

    default void leftArmPitch(float pitch) {
        getFlippedLeftArm().ifPresent(l -> l.pitch = pitch);
    }

    default float leftArmRoll() {
        return getFlippedLeftArm().map(l -> l.roll).orElse(0.0f);
    }

    default void leftArmRoll(float roll) {
        getFlippedLeftArm().ifPresent(l -> l.roll = (flipHands() ? -roll : roll));
    }

    default float rightArmYaw() {
        return getFlippedRightArm().map(r -> r.yaw).orElse(0.0f);
    }

    default void rightArmYaw(float yaw) {
        getFlippedRightArm().ifPresent(r -> r.yaw = (flipHands() ? -yaw : yaw));
    }

    default float rightArmPitch() {
        return getFlippedRightArm().map(r -> r.pitch).orElse(0.0f);
    }

    default void rightArmPitch(float pitch) {
        getFlippedRightArm().ifPresent(r -> r.pitch = pitch);
    }

    default float rightArmRoll() {
        return getFlippedRightArm().map(r -> r.roll).orElse(0.0f);
    }

    default void rightArmRoll(float roll) {
        getFlippedRightArm().ifPresent(r -> r.roll = (flipHands() ? -roll : roll));
    }
}
