package gg.auroramc.crafting.menu;

import gg.auroramc.aurora.api.menu.AuroraMenu;
import gg.auroramc.aurora.api.menu.ItemBuilder;
import gg.auroramc.crafting.AuroraCrafting;
import gg.auroramc.crafting.api.blueprint.Blueprint;
import gg.auroramc.crafting.api.workbench.custom.CustomWorkbench;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class RecipeMenu {
    private final AuroraCrafting plugin;
    private final Player player;
    private final Blueprint blueprint;
    private final Runnable backAction;

    public static RecipeMenu recipeMenu(AuroraCrafting plugin, Player player, Blueprint recipe, Runnable backAction) {
        return new RecipeMenu(plugin, player, recipe, backAction);
    }

    public void open() {
        var workbench = blueprint.getWorkbench();

        if (workbench instanceof CustomWorkbench customWorkbench) {
            open(customWorkbench);
        }
        // TODO: handle vanilla workbenches
    }

    private void open(CustomWorkbench workbench) {
        var mc = plugin.getConfigManager().getRecipeViewConfig();
        var mcc = plugin.getConfigManager().getRecipeBookCategoryConfig();

        var menu = new AuroraMenu(player, mc.getTitle(), workbench.getMenuOptions().getRows() * 9, false);
        menu.addFiller(ItemBuilder.of(workbench.getMenuOptions().getFillerItem()).toItemStack(player));

        var ingredientItems = blueprint.getIngredientItems();
        var ingredientTypes = blueprint.getIngredients();

        for (int i = 0; i < workbench.getMatrixSlots().size(); i++) {
            var slot = workbench.getMatrixSlots().get(i);
            var item = i < ingredientItems.size() ? ingredientItems.get(i) : ItemStack.empty();
            var type = i < ingredientTypes.size() ? ingredientTypes.get(i) : null;
            if (type != null) {
                var recipe = plugin.getBlueprintRegistry().getBlueprintsFor(type.id()).getFirst();
                if (recipe != null && (recipe.hasAccess(player) || !mcc.getSecretRecipeDisplay().getEnabled())) {
                    menu.addItem(ItemBuilder.item(item).amount(item.getAmount()).slot(slot).build(player), (e) -> {
                        RecipeMenu.recipeMenu(plugin, player, recipe, () -> RecipeMenu.recipeMenu(plugin, player, this.blueprint, this.backAction).open()).open();
                    });
                    continue;
                }
            }

            menu.addItem(ItemBuilder.item(item).amount(item.getAmount()).slot(slot).build(player));
        }

        Integer resultSlot;
        resultSlot = mc.getResultSlot().get(blueprint.getWorkbench().getId());
        if (resultSlot == null) {
            resultSlot = mc.getResultSlot().get("default");
        }
        if (resultSlot == null) {
            resultSlot = workbench.getResultSlot();
        }

        menu.addItem(ItemBuilder.item(blueprint.getResultItem()).amount(blueprint.getResult().amount()).slot(resultSlot).build(player));

        if (backAction != null && blueprint.getCategory() != null) {
            menu.addItem(ItemBuilder.of(mc.getItems().get("back")).build(player), (e) -> {
                backAction.run();
            });
        }

        for (var item : mc.getCustomItems().values()) {
            menu.addItem(ItemBuilder.of(item).build(player));
        }

        menu.open();
    }
}
