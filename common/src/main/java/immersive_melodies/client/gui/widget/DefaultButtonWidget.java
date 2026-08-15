package immersive_melodies.client.gui.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.function.Supplier;

public class DefaultButtonWidget extends Button {
    private final Supplier<List<FormattedCharSequence>> tooltipSupplier;

    public DefaultButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        this(x, y, width, height, message, onPress, null);
    }

    public DefaultButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress, Supplier<List<FormattedCharSequence>> tooltipSupplier) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);

        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        extractDefaultSprite(guiGraphics);
        extractDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));

        if (visible && tooltipSupplier != null && isHovered) {
            guiGraphics.setTooltipForNextFrame(tooltipSupplier.get(), mouseX, mouseY);
        }
    }
}
