package de.istkorrekt.microstream;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class Root {

    List<Node> nodes = new ArrayList<>();
}
