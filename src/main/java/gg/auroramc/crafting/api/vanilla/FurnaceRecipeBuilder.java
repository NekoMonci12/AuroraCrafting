package gg.auroramc.crafting.api.vanilla;

import gg.auroramc.crafting.api.blueprint.ChoiceType;
import org.bukkit.inventory.FurnaceRecipe;

public class FurnaceRecipeBuilder extends CookingRecipeBuilder<FurnaceRecipe>{
    public FurnaceRecipeBuilder(String id, ChoiceType choiceType) {
        super(id, choiceType);
    }

    public FurnaceRecipeBuilder(String id) {
        this(id, ChoiceType.EXACT);
    }

    public static FurnaceRecipeBuilder furnaceRecipe(String id, ChoiceType choiceType) {
        return new FurnaceRecipeBuilder(id, choiceType);
    }

    public static FurnaceRecipeBuilder furnaceRecipe(String id) {
        return new FurnaceRecipeBuilder(id, ChoiceType.EXACT);
    }

    @Override
    public FurnaceRecipe buildInternal() {
        return new FurnaceRecipe(key, result, choiceSelector.apply(input), experience, cookingTime);
    }
}
