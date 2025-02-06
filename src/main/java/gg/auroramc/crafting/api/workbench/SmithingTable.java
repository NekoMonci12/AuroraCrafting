package gg.auroramc.crafting.api.workbench;

import java.util.List;

public class SmithingTable extends Workbench {
    public SmithingTable() {
        super("vanilla-smithing-table", 2, List.of(0, 1, 2));
        square = false;
    }
}
