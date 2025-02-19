package gg.auroramc.crafting.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import gg.auroramc.crafting.AuroraCrafting;
import gg.auroramc.crafting.menu.RecipeBookMenu;
import gg.auroramc.crafting.menu.RecipeCategoryMenu;
import gg.auroramc.crafting.menu.RecipeMenu;
import org.bukkit.entity.Player;

@CommandAlias("%recipesAlias")
public class RecipesCommand extends BaseCommand {
    private final AuroraCrafting plugin;

    public RecipesCommand(AuroraCrafting plugin) {
        this.plugin = plugin;
    }

    @Default
    @Description("Opens the recipes menu")
    @CommandPermission("aurora.crafting.recipes")
    @CommandCompletion("@recipes @nothing")
    public void onMenu(Player player, @Optional String recipeId) {
        if (recipeId == null) {
            RecipeBookMenu.recipeBookMenu(plugin, player).open();
            return;
        }
        var blueprint = plugin.getBlueprintRegistry().getBlueprint(recipeId);
        if (blueprint == null) return;

        if (blueprint.hasAccess(player) || !plugin.getConfigManager().getRecipeBookCategoryConfig().getSecretRecipeDisplay().getEnabled()) {
            RecipeMenu.recipeMenu(plugin, player, blueprint, () -> RecipeCategoryMenu.recipeCategoryMenu(plugin, player, blueprint.getCategory().getFirst()).open()).open();
        }
    }
}
