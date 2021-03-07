package net.fabricmc.projectez.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessorMixin {
    @Accessor("options")
    GameOptions getGameOptions();
}
