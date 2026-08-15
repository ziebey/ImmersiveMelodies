package immersive_melodies.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Supplier;

public class TexturedButtonWidget extends DefaultButtonWidget {
    private final int u, v, tw, th, w, h;
    private final Identifier texture;

    public TexturedButtonWidget(int x, int y, int width, int height, Identifier texture, int u, int v, int tw, int th, Component message, OnPress onPress, Supplier<List<FormattedCharSequence>> tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.texture = texture;
        this.w = width;
        this.h = height;
        this.u = u;
        this.v = v;
        this.tw = tw;
        this.th = th;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTick) {
        super.extractContents(context, mouseX, mouseY, partialTick);

        // 26.2 pixel-UV blit: (pipeline, texture, x, y, u, v, width, height, srcWidth, srcHeight, textureWidth, textureHeight)
        context.blit(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), this.u, this.v + (active ? 0 : 16), this.w, this.h, this.tw, this.th);

        int j = this.active ? 0xFFFFFF : 0xA0A0A0;
        context.centeredText(Minecraft.getInstance().font, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0f) << 24);
    }
}
