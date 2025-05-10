package gg.auroramc.crafting.api.vanilla;

import gg.auroramc.crafting.api.blueprint.ChoiceType;
import org.bukkit.inventory.CampfireRecipe;

public class CampfireRecipeBuilder extends CookingRecipeBuilder<CampfireRecipe> {
    public CampfireRecipeBuilder(String id, ChoiceType choiceType) {
        super(id, choiceType);
    }

    public CampfireRecipeBuilder(String id) {
        this(id, ChoiceType.EXACT);
    }

    public static CampfireRecipeBuilder campfireRecipe(String id, ChoiceType choiceType) {
        return new CampfireRecipeBuilder(id, choiceType);
    }

    public static CampfireRecipeBuilder campfireRecipe(String id) {
        return new CampfireRecipeBuilder(id, ChoiceType.EXACT);
    }

    @Override
    public CampfireRecipe buildInternal() {
        return new CampfireRecipe(key, result, choiceSelector.apply(input), experience, cookingTime);
    }
}
