package de.istkorrekt.microstream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryShould {

    private static final Path LOCATION = Path.of("build/storage-test");

    private Repository cut;

    @BeforeEach
    void setUp() {
        deleteStorage();
        cut = new Repository(LOCATION);
    }

    @AfterAll
    static void tearDown() {
        deleteStorage();
    }

    @Test
    void load_new_root_data_initially() {
        RootData data = cut.load();

        assertThat(data).isEqualTo(new RootData());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void deleteStorage() {
        try {
            Files.walk(LOCATION)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}