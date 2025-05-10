package gg.auroramc.crafting.api.workbench.vanilla;

import java.util.List;

public class Furnace extends CookingWorkbench {
    public Furnace() {
        super("vanilla-furnace", 0, List.of(1), VanillaType.FURNACE);
    }
}
