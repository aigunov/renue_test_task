package gitlab.renue.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс CompressedTrie представляет собой сжатое префиксное дерево (Compressed Trie)
 * для хранения и поиска идентификаторов аэропортов по префиксам.
 * Поиск нечувствителен к регистру.
 */
public class CompressedTrie {

    /**
     * Внутренний класс Node представляет узел сжатого дерева Trie.
     */
    private static class Node {
        String prefix;
        Map<Character, Node> children;
        List<Integer> airportIds;

        Node(String prefix) {
            this.prefix = prefix;
            this.children = new HashMap<>();
            this.airportIds = new ArrayList<>();
        }

        Node() {
            this("");
        }
    }

    private final Node root;

    /**
     * Конструктор для создания объекта CompressedTrie.
     */
    public CompressedTrie() {
        root = new Node();
    }

    /**
     * Вставляет значение и идентификатор аэропорта в CompressedTrie.
     *
     * @param value     Значение для вставки.
     * @param airportId Идентификатор аэропорта, связанный со значением.
     */
    public void insert(String value, int airportId) {
        Node current = root;
        String lowerCaseValue = value.toLowerCase();
        int i = 0;
        while (i < lowerCaseValue.length()) {
            char c = lowerCaseValue.charAt(i);
            if (!current.children.containsKey(c)) {
                Node newNode = new Node(lowerCaseValue.substring(i));
                newNode.airportIds.add(airportId);
                current.children.put(c, newNode);
                return;
            }
            Node child = current.children.get(c);
            String childPrefix = child.prefix;
            int j = 0;
            while (j < childPrefix.length() && i < lowerCaseValue.length()
                    && lowerCaseValue.charAt(i) == childPrefix.charAt(j)) {
                i++;
                j++;
            }
            if (j == childPrefix.length()) {
                current = child;
            } else {
                Node splitNode = new Node(childPrefix.substring(0, j));
                splitNode.children.put(childPrefix.charAt(j), child);
                if (i < lowerCaseValue.length()) {
                    splitNode.children.put(lowerCaseValue.charAt(i), new Node(lowerCaseValue.substring(i)));
                }
                splitNode.airportIds.addAll(child.airportIds);
                child.prefix = childPrefix.substring(j);
                child.airportIds.clear();
                child.airportIds.add(airportId);
                if (i - childPrefix.length() + j >= 0) {
                    current.children.put(lowerCaseValue.charAt(i - childPrefix.length() + j), splitNode);
                } else {
                    current.children.put(childPrefix.charAt(0), splitNode);
                }
                return;
            }
        }
        current.airportIds.add(airportId);
    }

    /**
     * Ищет идентификаторы аэропортов по заданному префиксу.
     *
     * @param prefix Префикс для поиска.
     * @return Список идентификаторов аэропортов, найденных по префиксу.
     */
    public List<Integer> search(String prefix) {
        Node current = root;
        String lowerCasePrefix = prefix.toLowerCase();
        int i = 0;
        while (i < lowerCasePrefix.length()) {
            char c = lowerCasePrefix.charAt(i);
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }
            Node child = current.children.get(c);
            String childPrefix = child.prefix;
            int j = 0;
            while (j < childPrefix.length() && i < lowerCasePrefix.length()
                    && lowerCasePrefix.charAt(i) == childPrefix.charAt(j)) {
                i++;
                j++;
            }
            if (j == childPrefix.length()) {
                current = child;
            } else {
                return new ArrayList<>();
            }
        }
        return collectAirportIds(current);
    }

    /**
     * Собирает идентификаторы аэропортов из узла и его потомков.
     *
     * @param node Узел, из которого начинается сбор идентификаторов.
     * @return Список идентификаторов аэропортов.
     */
    private List<Integer> collectAirportIds(Node node) {
        List<Integer> result = new ArrayList<>();
        collectAirportIdsRecursive(node, result);
        return result;
    }

    /**
     * Рекурсивно собирает идентификаторы аэропортов из узла и его потомков.
     *
     * @param node   Узел для рекурсивного обхода.
     * @param result Список для хранения собранных идентификаторов.
     */
    private void collectAirportIdsRecursive(Node node, List<Integer> result) {
        if (!node.airportIds.isEmpty()) {
            result.addAll(node.airportIds);
        }
        for (Node child : node.children.values()) {
            collectAirportIdsRecursive(child, result);
        }
    }
}