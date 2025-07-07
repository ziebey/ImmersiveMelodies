package immersive_melodies.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
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
        super(x, y, width, height, message, onPress, Supplier::get);

        this.tooltipSupplier = tooltipSupplier;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (visible) {
            updateTooltip();
        }
    }

    private void updateTooltip() {
        if (this.tooltipSupplier != null && isHovered()) {
            Screen screen = Minecraft.getInstance().screen;
            if (screen != null) {
                screen.setTooltipForNextRenderPass(this.tooltipSupplier.get(), this.createTooltipPositioner(), this.isFocused());
            }
        }
    }
}
