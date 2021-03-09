package net.fabricmc.projectez;

import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class SimpleZoom implements ClientModInitializer {

    public static Logger LOGGER = LogManager.getLogger();
    private static FabricKeyBinding ZOOM_KEYBIND;
    public static final String MOD_ID = "simplezoom";

    public static void getRender(CallbackInfoReturnable<Double> callbackInfoReturnable) {
        SimpleConfig simpleConfig = getConfig();
        if(ZOOM_KEYBIND.isPressed()) {
            if(simpleConfig.isSmoothZoom()) {
                MinecraftClient.getInstance().options.smoothCameraEnabled = true;
            }
            callbackInfoReturnable.setReturnValue(getConfig().getZoomAmount());
        }else {
            if(simpleConfig.isSmoothZoom()) {
                MinecraftClient.getInstance().options.smoothCameraEnabled = false;
            }
        }
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Project E.Z] Loading!");
        AutoConfig.register(SimpleConfig.class, JanksonConfigSerializer::new);
        ZOOM_KEYBIND = FabricKeyBinding.Builder.create(new Identifier(MOD_ID, "zoom"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "SimpleZoom").build();
        KeyBindingRegistry.INSTANCE.addCategory("SimpleZoom");
        KeyBindingRegistry.INSTANCE.register(ZOOM_KEYBIND);
    }


    public static SimpleConfig getConfig() {
        return AutoConfig.getConfigHolder(SimpleConfig.class).getConfig();
    }

}
