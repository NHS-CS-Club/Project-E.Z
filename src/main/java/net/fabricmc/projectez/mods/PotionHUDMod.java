package net.fabricmc.projectez.mods;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.render.PotionEffectRenderEvent;
import net.fabricmc.projectez.mixin.MinecraftClientAccessorMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class PotionHUDMod extends Mod {
    private final MinecraftClient client;

    public PotionHUDMod() {
        super("Potion Effects HUD");
        client = MinecraftClient.getInstance();
        /*HudRenderCallback.EVENT.register((__, ___) -> {
            this.render();
        });*/
    }

    @EventHandler
    public void render(PotionEffectRenderEvent e) {
        e.cancel();

        final PlayerEntity player = client.player;
        final InGameHud inGameHud = client.inGameHud;
        final TextRenderer textRenderer = client.textRenderer;
        final MatrixStack matrixStack = new MatrixStack();


        if (player == null) return;

        Collection<StatusEffectInstance> statusEffects = player.getStatusEffects();

        final GameOptions gameOptions = ((MinecraftClientAccessorMixin) MinecraftClient.getInstance()).getGameOptions();

        if (!statusEffects.isEmpty() && !gameOptions.debugEnabled) {
            RenderSystem.enableBlend();

            StatusEffectSpriteManager statusEffectSpriteManager = client.getStatusEffectSpriteManager();
            List<Runnable> statusEffectsRunnables = Lists.newArrayListWithExpectedSize(statusEffects.size());

            final int spriteSize = 18;

            for (StatusEffectInstance statusEffectInstance : Ordering.natural().reverse().sortedCopy(statusEffects)) {
                StatusEffect statusEffect = statusEffectInstance.getEffectType();

                double durationInSeconds = statusEffectInstance.getDuration() / 20.0;
                String formattedPotionInfo = getPotionInfoText(
                        statusEffectInstance.getAmplifier(),
                        durationInSeconds,
                        statusEffectInstance.isPermanent()
                );

                final int x = 3;
                final int y = spriteSize * statusEffectsRunnables.size() + 3;

                Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);

                statusEffectsRunnables.add(() -> {
                    client.getTextureManager().bindTexture(sprite.getAtlas().getId());
                    DrawableHelper.drawSprite(matrixStack, x, y, inGameHud.getZOffset(), 18, 18, sprite);
                    final float textYOffset = spriteSize / 2f - textRenderer.fontHeight / 2.5f;
                    int color;
                    if (durationInSeconds <= 5)       color = 0xFF5555;
                    else if (durationInSeconds <= 15) color = 0xFFAA00;
                    else if (durationInSeconds <= 25) color = 0xFFFF55;
                    else                              color = 0xFFFFFF;
                    textRenderer.draw(matrixStack, formattedPotionInfo, x + spriteSize + 3, y + textYOffset, color);
                });
            }

            statusEffectsRunnables.forEach(Runnable::run);
        }
    }

    private String getPotionInfoText(int strength, double durationInSeconds, boolean isPermanent) {
        final long minutes = TimeUnit.SECONDS.toMinutes((long)durationInSeconds);
        final long seconds = (long)durationInSeconds - TimeUnit.MINUTES.toSeconds(minutes);

        if (isPermanent) return String.format("[LVL %d] PERMANENT",strength+1);
        return String.format("[LVL %3$d] "+(minutes>0?"%1$d min, ":"")+"%2$d sec", minutes, seconds, strength+1);
    }
}
