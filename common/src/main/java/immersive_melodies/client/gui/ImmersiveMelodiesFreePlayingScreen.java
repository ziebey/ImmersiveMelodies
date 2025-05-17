package immersive_melodies.client.gui;

import immersive_melodies.Client;
import immersive_melodies.Config;
import immersive_melodies.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

import static immersive_melodies.client.gui.ImmersiveMelodiesScreen.BACKGROUND_TEXTURE;

public class ImmersiveMelodiesFreePlayingScreen extends Screen {
    public static final MutableText TEXT = Text.translatable("immersive_melodies.free_playing");

    protected ImmersiveMelodiesFreePlayingScreen() {
        super(TEXT);
    }

    @Override
    protected void init() {
        super.init();

        // Exit
        addDrawableChild(new TexturedButtonWidget(width / 2, height / 2 + 50, 16, 16, BACKGROUND_TEXTURE, 256 - 16, 16, 256, 256, Text.of(null), button -> {
            close();
        }, () -> List.of(Text.translatable("immersive_melodies.close").asOrderedText())));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Hint
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                TEXT,
                this.width / 2,
                this.height / 2 + 70,
                0xFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Config.getInstance().scancodeToMidi.containsKey(scanCode)) {
            int midi = Config.getInstance().scancodeToMidi.get(scanCode);
            Client.playNote(midi, 127);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (Config.getInstance().scancodeToMidi.containsKey(scanCode)) {
            int midi = Config.getInstance().scancodeToMidi.get(scanCode);
            Client.playNote(midi, 0);
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
