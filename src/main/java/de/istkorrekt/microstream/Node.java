package de.istkorrekt.microstream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import one.microstream.reference.Lazy;

@Data
public class Node {
    @NonNull
    private String value;

    @EqualsAndHashCode.Exclude
    private Lazy<BigFatData> bigFatData = Lazy.Reference(null);

    public static Node withBigFatData(String value) {
        Node node = new Node(value);
        node.setBigFatData(Lazy.Reference(BigFatData.random()));
        return node;
    }
}
