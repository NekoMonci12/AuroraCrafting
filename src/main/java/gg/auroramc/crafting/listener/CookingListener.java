package gg.auroramc.crafting.listener;

import gg.auroramc.crafting.AuroraCrafting;
import gg.auroramc.crafting.api.workbench.vanilla.CookingWorkbench;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

public class CookingListener implements Listener {
    private final AuroraCrafting plugin;

    public CookingListener(AuroraCrafting plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSmeltStart(FurnaceStartSmeltEvent event) {
        if (!(event.getRecipe().getInputChoice() instanceof RecipeChoice.MaterialChoice)) {
            return;
        }

        var workbench = getWorkbench(event.getBlock());
        if (workbench == null) return;

        var input = event.getSource();
        var context = workbench.createContext(null, input);
        var blueprint = workbench.lookupBlueprint(context);

        if (blueprint != null) {
            event.setTotalCookTime(blueprint.getVanillaOptions().getCookingTime());
        } else if (workbench.matchesRegisteredVanillaRecipe(context)) {
            event.setTotalCookTime(Integer.MAX_VALUE);
        }
    }

    @EventHandler
    public void onSmeltBurn(FurnaceBurnEvent event) {
        var input = ((Furnace) event.getBlock().getState()).getInventory().getSmelting();

        var workbench = getWorkbench(event.getBlock());
        if (workbench == null) return;

        var context = workbench.createContext(null, input);
        var blueprint = workbench.lookupBlueprint(context);

        if (blueprint == null && workbench.matchesRegisteredVanillaRecipe(context)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSmeltDone(FurnaceSmeltEvent event) {
        var workbench = getWorkbench(event.getBlock());
        if (workbench == null) return;

        var input = event.getSource();
        var context = workbench.createContext(null, input);
        var blueprint = workbench.lookupBlueprint(context);

        if (blueprint != null) {
            event.setResult(blueprint.getResultItem(context));
            var furnace = ((Furnace) event.getBlock().getState());
        } else if (workbench.matchesRegisteredVanillaRecipe(context)) {
            event.setResult(ItemStack.empty());
            event.setCancelled(true);
        }
    }

    private CookingWorkbench getWorkbench(Block block) {
        return switch (block.getType()) {
            case FURNACE -> plugin.getWorkbenchRegistry().getFurnace();
            case BLAST_FURNACE -> plugin.getWorkbenchRegistry().getBlastFurnace();
            case SMOKER -> plugin.getWorkbenchRegistry().getSmoker();
            case CAMPFIRE -> plugin.getWorkbenchRegistry().getCampfire();
            default -> null;
        };
    }


}
