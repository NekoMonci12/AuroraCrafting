package gg.auroramc.crafting.api.vanilla;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;

import java.util.function.Function;

public class SmithingRecipeBuilder extends RecipeBuilder<SmithingRecipeBuilder, SmithingTransformRecipe> {
    private ItemStack template = null;
    private ItemStack base = null;
    private ItemStack addition = null;

    public SmithingRecipeBuilder(String id) {
        super(id);
    }

    public static SmithingRecipeBuilder smithingRecipe(String id) {
        return new SmithingRecipeBuilder(id);
    }

    public SmithingRecipeBuilder template(ItemStack template) {
        this.template = template;
        return this;
    }

    public SmithingRecipeBuilder base(ItemStack base) {
        this.base = base;
        return this;
    }

    public SmithingRecipeBuilder addition(ItemStack addition) {
        this.addition = addition;
        return this;
    }

    @Override
    public SmithingTransformRecipe build() {
        return buildInternal(key, this::dynamicChoiceFor);
    }

    private SmithingTransformRecipe buildInternal(NamespacedKey key, Function<ItemStack, RecipeChoice> choiceSelector) {
        return new SmithingTransformRecipe(
                key,
                result,
                choiceSelector.apply(template),
                choiceSelector.apply(base),
                choiceSelector.apply(addition),
                true
        );
    }
}

