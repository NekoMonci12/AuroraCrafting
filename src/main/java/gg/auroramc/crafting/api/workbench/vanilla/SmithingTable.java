package gg.auroramc.crafting.api.workbench.vanilla;

import gg.auroramc.crafting.api.blueprint.SmithingBlueprint;

import java.util.List;

public class SmithingTable extends VanillaWorkbench<SmithingBlueprint> {
    public SmithingTable() {
        super("vanilla-smithing-table", 0, List.of(1, 2, 3), VanillaType.SMITHING_TABLE);
    }
}
