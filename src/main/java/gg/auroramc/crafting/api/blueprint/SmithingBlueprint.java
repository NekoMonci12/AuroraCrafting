package gg.auroramc.crafting.api.blueprint;

import gg.auroramc.crafting.api.ItemPair;
import org.bukkit.inventory.ItemStack;

public class SmithingBlueprint extends ShapedBlueprint {
    public SmithingBlueprint(String id) {
        super(id);
    }

    public ItemPair getTemplate() {
        return ingredients.getFirst();
    }

    public ItemStack getTemplateItem() {
        return ingredientItems.getFirst();
    }

    public ItemPair getBase() {
        return ingredients.get(1);
    }

    public ItemStack getBaseItem() {
        return ingredientItems.get(1);
    }

    public ItemPair getAddition() {
        return ingredients.getLast();
    }

    public ItemStack getAdditionItem() {
        return ingredientItems.getLast();
    }
}
