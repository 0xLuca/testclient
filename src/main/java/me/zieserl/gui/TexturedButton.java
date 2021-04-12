package me.zieserl.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.zieserl.util.RenderUtil;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TexturedButton extends Button {
    private ResourceLocation buttonTexture;
    private int renderY;

    public TexturedButton(ResourceLocation buttonTexture, int x, int y, int width, int height, IPressable pressedAction) {
        this(buttonTexture, x, y, width, height, null, pressedAction, null);
    }

    public TexturedButton(ResourceLocation buttonTexture, int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip onTooltip) {
        super(x, y, width, height, title, pressedAction, onTooltip);
        this.buttonTexture = buttonTexture;
        this.renderY = y;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        checkHoverAnimation();
        RenderUtil.renderImage(matrixStack, buttonTexture, x, renderY, height * 2, width * 2);
    }

    private void checkHoverAnimation() {
        if (this.isHovered() && renderY > y - 5) {
            renderY--;
        } else if (!this.isHovered() && renderY < y) {
            renderY++;
        }
    }
}
