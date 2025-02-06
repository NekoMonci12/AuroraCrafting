package gg.auroramc.crafting.api.blueprint;

import gg.auroramc.crafting.api.vanilla.ShapedRecipeBuilder;
import gg.auroramc.crafting.api.vanilla.ShapelessRecipeBuilder;
import gg.auroramc.crafting.api.vanilla.SmithingRecipeBuilder;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;

public class BlueprintAdapter {
    public static ShapelessRecipe adapt(ShapelessBlueprint blueprint) {
        return ShapelessRecipeBuilder.shapelessRecipe(blueprint.getId())
                .category(blueprint.getVanillaCategory())
                .group(blueprint.getVanillaGroup())
                .ingredients(blueprint.getIngredientItems())
                .result(blueprint.getResultItem())
                .build();
    }

    public static ShapedRecipe adapt(ShapedBlueprint blueprint) {
        return ShapedRecipeBuilder.shapedRecipe(blueprint.getId())
                .category(blueprint.getVanillaCategory())
                .group(blueprint.getVanillaGroup())
                .ingredients(blueprint.getIngredientItems())
                .result(blueprint.getResultItem())
                .build();
    }

    public static SmithingTransformRecipe adapt(SmithingBlueprint blueprint) {
        return SmithingRecipeBuilder.smithingRecipe(blueprint.getId())
                .base(blueprint.getBaseItem())
                .addition(blueprint.getAdditionItem())
                .result(blueprint.getResultItem())
                .build();
    }
}
