package gg.auroramc.crafting.api.workbench;

import java.util.List;

public class Furnace extends Workbench {
    public Furnace() {
        super("vanilla-furnace", 1, List.of(0));
        square = false;
    }
}
