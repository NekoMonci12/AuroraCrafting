package gg.auroramc.crafting.listener;

import gg.auroramc.aurora.api.AuroraAPI;
import gg.auroramc.crafting.AuroraCrafting;
import gg.auroramc.crafting.api.workbench.vanilla.CookingWorkbench;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.RecipeChoice;

public class SmeltingListener implements Listener {
    private final AuroraCrafting plugin;

    public SmeltingListener(AuroraCrafting plugin) {
        this.plugin = plugin;
    }

    private boolean isSmeltingResultExtraction(final InventoryClickEvent event, final InventoryType inventoryType) {
        return (inventoryType == InventoryType.FURNACE
                || inventoryType == InventoryType.SMOKER
                || inventoryType == InventoryType.BLAST_FURNACE)
                && event.getWhoClicked() instanceof Player
                && event.getRawSlot() == 2
                && !InventoryUtils.isEmptySlot(event.getCurrentItem());
    }

    private int calculateTakeAmount(final InventoryClickEvent event) {
        final ItemStack result = event.getCurrentItem();
        final PlayerInventory inventory = event.getWhoClicked().getInventory();
        switch (event.getClick()) {
            case SHIFT_LEFT:
            case SHIFT_RIGHT:
                return Math.min(InventoryUtils.calculateSpaceForItem(inventory, result), result.getAmount());
            case CONTROL_DROP:
                return InventoryUtils.calculateSpaceForItem(inventory, result);
            case NUMBER_KEY:
                return InventoryUtils.calculateSwapCraftAmount(result, inventory.getItem(event.getHotbarButton()));
            case SWAP_OFFHAND:
                return InventoryUtils.calculateSwapCraftAmount(result, inventory.getItemInOffHand());
            case DROP:
                return 1;
            case RIGHT:
                if (InventoryUtils.isEmptySlot(event.getCursor())) {
                    return (result.getAmount() + 1) / 2;
                }
            case LEFT:
                return InventoryUtils.calculateSimpleCraftAmount(result, event.getCursor());
            default:
                return 0;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSmelting(InventoryClickEvent event) {
        InventoryType inventoryType = event.getInventory().getType();

        if (event.getWhoClicked() instanceof Player player && isSmeltingResultExtraction(event, inventoryType)) {
            if (event.getInventory().getHolder() instanceof Furnace furnace) {
                var workbench = getWorkbench(inventoryType);
                if (workbench == null) return;

                var completedRecipes = furnace.getRecipesUsed();
                int taken = calculateTakeAmount(event);
                float exp = 0;

                for (var recipeEntry : completedRecipes.entrySet()) {
                    var inputChoice = recipeEntry.getKey().getInputChoice();
                    if (inputChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
                        var blueprint = workbench.lookupBlueprint(workbench.createContext(player, materialChoice.getItemStack()));
                        if (blueprint != null) {
                            exp += blueprint.getVanillaOptions().getExperience() * taken;
                            break;
                        }
                    }
                }

                player.giveExp(Math.round(exp));
            }
        }
    }

    private CookingWorkbench getWorkbench(InventoryType inventoryType) {
        return switch (inventoryType) {
            case FURNACE -> plugin.getWorkbenchRegistry().getFurnace();
            case BLAST_FURNACE -> plugin.getWorkbenchRegistry().getBlastFurnace();
            case SMOKER -> plugin.getWorkbenchRegistry().getSmoker();
            default -> null;
        };
    }
}
