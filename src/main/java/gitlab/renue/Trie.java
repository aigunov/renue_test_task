package gitlab.renue;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String value, int airportId) {
        Node current = root;
        for (char c : value.toCharArray()) {
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
        }
        current.airportIds.add(airportId);
    }

    public List<Integer> search(String prefix) {
        Node current = root;
        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }
            current = current.children.get(c);
        }
        return collectAirportIds(current);
    }

    private List<Integer> collectAirportIds(Node node) {
        List<Integer> result = new ArrayList<>();
        collectAirportIdsRecursive(node, result);
        return result;
    }

    private void collectAirportIdsRecursive(Node node, List<Integer> result) {
        if (node.airportIds != null && !node.airportIds.isEmpty()) {
            result.addAll(node.airportIds);
        }
        for (Node child : node.children.values()) {
            collectAirportIdsRecursive(child, result);
        }
    }
}