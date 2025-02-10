package gg.auroramc.crafting.api.workbench;

import gg.auroramc.crafting.api.workbench.custom.CustomWorkbench;
import gg.auroramc.crafting.api.workbench.vanilla.CraftingTable;
import gg.auroramc.crafting.api.workbench.vanilla.Furnace;
import gg.auroramc.crafting.api.workbench.vanilla.SmithingTable;
import gg.auroramc.crafting.api.workbench.vanilla.Station;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class WorkbenchRegistry {
    @Getter
    private boolean frozen = false;

    private final Map<String, CustomWorkbench> workbenches = new HashMap<>();

    private final Map<Station, Workbench> vanillaWorkbenches = Map.of(
            Station.CRAFTING_TABLE, new CraftingTable(),
            Station.SMITHING_TABLE, new SmithingTable(),
            Station.FURNACE, new Furnace()
    );

    public void registerWorkbench(CustomWorkbench workbench) {
        if (frozen) throw new IllegalStateException("Cannot register workbench after freezing");
        workbench.validate();
        workbenches.put(workbench.getId(), workbench);
    }

    public @Nullable Workbench getWorkbench(String id) {
        return workbenches.get(id);
    }

    public CraftingTable getCraftingTable() {
        return (CraftingTable) vanillaWorkbenches.get(Station.CRAFTING_TABLE);
    }

    public SmithingTable getSmithingTable() {
        return (SmithingTable) vanillaWorkbenches.get(Station.SMITHING_TABLE);
    }

    public Furnace getFurnace() {
        return (Furnace) vanillaWorkbenches.get(Station.FURNACE);
    }

    public Collection<CustomWorkbench> getCustomWorkbenches() {
        return workbenches.values();
    }

    public Collection<Workbench> getVanillaWorkbenches() {
        return vanillaWorkbenches.values();
    }

    public void freeze() {
        frozen = true;

        for (var workbench : workbenches.values()) {
            workbench.freeze();
        }

        for (var workbench : workbenches.values()) {
            workbench.freeze();
        }
    }
}
