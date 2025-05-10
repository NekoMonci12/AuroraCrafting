package gg.auroramc.crafting.api.workbench.vanilla;

import java.util.List;

public class Campfire extends CookingWorkbench {
    public Campfire() {
        super("vanilla-campfire", 0, List.of(1), VanillaType.CAMPFIRE);
    }
}
