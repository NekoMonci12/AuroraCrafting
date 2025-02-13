package gg.auroramc.crafting.parser;

import gg.auroramc.aurora.api.AuroraAPI;
import gg.auroramc.aurora.api.item.TypeId;
import gg.auroramc.crafting.AuroraCrafting;
import gg.auroramc.crafting.api.ItemPair;
import gg.auroramc.crafting.api.blueprint.Blueprint;
import gg.auroramc.crafting.api.blueprint.CookingBlueprint;
import gg.auroramc.crafting.api.blueprint.SmithingBlueprint;
import gg.auroramc.crafting.api.workbench.Workbench;
import gg.auroramc.crafting.config.CookingRecipesConfig;
import gg.auroramc.crafting.config.CraftingRecipesConfig;
import gg.auroramc.crafting.config.SmithingRecipesConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.recipe.CookingBookCategory;

@RequiredArgsConstructor
public class BlueprintParser {
    private final Workbench workbench;
    private final String filePath;
    private final String recipeId;

    public static BlueprintParser create(Workbench workbench, String filePath, String recipeId) {
        return new BlueprintParser(workbench, filePath, recipeId);
    }

    public Blueprint parse(CraftingRecipesConfig.RecipeConfig config) {
        return null;
    }

    public Blueprint parse(CookingRecipesConfig.RecipeConfig config, CookingBlueprint.Type type) {
        // TODO: parse display options
        return CookingBlueprint.cookingBlueprint(workbench, config.getId())
                .type(type)
                .input(parseItemPair(config.getInput(), Material.BARRIER))
                .vanillaOptions(
                        CookingBlueprint.VanillaOptions.builder()
                                .cookingTime(config.getCookingTime())
                                .experience(config.getExperience())
                                .group(config.getGroup())
                                .category(CookingBookCategory.valueOf(config.getCategory().toUpperCase()))
                                .build())
                // TODO: get recipe book category from somewhere
                .category(null)
                .result(parseItemPair(config.getResult(), Material.AIR));
    }

    public Blueprint parse(SmithingRecipesConfig.RecipeConfig config) {
        // TODO: parse merge options
        // TODO: parse permission
        // TODO: parse display options
        return SmithingBlueprint.smithingBlueprint(workbench, config.getId())
                .template(parseItemPair(config.getTemplate(), Material.BARRIER))
                .base(parseItemPair(config.getBase(), Material.BARRIER))
                .addition(parseItemPair(config.getAddition(), Material.BARRIER))
                //.permission(config.getPermission())
                // TODO: get recipe book category from somewhere
                .category(null)
                .onCraft((player, result, amount) -> {
                    // TODO: Command dispatcher from config file
                })
                .result(parseItemPair(config.getResult(), Material.AIR));
    }

    private ItemPair parseItemPair(String input, Material invalidMaterial) {
        var split = input.split("/");
        if (split[0].isEmpty()) {
            return new ItemPair(TypeId.from(Material.AIR), 0);
        }
        var pair = split.length > 1
                ? new ItemPair(TypeId.fromDefault(split[0]), Integer.parseInt(split[1]))
                : new ItemPair(TypeId.fromDefault(split[0]), 1);

        var itemStack = AuroraAPI.getItemManager().resolveItem(pair.id());
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            AuroraCrafting.logger().severe("Can't validate item id: " + pair.id() + " in recipe: " + recipeId);
            return new ItemPair(TypeId.from(invalidMaterial), 1);
        }
        return pair;
    }
}
