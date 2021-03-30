package net.fabricmc.projectez.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class RenderUtil {

    public static final int ITEM_RENDER_HEIGHT = 16;
    public static final int DURABILITY_WIDTH = 100;

    private static TextRenderer textRenderer;
    private static ItemRenderer itemRenderer;

    public static void updateRenderers() {
        MinecraftClient mc = MinecraftClient.getInstance();
        textRenderer = mc.textRenderer;
        itemRenderer = mc.getItemRenderer();
    }


    public static int getHUDItemRenderHeight(ItemStack stack, boolean showTooltipList) {
        int h = 2+ITEM_RENDER_HEIGHT, n = getHudItemTooltips(stack).size();
        if (n > 0 && showTooltipList) h += n*(textRenderer.fontHeight/2+1)+1;
        return h;
    }
    public static void renderHUDItem(MatrixStack matrixStack, int x, int y, ItemStack stack, boolean showTooltipList) {
        fillRectGradient(matrixStack,x-1,y-1,x+1+ITEM_RENDER_HEIGHT+DURABILITY_WIDTH+30, y-1+getHUDItemRenderHeight(stack,showTooltipList),0x55000000,0x00000000, true);
        if (stack.isEmpty())
            drawHUDItemEmpty(matrixStack,x,y);
        else {
            if (stack.getMaxDamage() == 0)
                drawHUDItemCount(matrixStack, x, y, stack);
            else
                drawHUDItemCountDurability(matrixStack, x, y, stack);
            if (showTooltipList)
                drawHudItemTooltip(matrixStack, x, y, stack);
        }
    }

    private static List<Text> getHudItemTooltips(ItemStack stack) {
        final List<Text> tooltip = new ArrayList<>();

        // Potion Effects
        if (stack.getItem() == Items.POTION || stack.getItem() == Items.SPLASH_POTION || stack.getItem() == Items.LINGERING_POTION)
            ItemUtil.buildModifiedPotionTooltip(stack,tooltip,1f);

        // Enchantments
        final List<ItemUtil.LeveledEnchant> enchantments = ItemUtil.getEnchantments(stack);
        for (ItemUtil.LeveledEnchant e : enchantments)
            tooltip.add(getEnchantmentName(e.enchantment,e.level));

        return tooltip;
    }
    private static void drawHudItemTooltip(MatrixStack ms, int x, int y, ItemStack stack) {
        ms.push();
        int yOff = ITEM_RENDER_HEIGHT+2;
        final List<Text> tooltip = getHudItemTooltips(stack);
        ms.scale(0.5f,0.5f,0.5f);
        for (Text row : tooltip) {
            textRenderer.draw(ms,row,2*x+1,2*(y+yOff),-1);
            yOff += textRenderer.fontHeight/2+1;
        }
        ms.pop();
    }
    private static void drawHUDItemEmpty(MatrixStack ms, int x, int y) {
        ms.push();
        ms.translate(x,y,0);
        Text noItemText = Text.of("no item");
        textRenderer.draw(ms, noItemText, 3,ITEM_RENDER_HEIGHT/2f+1-textRenderer.fontHeight/2f,0x88ffffff);
        ms.pop();
    }
    private static Text getHUDItemCountNameText(ItemStack stack) { return stack.getCount() > 1 ? Text.of("").shallowCopy().append(Text.of(stack.getCount()+" ").shallowCopy().formatted(Formatting.GRAY)).append(stack.getName()) : stack.getName(); }
    private static Text getHUDItemDamageText(ItemStack stack) { return stack.isDamageable()?Text.of((stack.getMaxDamage()-stack.getDamage())+"/"+stack.getMaxDamage()):new TranslatableText("projectez.armorHUD.unbreakable"); }
    private static void drawHUDItemCount(MatrixStack ms, int x, int y, ItemStack stack) {
        ms.push();
        itemRenderer.renderInGui(stack,x,y);
        ms.translate(x,y,0);
        textRenderer.draw(ms, getHUDItemCountNameText(stack), ITEM_RENDER_HEIGHT+2,5,0xffffffff);
        ms.pop();
    }
    private static void drawHUDItemCountDurability(MatrixStack ms, int x, int y, ItemStack stack) {
        final int maxDamage = stack.getMaxDamage(), damage = maxDamage-stack.getDamage();
        final int xn = x+ITEM_RENDER_HEIGHT+1, yn = y+textRenderer.fontHeight+1;
        final int xp = xn+DURABILITY_WIDTH,  yp = yn+6;
        final int dw = damage*(DURABILITY_WIDTH-4)/maxDamage;
        Text durabilityText = getHUDItemDamageText(stack);
        Text countText = getHUDItemCountNameText(stack);
        itemRenderer.renderInGui(stack,x,y);
        ms.push(); ms.translate(x,y,0);
        textRenderer.draw(ms, countText, ITEM_RENDER_HEIGHT+1,1,0xffffffff);
        ms.scale(0.5f, 0.5f, 0.5f);
        if (textRenderer.getWidth(countText)+textRenderer.getWidth(durabilityText)/2f > DURABILITY_WIDTH)
            textRenderer.draw(ms, durabilityText, 2 * (ITEM_RENDER_HEIGHT+DURABILITY_WIDTH) + 5, 2.5f*textRenderer.fontHeight, 0x88ffffff);
        else textRenderer.draw(ms, durabilityText, 2 * (ITEM_RENDER_HEIGHT+DURABILITY_WIDTH) - textRenderer.getWidth(durabilityText), textRenderer.fontHeight, 0x88ffffff);
        ms.pop();

        strokeRect(ms,xn,yn,xp,yp,0xaaffffff,1f);
        if (stack.isDamageable())
            fillRect(ms, xn + 2, yn + 2, xn + 2 + dw, yp - 2, 0xaaffffff);
        else fillRect(ms, xn + 2, yn + 2.5f, xn + 2 + dw, yp - 2.5f, 0x7799ffff);

    }

    public static Text getEnchantmentName(Enchantment enchantment, int level) {
        MutableText txt = new TranslatableText(enchantment.getTranslationKey());
        txt = txt.formatted(enchantment.isCursed()?Formatting.RED:Formatting.WHITE);
        if (level != 1 || enchantment.getMaxLevel() != 1) txt = txt.append(Text.of(" "+level));
        return txt;
    }

    public static void strokeRect(MatrixStack matrices, float xn, float yn, float xp, float yp, int color, float strokeWidth) {
        fillRect(matrices,xn,yn,xp,yn+strokeWidth,color);
        fillRect(matrices,xn,yn+strokeWidth,xn+strokeWidth,yp-strokeWidth,color);
        fillRect(matrices,xn,yp-strokeWidth,xp,yp,color);
        fillRect(matrices,xp-strokeWidth,yn+strokeWidth,xp,yp-strokeWidth,color);
    }
    public static void fillRect(MatrixStack matrices, float xStart, float yStart, float xEnd, float yEnd, int color) {
        fillRectGradient(matrices, xStart, yStart, xEnd, yEnd, color, color, false);
    }
    @SuppressWarnings("deprecation")
    public static void fillRectGradient(MatrixStack matrices, float xStart, float yStart, float xEnd, float yEnd, int colorStart, int colorEnd, boolean horizontal) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
        fillRectGradientInternal(matrices.peek().getModel(), bufferBuilder, xStart, yStart, xEnd, yEnd, colorStart, colorEnd, horizontal);
        tessellator.draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    private static void fillRectGradientInternal(Matrix4f matrix, BufferBuilder bufferBuilder, float xStart, float yStart, float xEnd, float yEnd, int colorStart, int colorEnd, boolean horizontal) {
        float f = (float)(colorStart >> 24 & 255) / 255.0F;
        float g = (float)(colorStart >> 16 & 255) / 255.0F;
        float h = (float)(colorStart >> 8 & 255) / 255.0F;
        float i = (float)(colorStart & 255) / 255.0F;
        float j = (float)(colorEnd >> 24 & 255) / 255.0F;
        float k = (float)(colorEnd >> 16 & 255) / 255.0F;
        float l = (float)(colorEnd >> 8 & 255) / 255.0F;
        float m = (float)(colorEnd & 255) / 255.0F;
        if (horizontal) {
            bufferBuilder.vertex(matrix, xEnd, yStart, 0).color(k, l, m, j).next();
            bufferBuilder.vertex(matrix, xStart, yStart, 0).color(g, h, i, f).next();
            bufferBuilder.vertex(matrix, xStart, yEnd, 0).color(g, h, i, f).next();
            bufferBuilder.vertex(matrix, xEnd, yEnd, 0).color(k, l, m, j).next();
        } else {
            bufferBuilder.vertex(matrix, xEnd, yStart, 0).color(g, h, i, f).next();
            bufferBuilder.vertex(matrix, xStart, yStart, 0).color(g, h, i, f).next();
            bufferBuilder.vertex(matrix, xStart, yEnd, 0).color(k, l, m, j).next();
            bufferBuilder.vertex(matrix, xEnd, yEnd, 0).color(k, l, m, j).next();
        }
    }
}
