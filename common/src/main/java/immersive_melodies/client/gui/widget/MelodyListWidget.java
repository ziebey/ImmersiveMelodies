package immersive_melodies.client.gui.widget;

import immersive_melodies.client.gui.ImmersiveMelodiesScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Objects;

public class MelodyListWidget extends AlwaysSelectedEntryListWidget<MelodyListWidget.MelodyEntry> {
    private final ImmersiveMelodiesScreen currentScreen;
    private final boolean showSelection;

    public MelodyListWidget(MinecraftClient client, ImmersiveMelodiesScreen currentScreen, int left, int width, int listY, int listH, boolean showSelection) {
        super(client, width, currentScreen.height, listY, listY + listH, 10);

        this.currentScreen = currentScreen;

        this.left = left;
        this.right = this.left + width;

        this.showSelection = showSelection;

        setRenderBackground(false);
        setRenderHorizontalShadows(false);
        setRenderHeader(false, 0);
    }

    @Override
    public void clearEntries() {
        super.clearEntries();
    }

    public void addEntry(Identifier identifier, Text name, Runnable onPress) {
        super.addEntry(new MelodyEntry(identifier, name, onPress));
    }

    @Override
    public void replaceEntries(Collection<MelodyEntry> newEntries) {
        super.replaceEntries(newEntries);
    }

    @Override
    protected int getScrollbarPositionX() {
        return left + width + 2;
    }

    @Override
    public int getRowWidth() {
        return width;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= left && mouseX <= left + width + 10 && mouseY >= this.top && mouseY <= this.bottom;
    }

    @Override
    protected void enableScissor(DrawContext context) {
        context.enableScissor(left - 15, this.top, left + width, this.bottom);
    }

    @Override
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        if (showSelection) {
            context.fill(left - 1, y - 1, left + width, y + entryHeight + 3, 0x40000000);
        }
    }

    public class MelodyEntry extends AlwaysSelectedEntryListWidget.Entry<MelodyEntry> {
        final Identifier identifier;
        final Text name;
        final Runnable onPress;

        public MelodyEntry(Identifier identifier, Text melody, Runnable onPress) {
            this.identifier = identifier;
            this.name = melody;
            this.onPress = onPress;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(currentScreen.getTextRenderer(), name, left + (onPress == null ? -2 : 2), y + 1, 0x404040, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0 && onPress != null) {
                onPress.run();
                return true;
            }
            return false;
        }

        @Override
        public Text getNarration() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MelodyEntry that)) return false;
            return Objects.equals(identifier, that.identifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(identifier);
        }
    }
}