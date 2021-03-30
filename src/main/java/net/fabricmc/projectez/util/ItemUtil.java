package net.fabricmc.projectez.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemUtil {
    public static ListTag getEnchantedBookEnchants(ItemStack stack) {
        return stack.getTag()!=null?stack.getTag().getList("Enchantments", 10) : new ListTag();
    }
    public static List<LeveledEnchant> getEnchantments(ItemStack stack) {
        final ListTag enchantmentsTag =
                stack.getItem() == Items.ENCHANTED_BOOK ?
                        ItemUtil.getEnchantedBookEnchants(stack) :
                        stack.getEnchantments();
        final List<LeveledEnchant> enchantments = new ArrayList<>();
        for (int i = 0; i < enchantmentsTag.size(); ++i) {
            CompoundTag compoundTag = enchantmentsTag.getCompound(i);
            Registry.ENCHANTMENT.getOrEmpty(Identifier.tryParse(compoundTag.getString("id"))).ifPresent(ench ->
                    enchantments.add(new LeveledEnchant(ench, compoundTag.getInt("lvl")))
            );
        }
        return enchantments;
    }
    public static class LeveledEnchant {
        final int level;
        final Enchantment enchantment;
        public LeveledEnchant(Enchantment enchantment, int level) {
            this.level = level;
            this.enchantment = enchantment;
        }
    }

    public static void buildModifiedPotionTooltip(ItemStack stack, List<Text> tooltip, float f) {
        List<StatusEffectInstance> potionEffects = PotionUtil.getPotionEffects(stack);
        TranslatableText mutableText; StatusEffect statusEffect;
        if (!potionEffects.isEmpty()) {
            for(StatusEffectInstance statusEffectInstance : potionEffects) {
                mutableText = new TranslatableText(statusEffectInstance.getTranslationKey());
                statusEffect = statusEffectInstance.getEffectType();

                if (statusEffectInstance.getAmplifier() > 0)
                    mutableText = new TranslatableText("potion.withAmplifier", mutableText,
                            new TranslatableText("potion.potency." + statusEffectInstance.getAmplifier()));

                if (statusEffectInstance.getDuration() > 20)
                    mutableText = new TranslatableText("potion.withDuration", mutableText,
                            StatusEffectUtil.durationToString(statusEffectInstance, f));

                tooltip.add(mutableText.formatted(statusEffect.isBeneficial()?Formatting.WHITE:Formatting.RED));
            }
        } else
            tooltip.add((new TranslatableText("effect.none")).formatted(Formatting.GRAY));
    }
}
