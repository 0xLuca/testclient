package me.zieserl.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static void renderImage(MatrixStack matrixStack, ResourceLocation resourceLocation, int x, int y, int height, int width) {
        MINECRAFT.getTextureManager().bindTexture(resourceLocation);
        RenderSystem.enableBlend();
        AbstractGui.blit(matrixStack, x, y, 0.0F, 0.0F, height / 2, width / 2, height / 2, width / 2);
        RenderSystem.disableBlend();
    }
}
