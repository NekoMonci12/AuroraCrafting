package gg.auroramc.crafting.api.vanilla;

import gg.auroramc.crafting.api.blueprint.ChoiceType;
import org.bukkit.inventory.BlastingRecipe;

public class BlastingRecipeBuilder extends CookingRecipeBuilder<BlastingRecipe> {
    public BlastingRecipeBuilder(String id, ChoiceType choiceType) {
        super(id, choiceType);
    }

    public BlastingRecipeBuilder(String id) {
        super(id, ChoiceType.EXACT);
    }

    @Override
    public BlastingRecipe buildInternal() {
        return new BlastingRecipe(key, result, choiceSelector.apply(input), experience, cookingTime);
    }

    public static BlastingRecipeBuilder blastingRecipe(String id, ChoiceType choiceType) {
        return new BlastingRecipeBuilder(id, choiceType);
    }

    public static BlastingRecipeBuilder blastingRecipe(String id) {
        return new BlastingRecipeBuilder(id, ChoiceType.EXACT);
    }
}
