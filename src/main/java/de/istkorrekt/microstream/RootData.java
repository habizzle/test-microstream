package de.istkorrekt.microstream;

import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class RootData {

    List<NodeData> nodes = new ArrayList<>();

    @Data
    public static class NodeData {
        private String value;
    }
}
