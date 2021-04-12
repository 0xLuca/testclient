package me.zieserl.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.zieserl.Client;
import me.zieserl.util.RenderUtil;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CustomMainMenuScreen extends Screen {
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_TITLE_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private static final ResourceLocation ACCESSIBILITY_TEXTURES = new ResourceLocation("textures/gui/accessibility.png");
    private static final ResourceLocation SINGLEPLAYER = Client.getResource("singleplayer.png");

    private int widthCopyright;
    private int widthCopyrightRest;
    private float currentBackgroundImage = 0;

    public CustomMainMenuScreen(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    protected void init() {
        this.widthCopyright = this.font.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.widthCopyrightRest = this.width - this.widthCopyright - 2;
        int j = this.height / 4 + 48;

        this.addSingleplayerMultiplayerButtons(j, 24);

        this.addButton(
                new ImageButton(
                        this.width / 2 - 124,
                        j + 72 + 12,
                        20,
                        20,
                        0,
                        106,
                        20,
                        Button.WIDGETS_LOCATION,
                        256,
                        256,
                        (button) -> this.minecraft.displayGuiScreen(
                                new LanguageScreen(this, this.minecraft.gameSettings, this.minecraft.getLanguageManager())
                        ),
                        new TranslationTextComponent("narrator.button.language")
                )
        );
        this.addButton(new Button(this.width / 2 - 100, j + 72 + 12, 98, 20, new TranslationTextComponent("menu.options"),
                (button) -> this.minecraft.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings))));
        this.addButton(new Button(this.width / 2 + 2, j + 72 + 12, 98, 20, new TranslationTextComponent("menu.quit"),
                (button) -> this.minecraft.shutdown()));
        this.addButton(
                new ImageButton(
                        this.width / 2 + 104,
                        j + 72 + 12,
                        20,
                        20,
                        0,
                        0,
                        20,
                        ACCESSIBILITY_TEXTURES,
                        32,
                        64,
                        (button) -> this.minecraft.displayGuiScreen(
                                new AccessibilityScreen(this, this.minecraft.gameSettings)
                        ),
                        new TranslationTextComponent("narrator.button.accessibility")
                )
        );

        this.minecraft.setConnectedToRealms(false);
    }

    private void addSingleplayerMultiplayerButtons(int yIn, int rowHeightIn) {
        this.addButton(new Button(this.width / 2 - 100, yIn, 200, 20, new TranslationTextComponent("menu.singleplayer"),
                (button) -> this.minecraft.displayGuiScreen(new WorldSelectionScreen(this))
        ));

        boolean multiplayerEnabled = this.minecraft.isMultiplayerEnabled();

        Button.ITooltip tooltip = multiplayerEnabled ? Button.field_238486_s_ : (button, matrixStack, mouseX, mouseY) -> {
            if (!button.active) {
                this.renderTooltip(
                        matrixStack,
                        this.minecraft.fontRenderer.trimStringToWidth(
                                new TranslationTextComponent("title.multiplayer.disabled"),
                                Math.max(this.width / 2 - 43, 170)
                        ),
                        mouseX,
                        mouseY);
            }
        };

        this.addButton(
                new Button(this.width / 2 - 100,
                        yIn + rowHeightIn,
                        200,
                        20,
                        new TranslationTextComponent("menu.multiplayer"),
                        (button) -> {
                            Screen screen = this.minecraft.gameSettings.skipMultiplayerWarning ? new MultiplayerScreen(this) : new MultiplayerWarningScreen(this);
                            this.minecraft.displayGuiScreen(screen);
                        }, tooltip
                )
        ).active = multiplayerEnabled;

        this.addButton(
                new Button(
                        this.width / 2 - 100,
                        yIn + rowHeightIn * 2,
                        200,
                        20,
                        new TranslationTextComponent("menu.online"),
                        (button) -> this.switchToRealms(),
                        tooltip
                )
        ).active = multiplayerEnabled;
    }

    private void switchToRealms() {
        RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
        realmsbridgescreen.func_231394_a_(this);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        renderLogo(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    /**
     * This method cycles through the parts of an split-up gif and repeats them
     * for an animated background.
     *
     * @param matrixStack MatrixStack for rendering the image
     */
    @Override
    public void renderBackground(MatrixStack matrixStack) {
        currentBackgroundImage += 0.1;
        int currentImage = (int) currentBackgroundImage;
        if (currentImage > 25) {
            currentBackgroundImage = 0;
            currentImage = (int) currentBackgroundImage;
        }
        RenderUtil.renderImage(matrixStack, Client.getResource("bg/" + currentImage + ".gif"), 0, 0, minecraft.getMainWindow().getWidth(), minecraft.getMainWindow().getHeight());
    }

    private void renderLogo(MatrixStack matrixStack) {
        int x = this.width / 2 - 137;

        // Render Minecraft Logo
        this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.blitBlackOutline(x, 30, (boxX, boxY) -> {
            this.blit(matrixStack, boxX, boxY, 0, 0, 155, 44);
            this.blit(matrixStack, boxX + 155, boxY, 0, 45, 155, 44);
        });

        // Render Edition Logo
        this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_EDITION);
        blit(matrixStack, x + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
    }

}