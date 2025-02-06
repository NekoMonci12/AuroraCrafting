package gg.auroramc.crafting.api.workbench;

import gg.auroramc.crafting.api.blueprint.Blueprint;
import gg.auroramc.crafting.api.blueprint.BlueprintType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Workbench {
    @Getter
    private String id;

    private final Map<String, Blueprint> blueprints = new HashMap<>();
    private final Map<BlueprintType, Map<String, Blueprint>> matrixLookup = new HashMap<>();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Workbench workbench = (Workbench) object;
        return Objects.equals(id, workbench.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
