package gg.auroramc.crafting.api.vanilla;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.List;
import java.util.function.Function;

public class ShapelessRecipeBuilder extends RecipeBuilder<ShapelessRecipeBuilder, ShapelessRecipe> {
    private CraftingBookCategory category = CraftingBookCategory.MISC;
    private String group = null;
    private List<ItemStack> ingredients;

    public ShapelessRecipeBuilder(String id) {
        super(id);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(String id) {
        return new ShapelessRecipeBuilder(id);
    }

    public ShapelessRecipeBuilder category(CraftingBookCategory category) {
        this.category = category;
        return this;
    }

    public ShapelessRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    public ShapelessRecipeBuilder ingredients(List<ItemStack> ingredients) {
        this.ingredients = List.copyOf(ingredients);
        return this;
    }

    @Override
    public ShapelessRecipe build() {
        return buildInternal(key, this::dynamicChoiceFor);
    }

    private ShapelessRecipe buildInternal(NamespacedKey key, Function<ItemStack, RecipeChoice> choiceSelector) {
        var recipe = new ShapelessRecipe(key, result);

        if (group != null) {
            recipe.setGroup(group);
        }
        recipe.setCategory(category);

        for (var ingredient : ingredients) {
            recipe.addIngredient(choiceSelector.apply(ingredient));
        }

        return recipe;
    }
}
