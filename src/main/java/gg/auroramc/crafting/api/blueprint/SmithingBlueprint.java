package gg.auroramc.crafting.api.blueprint;

import gg.auroramc.crafting.api.ItemPair;

public class SmithingBlueprint extends ShapedBlueprint {
    public SmithingBlueprint(String id) {
        super(id);
    }

    public ItemPair getTemplate() {
        return ingredients.getFirst();
    }

    public ItemPair getBase() {
        return ingredients.get(1);
    }

    public ItemPair getAddition() {
        return ingredients.getLast();
    }
}
