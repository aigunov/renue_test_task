package gitlab.renue.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс Trie представляет собой префиксное дерево (Trie) для хранения и поиска идентификаторов аэропортов по префиксам.
 * Поиск нечувствителен к регистру.
 */
public class Trie {

    /**
     * Внутренний класс Node представляет узел дерева Trie.
     */
    private static class Node {
        Map<Character, Node> children;
        List<Integer> airportIds;

        Node() {
            children = new HashMap<>();
            airportIds = new ArrayList<>();
        }
    }

    private final Node root;

    /**
     * Конструктор для создания объекта Trie.
     */
    public Trie() {
        root = new Node();
    }

    /**
     * Вставляет значение и идентификатор аэропорта в Trie.
     *
     * @param value     Значение для вставки.
     * @param airportId Идентификатор аэропорта, связанный со значением.
     */
    public void insert(String value, int airportId) {
        Node current = root;
        for (char c : value.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
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
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }
            current = current.children.get(c);
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