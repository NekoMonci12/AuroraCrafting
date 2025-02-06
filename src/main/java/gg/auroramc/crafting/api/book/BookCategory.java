package gg.auroramc.crafting.api.book;

import gg.auroramc.crafting.api.blueprint.Blueprint;
import lombok.Getter;

import java.util.*;

public class BookCategory {
    @Getter
    private final String id;
    @Getter
    private final Book book;
    private final Map<String, BookCategory> categories = new HashMap<>();
    @Getter
    private final List<Blueprint> blueprints = new ArrayList<>();

    public BookCategory(String id, Book book) {
        this.id = id;
        this.book = book;
    }

    public void addSubCategory(BookCategory category) {
        if (!blueprints.isEmpty()) {
            throw new IllegalStateException("Cannot add subcategory to a category with blueprints");
        }
        book.registerCategory(category.getId(), category);
        categories.put(category.getId(), category);
    }

    public void addBlueprint(Blueprint blueprint) {
        if (!categories.isEmpty()) {
            throw new IllegalStateException("Cannot add blueprint to a category with subcategories");
        }
        blueprints.add(blueprint);
    }

    public Collection<BookCategory> getCategories() {
        return categories.values();
    }
}
