package de.istkorrekt.microstream;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

import java.nio.file.Path;

public class Repository {

    private final EmbeddedStorageManager storageManager;

    public Repository(Path location) {
        storageManager = EmbeddedStorage.start(location);
    }

    public Root load() {
        Object object = storageManager.root();
        if (object instanceof Root) {
            return (Root) object;
        }
        if (object == null) {
            Root root = new Root();
            storageManager.setRoot(root);
            return root;
        }
        throw new IllegalStateException("Invalid root of type " + object.getClass());
    }

    public void save() {
        storageManager.storeRoot();
    }

    public void saveNode(Node node) {
        storageManager.store(node);
    }

    public void wipe() {
        storageManager.setRoot(null);
        save();
    }

    public void shutdown() {
        storageManager.shutdown();
    }
}
