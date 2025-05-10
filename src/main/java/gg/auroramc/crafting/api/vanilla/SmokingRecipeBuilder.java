package gg.auroramc.crafting.api.vanilla;

import gg.auroramc.crafting.api.blueprint.ChoiceType;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.recipe.CookingBookCategory;

public class SmokingRecipeBuilder extends CookingRecipeBuilder<SmokingRecipe>{
    public SmokingRecipeBuilder(String id, ChoiceType choiceType) {
        super(id, choiceType);
        this.category = CookingBookCategory.FOOD;
    }

    public SmokingRecipeBuilder(String id) {
        this(id, ChoiceType.EXACT);
    }

    public static SmokingRecipeBuilder smokingRecipe(String id, ChoiceType choiceType) {
        return new SmokingRecipeBuilder(id, choiceType);
    }

    public static SmokingRecipeBuilder smokingRecipe(String id) {
        return new SmokingRecipeBuilder(id, ChoiceType.EXACT);
    }

    @Override
    public SmokingRecipe buildInternal() {
        return new SmokingRecipe(key, result, exactChoiceFor(input), experience, cookingTime);
    }
}
