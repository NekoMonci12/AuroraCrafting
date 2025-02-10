package gg.auroramc.crafting.api.vanilla;

import gg.auroramc.crafting.api.blueprint.CraftingBlueprint;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.List;
import java.util.function.Function;

public class ShapelessRecipeBuilder extends CraftingRecipeBuilder<ShapelessRecipe, ShapelessRecipeBuilder> {
    private List<ItemStack> ingredients;

    public ShapelessRecipeBuilder(String id, CraftingBlueprint.ChoiceType choiceType) {
        super(id, choiceType);
    }

    public static ShapelessRecipeBuilder shapelessRecipe(String id, CraftingBlueprint.ChoiceType choiceType) {
        return new ShapelessRecipeBuilder(id, choiceType);
    }

    public ShapelessRecipeBuilder ingredients(List<ItemStack> ingredients) {
        this.ingredients = List.copyOf(ingredients);
        return this;
    }

    @Override
    public ShapelessRecipe build() {
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
