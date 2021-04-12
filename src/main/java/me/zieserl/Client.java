package me.zieserl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Client {
    public static final String RESOURCE_PATH = "client/";

    public static ResourceLocation getResource(String path) {
        return new ResourceLocation(RESOURCE_PATH + path);
    }

    public static void init() {
        Minecraft.getInstance().getMainWindow().setWindowTitle("Custom Client");
    }
}
