package gg.auroramc.crafting.listener;

import gg.auroramc.aurora.api.util.ItemUtils;
import gg.auroramc.crafting.AuroraCrafting;
import gg.auroramc.crafting.api.blueprint.BlueprintType;
import gg.auroramc.crafting.util.InventoryUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CraftingListener implements Listener {
    private final AuroraCrafting plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareCrafting(PrepareItemCraftEvent event) {
        if (!(event.getViewers().getFirst() instanceof Player player)) return;

        var workbench = plugin.getWorkbenchRegistry().getCraftingTable();
        var context = workbench.createContext(player, event.getInventory());
        var blueprint = workbench.lookupBlueprint(context, BlueprintType.SHAPED, BlueprintType.SHAPELESS);

        boolean isAuroraRecipe = false;

        if (event.getRecipe() instanceof CraftingRecipe recipe) {
            if (recipe.getKey().getNamespace().equals("aurora")) {
                isAuroraRecipe = true;
            }
        }

        if (blueprint == null) {
            if (isAuroraRecipe) {
                event.getInventory().setResult(null);
            }
            return;
        }

        if (!blueprint.hasAccess(player)) {
            event.getInventory().setResult(null);
            return;
        }

        if (blueprint.getTimesCraftable(context) <= 0) {
            if (isAuroraRecipe) {
                event.getInventory().setResult(null);
            }
            return;
        }

        event.getInventory().setResult(blueprint.getResultItem(context));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftItem(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getClickedInventory() instanceof CraftingInventory craftingInventory)) return;

        var workbench = plugin.getWorkbenchRegistry().getCraftingTable();
        if (event.getSlot() != workbench.getResultSlot()) return;

        var context = workbench.createContext(player, craftingInventory);
        var blueprint = workbench.lookupBlueprint(context, BlueprintType.SHAPED, BlueprintType.SHAPELESS);

        if (blueprint == null) {
            return;
        }

        if (!blueprint.isStacked()) return;

        event.setCancelled(true);

        // Ignore dumb ways of crafting
        if (event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.LEFT) {
            return;
        }

        var timesCraftable = blueprint.getTimesCraftable(context);
        if (timesCraftable == 0) return;

        final var currentItem = event.getCurrentItem() != null ? event.getCurrentItem().clone() : ItemStack.empty();

        if (event.isShiftClick()) {
            int currentSpace = InventoryUtils.calculateSpaceForItem(player.getInventory(), currentItem);
            if (currentSpace < blueprint.getResult().amount()) {
                event.setCancelled(true);
                return;
            }
            final int availableSpace = currentSpace - blueprint.getResult().amount();
            final int timesCrafted = Math.min((availableSpace / blueprint.getResult().amount()) + 1, timesCraftable);

            if (timesCrafted == 1) {
                updateMatrix(player, event.getInventory(), blueprint.calcRemainingIngredientMatrix(context, 1));
                player.getInventory().addItem(currentItem);
            } else {
                var amount = timesCrafted * blueprint.getResult().amount();
                var stacks = ItemUtils.createStacksFromAmount(currentItem, amount);
                player.getInventory().addItem(stacks);
                updateMatrix(player, event.getInventory(), blueprint.calcRemainingIngredientMatrix(context, timesCrafted));
            }
        } else {
            if (event.getCursor().isEmpty()) {
                updateMatrix(player, event.getInventory(), blueprint.calcRemainingIngredientMatrix(context, 1));
                player.getScheduler().run(plugin, (t) -> player.setItemOnCursor(currentItem), null);
            } else {
                if (event.getCursor().isSimilar(currentItem)) {
                    var maxAmount = event.getCursor().getMaxStackSize() - event.getCursor().getAmount();
                    if (blueprint.getResult().amount() <= maxAmount) {
                        updateMatrix(player, event.getInventory(), blueprint.calcRemainingIngredientMatrix(context, 1));
                        player.getScheduler().run(plugin, (t) -> {
                            player.getItemOnCursor().setAmount(event.getCursor().getAmount() + blueprint.getResult().amount());
                        }, null);
                    }
                }
            }
        }
    }

    private void updateMatrix(Player player, Inventory inventory, ItemStack[] resultingMatrix) {
        var workbench = plugin.getWorkbenchRegistry().getCraftingTable();
        run(player, () -> {
            for (var i = 0; i < workbench.getMatrixSlots().size(); i++) {
                inventory.setItem(workbench.getMatrixSlots().get(i), resultingMatrix[i]);
            }
        });
    }

    private void run(Player player, Runnable runnable) {
        runnable.run();
        Bukkit.getRegionScheduler().run(plugin, player.getLocation(), (t) -> runnable.run());
    }
}
