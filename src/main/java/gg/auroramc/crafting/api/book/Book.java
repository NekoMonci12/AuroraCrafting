package gg.auroramc.crafting.api.book;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Book {
    @Getter
    private final BookCategory rootCategory = new BookCategory("root", this);
    private final Map<String, BookCategory> categories = new HashMap<>();

    void registerCategory(String id, BookCategory category) {
        if (categories.containsKey(id)) {
            throw new IllegalArgumentException("Category with ID " + id + " already exists");
        }
        categories.put(id, category);
    }

    public @Nullable BookCategory getCategory(String id) {
        return categories.get(id);
    }
}
