package net.fabricmc.projectez.mods;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.projectez.event.EventHandler;
import net.fabricmc.projectez.event.client.render.hud.InGameHudRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class ArmorHUDMod extends Mod {
    private final MinecraftClient mc;

    private TextRenderer textRenderer;
    private ItemRenderer renderer;

    public ArmorHUDMod() {
        super("projectez.armorHUD");
        mc = MinecraftClient.getInstance();
        textRenderer = mc.textRenderer;
        renderer = mc.getItemRenderer();
    }

    @EventHandler
    public void render(InGameHudRenderEvent e) {
        final PlayerEntity player = mc.player;
        textRenderer = mc.textRenderer; renderer = mc.getItemRenderer();

        if (player == null) return;

        renderStack(0,0,player.getMainHandStack());
    }

    private void renderStack(int x, int y, ItemStack stack) {
        MatrixStack ms = new MatrixStack();

        if (stack.isEmpty()) return;

        drawCount(x,y,stack);
        if (stack.getMaxDamage() > 0) drawDurability(x,y,stack);
    }


    private void drawCount(int x, int y, ItemStack stack) {
        MatrixStack ms = new MatrixStack();
        int c=stack.getCount(), s=16;

        renderer.renderInGui(stack,x,y);

        ms.translate(x,y,0);
        Text countText = Text.of(c+"x");
        textRenderer.draw(ms, countText,s+1,1,0xffffffff);
    }
    private void drawDurability(int x, int y, ItemStack stack) {
        MatrixStack ms = new MatrixStack();
        int fh=textRenderer.fontHeight, d=stack.getDamage(), dm=stack.getMaxDamage(), s=16, dcw=50;
        int xn=x+s, yn=y+fh+2, xp=xn+dcw, yp=yn+6, dw=((dm-d)*(dcw-4))/dm;
        Text durabilityText = Text.of((dm-d)+"/"+dm);

        ms.push(); ms.translate(x,y,0); ms.scale(0.5f, 0.5f, 0.5f);
        textRenderer.draw(ms, durabilityText, 2 * (s+dcw) - textRenderer.getWidth(durabilityText.asString()), textRenderer.fontHeight, 0x88ffffff);
        ms.pop();

        fillGradient(ms, xn, yn, xp, yn + 1, -1, -1);
        fillGradient(ms, xn, yn, xn + 1, yp, -1, -1);
        fillGradient(ms,xn,yp-1,xp,yp,-1,-1);
        fillGradient(ms,xp-1,yn,xp,yp,-1,-1);
        fillGradient(ms, xn + 2, yn + 2, xn + 2 + dw, yp - 2, -1, -1);
    }

    protected void fillGradient(MatrixStack matrices, int xStart, int yStart, int xEnd, int yEnd, int colorStart, int colorEnd) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
        fillGradient(matrices.peek().getModel(), bufferBuilder, xStart, yStart, xEnd, yEnd, 0, colorStart, colorEnd);
        tessellator.draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    protected static void fillGradient(Matrix4f matrix, BufferBuilder bufferBuilder, int xStart, int yStart, int xEnd, int yEnd, int z, int colorStart, int colorEnd) {
        float f = (float)(colorStart >> 24 & 255) / 255.0F;
        float g = (float)(colorStart >> 16 & 255) / 255.0F;
        float h = (float)(colorStart >> 8 & 255) / 255.0F;
        float i = (float)(colorStart & 255) / 255.0F;
        float j = (float)(colorEnd >> 24 & 255) / 255.0F;
        float k = (float)(colorEnd >> 16 & 255) / 255.0F;
        float l = (float)(colorEnd >> 8 & 255) / 255.0F;
        float m = (float)(colorEnd & 255) / 255.0F;
        bufferBuilder.vertex(matrix, (float)xEnd, (float)yStart, (float)z).color(g, h, i, f).next();
        bufferBuilder.vertex(matrix, (float)xStart, (float)yStart, (float)z).color(g, h, i, f).next();
        bufferBuilder.vertex(matrix, (float)xStart, (float)yEnd, (float)z).color(k, l, m, j).next();
        bufferBuilder.vertex(matrix, (float)xEnd, (float)yEnd, (float)z).color(k, l, m, j).next();
    }
}
