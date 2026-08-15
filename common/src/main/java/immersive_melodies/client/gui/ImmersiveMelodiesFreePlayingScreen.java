package immersive_melodies.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import immersive_melodies.Client;
import immersive_melodies.Config;
import immersive_melodies.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static immersive_melodies.client.gui.ImmersiveMelodiesScreen.BACKGROUND_TEXTURE;

public class ImmersiveMelodiesFreePlayingScreen extends Screen {
    public static final MutableComponent TEXT = Component.translatable("immersive_melodies.free_playing");

    private static final int KEY_WIDTH = 36;
    private static final int KEY_HEIGHT = 28;
    private static final int MAX_KEY_SPACING = 42;
    private static final int SHARP_ROW_OFFSET = -18;
    private static final int NATURAL_ROW_OFFSET = 16;

    private final Set<Integer> pressedKeys = new HashSet<>();

    protected ImmersiveMelodiesFreePlayingScreen() {
        super(TEXT);
    }

    @Override
    protected void init() {
        super.init();

        // Exit
        addRenderableWidget(new TexturedButtonWidget(width / 2 - 8, height / 2 + 58, 16, 16, BACKGROUND_TEXTURE, 256 - 16, 16, 256, 256, Component.nullToEmpty(null), button -> {
            onClose();
        }, () -> List.of(Component.translatable("immersive_melodies.close").getVisualOrderText())));
    }

    @Override
    protected void extractBlurredBackground(GuiGraphicsExtractor guiGraphics) {
        // Nop
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        List<Map.Entry<Integer, Integer>> mappings = new ArrayList<>(Config.getInstance().keycodeToMidi.entrySet());
        mappings.sort(Comparator.comparingInt(Map.Entry::getValue));

        context.centeredText(
                this.font,
                TEXT,
                this.width / 2,
                this.height / 2 - 50,
                0xFFFFFFFF
        );

        if (!mappings.isEmpty()) {
            double minPosition = mappings.stream().mapToDouble(entry -> notePosition(entry.getValue())).min().orElse(0.0);
            double maxPosition = mappings.stream().mapToDouble(entry -> notePosition(entry.getValue())).max().orElse(minPosition);
            double positionRange = Math.max(1.0, maxPosition - minPosition);
            int availableWidth = Math.max(KEY_WIDTH, this.width - 32);
            int keySpacing = Math.min(MAX_KEY_SPACING, Math.max(18, (int) ((availableWidth - KEY_WIDTH) / positionRange)));
            int totalWidth = (int) Math.round((maxPosition - minPosition) * keySpacing) + KEY_WIDTH;
            int startX = this.width / 2 - totalWidth / 2;

            for (Map.Entry<Integer, Integer> entry : mappings) {
                int keyCode = entry.getKey();
                int midi = entry.getValue();
                int x = startX + (int) Math.round((notePosition(midi) - minPosition) * keySpacing);
                int y = this.height / 2 + (isSharp(midi) ? SHARP_ROW_OFFSET : NATURAL_ROW_OFFSET);
                renderKey(context, x, y, keyCode, midi, pressedKeys.contains(keyCode));
            }
        }

        super.extractRenderState(context, mouseX, mouseY, delta);
    }

    private void renderKey(GuiGraphicsExtractor context, int x, int y, int keyCode, int midi, boolean pressed) {
        int borderColor = pressed ? 0xFFFFE59A : 0xFF8A8A8A;
        int backgroundColor = pressed ? 0xFFE2B84B : 0xCC171717;
        int keyColor = pressed ? 0xFF241B0A : 0xFFFFFFFF;
        int noteColor = pressed ? 0xFF3D2C0B : 0xFFC8C8C8;

        context.fill(x - 1, y - 1, x + KEY_WIDTH + 1, y + KEY_HEIGHT + 1, borderColor);
        context.fill(x, y, x + KEY_WIDTH, y + KEY_HEIGHT, backgroundColor);

        Component keyName = InputConstants.Type.KEYSYM.getOrCreate(keyCode).getDisplayName();
        context.centeredText(this.font, keyName, x + KEY_WIDTH / 2, y + 4, keyColor);
        context.centeredText(this.font, midiName(midi), x + KEY_WIDTH / 2, y + 16, noteColor);
    }

    private static String midiName(int midi) {
        String[] names = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        return names[Math.floorMod(midi, 12)] + (Math.floorDiv(midi, 12) - 1);
    }

    private static boolean isSharp(int midi) {
        return switch (Math.floorMod(midi, 12)) {
            case 1, 3, 6, 8, 10 -> true;
            default -> false;
        };
    }

    private static double notePosition(int midi) {
        int octave = Math.floorDiv(midi, 12);
        return octave * 7 + switch (Math.floorMod(midi, 12)) {
            case 0 -> 0.0;
            case 1 -> 0.5;
            case 2 -> 1.0;
            case 3 -> 1.5;
            case 4 -> 2.0;
            case 5 -> 3.0;
            case 6 -> 3.5;
            case 7 -> 4.0;
            case 8 -> 4.5;
            case 9 -> 5.0;
            case 10 -> 5.5;
            case 11 -> 6.0;
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        Integer midi = Config.getInstance().keycodeToMidi.get(event.key());
        if (midi != null) {
            if (pressedKeys.add(event.key())) {
                Client.playNote(midi, 127);
            }
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        Integer midi = Config.getInstance().keycodeToMidi.get(event.key());
        if (midi != null) {
            pressedKeys.remove(event.key());
            Client.playNote(midi, 0);
            return true;
        }
        return super.keyReleased(event);
    }

    @Override
    public void removed() {
        for (int keyCode : pressedKeys) {
            Integer midi = Config.getInstance().keycodeToMidi.get(keyCode);
            if (midi != null) {
                Client.playNote(midi, 0);
            }
        }
        pressedKeys.clear();
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
