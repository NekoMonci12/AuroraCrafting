package gg.auroramc.crafting.config;

import gg.auroramc.aurora.api.config.AuroraConfig;
import gg.auroramc.aurora.api.config.decorators.IgnoreField;
import gg.auroramc.crafting.AuroraCrafting;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SmithingRecipesConfig extends AuroraConfig {
    @IgnoreField
    private String sourcePath;

    private List<RecipeConfig> recipes = new ArrayList<>();

    @Getter
    public static final class RecipeConfig {
        private String id;
        private String result;
        private String template;
        private String base;
        private String addition;

        @Setter
        @IgnoreField
        private String sourcePath;
    }

    public SmithingRecipesConfig(File file) {
        super(file);
        var target = "blueprints" + File.separator;
        var absPath = file.getAbsolutePath();
        var index = absPath.indexOf(target);
        this.sourcePath = absPath.substring(index + target.length()).replace(".yml", "");
    }

    @Override
    public void load() {
        super.load();
        recipes.forEach(recipe -> recipe.setSourcePath(sourcePath));

        var it = recipes.iterator();

        while (it.hasNext()) {
            var recipe = it.next();

            if (recipe.id == null) {
                it.remove();
                AuroraCrafting.logger().severe("Smithing transform recipe in " + sourcePath + " has no id removing...");
            }
            if(recipe.result == null) {
                it.remove();
                AuroraCrafting.logger().severe("Smithing transform Recipe in " + sourcePath + " with id " + recipe.id + " has no result removing...");
            }
        }
    }
}
