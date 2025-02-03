package gg.auroramc.crafting.api.blueprint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all registered blueprints and provides fast lookup methods
 * based on different criteria.
 */
public class BlueprintRegistry {
    private final Map<BlueprintType, Map<String, Blueprint>> matrixLookup = new HashMap<>();
    private final Map<String, Blueprint> idLookup = new HashMap<>();
    private final Map<String, List<Blueprint>> categoryLookup = new HashMap<>();
    private final Map<String, List<Blueprint>> workbenchLookup = new HashMap<>();

    /**
     * Registers a blueprint into the registry.
     *
     * @param type      the type of the blueprint
     * @param category  the category of the blueprint
     * @param blueprint the blueprint to register
     */
    public void register(BlueprintType type, String category, Blueprint blueprint) {
        idLookup.put(blueprint.getId(), blueprint);
        matrixLookup.computeIfAbsent(type, k -> new HashMap<>()).put(BlueprintLookupGenerator.toKey(blueprint), blueprint);
        categoryLookup.computeIfAbsent(category, k -> new ArrayList<>()).add(blueprint);
        workbenchLookup.computeIfAbsent(blueprint.getWorkbench().getId(), k -> new ArrayList<>()).add(blueprint);
    }

    /**
     * Gets a blueprint by its ID.
     *
     * @param id the ID of the blueprint
     * @return the blueprint with the given ID, or null if not found
     */
    public @Nullable Blueprint getById(String id) {
        return idLookup.get(id);
    }

    /**
     * Gets a blueprint by its matrix lookup key.
     *
     * @param type            the type of the blueprint
     * @param matrixLookupKey the matrix lookup key
     * @return the blueprint with the given matrix lookup key, or null if there is no matching blueprint
     */
    public @Nullable Blueprint getByMatrix(BlueprintType type, String matrixLookupKey) {
        return matrixLookup.get(type).get(matrixLookupKey);
    }

    /**
     * Gets all blueprints of a specific category.
     *
     * @param category the category of the blueprints
     * @return a list of blueprints with the given category
     */
    public @NotNull List<Blueprint> getByCategory(String category) {
        return categoryLookup.getOrDefault(category, new ArrayList<>());
    }

    /**
     * Gets all blueprints that can be crafted on a specific workbench.
     *
     * @param workbenchId the ID of the workbench
     * @return a list of blueprints that can be crafted on the given workbench
     */
    public @NotNull List<Blueprint> getByWorkbench(String workbenchId) {
        return workbenchLookup.getOrDefault(workbenchId, new ArrayList<>());
    }

    /**
     * Clears all registered blueprints from the registry.
     */
    public void clear() {
        idLookup.clear();
        matrixLookup.clear();
        categoryLookup.clear();
        workbenchLookup.clear();
    }
}
