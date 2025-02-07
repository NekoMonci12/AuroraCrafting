package gg.auroramc.crafting.api.workbench;

import gg.auroramc.crafting.api.blueprint.Blueprint;
import gg.auroramc.crafting.api.blueprint.BlueprintType;
import gg.auroramc.crafting.util.InventoryUtils;
import gg.auroramc.crafting.util.Square;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class CustomWorkbench extends Workbench {
    private final boolean square;
    private final List<Integer> quickCraftSlots;

    public CustomWorkbench(String name, int type, List<Integer> slots, List<Integer> quickCraftSlots) {
        super(name, type, slots);
        this.quickCraftSlots = quickCraftSlots == null ? List.of() : quickCraftSlots;
        this.square = Square.isSquareCraftingArea(slots);
    }

    public @NotNull List<Blueprint> getCraftableBlueprints(Player player, int maxCount, BlueprintType... types) {
        var craftableBlueprints = new ArrayList<Blueprint>();

        var itemCount = InventoryUtils.buildItemCounts(player);

        for (var type : types) {
            for (var blueprint : categorizedBlueprints.computeIfAbsent(type, (k) -> new HashMap<>()).values()) {
                if (blueprint.hasAccess(player) && blueprint.getQuickCraftTimes(itemCount) > 0) {
                    craftableBlueprints.add(blueprint);
                    if (craftableBlueprints.size() >= maxCount) break;
                }
            }
        }

        return craftableBlueprints;
    }
}
