package com.folkcargo.service;

import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * Common CRUD plumbing shared by every entity service. Concrete services
 * only need to say which {@link ObservableList} backs them and how to read
 * an entity's id; everything else (add/update/delete/lookup) is provided
 * here so the UI layer can talk to every module through the same shape of
 * API.
 *
 * @param <T> the entity type managed by this service
 */
public abstract class AbstractCrudService<T> {

    private final ObservableList<T> backingList;
    private final ToIntFunction<T> idExtractor;

    protected AbstractCrudService(ObservableList<T> backingList, ToIntFunction<T> idExtractor) {
        this.backingList = backingList;
        this.idExtractor = idExtractor;
    }

    /** All records currently held for this entity, live-bindable to a TableView. */
    public ObservableList<T> getAll() {
        return backingList;
    }

    /** Adds a brand-new record (already carrying a freshly-issued id). */
    public void add(T item) {
        backingList.add(item);
    }

    /**
     * Persists in-place edits made to {@code item}. Because the item is the
     * same object reference already held in the list, this replaces it at
     * its own index purely so that {@code ObservableList} fires a proper
     * update event and any bound {@code TableView} repaints that row.
     */
    public void update(T item) {
        int index = backingList.indexOf(item);
        if (index >= 0) {
            backingList.set(index, item);
        }
    }

    public void delete(T item) {
        backingList.remove(item);
    }

    public Optional<T> findById(int id) {
        return backingList.stream().filter(t -> idExtractor.applyAsInt(t) == id).findFirst();
    }

    public int count() {
        return backingList.size();
    }
}
