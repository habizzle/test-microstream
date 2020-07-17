package de.istkorrekt.microstream;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

import java.nio.file.Path;

public class Repository {

    private final EmbeddedStorageManager storageManager;

    public Repository(Path location) {
        storageManager = EmbeddedStorage.start(location);
    }

    public RootData load() {
        Object object = storageManager.root();
        if (object instanceof RootData) {
            return (RootData) object;
        }
        if (object == null) {
            RootData root = new RootData();
            storageManager.setRoot(root);
            return root;
        }
        throw new IllegalStateException("Invalid root of type " + object.getClass());
    }

    public void save() {
        storageManager.storeRoot();
    }

    public void wipe() {
        storageManager.setRoot(null);
        save();
    }
}
