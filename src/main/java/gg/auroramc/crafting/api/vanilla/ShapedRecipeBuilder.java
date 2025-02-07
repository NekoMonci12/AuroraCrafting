package gg.auroramc.crafting.api.vanilla;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ShapedRecipeBuilder extends RecipeBuilder<ShapedRecipeBuilder, ShapedRecipe> {
    private CraftingBookCategory category = CraftingBookCategory.MISC;
    private String group = null;
    private final String[] shape = new String[]{"012", "345", "678"};
    private Map<Character, ItemStack> ingredients;

    public ShapedRecipeBuilder(String id) {
        super(id);
    }

    public static ShapedRecipeBuilder shapedRecipe(String id) {
        return new ShapedRecipeBuilder(id);
    }

    public ShapedRecipeBuilder category(CraftingBookCategory category) {
        this.category = category;
        return this;
    }

    public ShapedRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    public ShapedRecipeBuilder ingredients(List<ItemStack> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            this.ingredients.put(String.valueOf(i).toCharArray()[0], ingredients.get(i));
        }

        return this;
    }

    @Override
    public ShapedRecipe build() {
        return buildInternal(key, this::dynamicChoiceFor);
    }

    private ShapedRecipe buildInternal(NamespacedKey key, Function<ItemStack, RecipeChoice> choiceSelector) {
        var recipe = new ShapedRecipe(key, result);

        if (group != null) {
            recipe.setGroup(group);
        }
        recipe.setCategory(category);
        recipe.shape(shape);

        for (var entry : ingredients.entrySet()) {
            recipe.setIngredient(entry.getKey(), choiceSelector.apply(entry.getValue()));
        }

        return recipe;
    }
}
