package de.istkorrekt.microstream;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryShould {

    private static final Path LOCATION = Path.of("build/storage-test");

    private Repository cut;

    @BeforeEach
    void setUp() {
        deleteStorage();
        cut = new Repository(LOCATION);
    }

    @AfterEach
    void tearDown() {
        cut.shutdown();
    }

    @Test
    void load_new_root_data_initially() {
        assertThatLoaded()
                .isEqualTo(new Root());
    }

    @Test
    void load_stored_root() {
        Node node = new Node("node");

        store(node);

        assertThatLoaded()
                .isEqualTo(rootDataWithNodes(node));
    }

    @Test
    void not_load_big_fat_data_from_node() {
        Node node = Node.withBigFatData("node");
        store(node);
        reconnect();

        assertThatLoadedNode("node")
                .matches(it -> !it.getBigFatData().isLoaded(), "is not loaded");
    }

    @Test
    void store_only_saved_node() {
        Node node = new Node("node");
        Node otherNode = new Node("otherNode");
        store(node, otherNode);
        node.setValue("updatedNode");

        cut.saveNode(node);

        assertThatLoaded()
                .isEqualTo(rootDataWithNodes("updatedNode", "otherNode"));
    }

    private ObjectAssert<Root> assertThatLoaded() {
        return assertThat(cut.load());
    }

    private ObjectAssert<Node> assertThatLoadedNode(String value) {
        Root root = cut.load();
        Node node = root.getNodes().stream()
                .filter(it -> it.getValue().equals(value))
                .findFirst().orElseThrow();
        return assertThat(node);
    }

    private static Root rootDataWithNodes(Node... nodes) {
        Root root = new Root();
        root.getNodes().addAll(Arrays.asList(nodes));
        return root;
    }

    private static Root rootDataWithNodes(String... values) {
        Root root = new Root();
        root.getNodes().addAll(Arrays.stream(values).map(Node::new).collect(Collectors.toList()));
        return root;
    }

    private void store(Node... nodes) {
        Root root = cut.load();
        Arrays.stream(nodes).forEach(node -> root.getNodes().add(node));
        cut.save();
    }

    private void reconnect() {
        cut.shutdown();
        cut = new Repository(LOCATION);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void deleteStorage() {
        if (LOCATION.toFile().exists()) {
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
}