package gitlab.renue.logic;

import java.util.*;

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
        char[] prefix;
        Map<Character, Node> children;
        Set<Integer> airportIds;

        Node(String prefix) {
            this.prefix = prefix.toCharArray();
            this.children = new TreeMap<>();
            this.airportIds = new HashSet<>();
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
            int prefixLen = commonPrefixLength(lowerCaseValue.substring(i), child.prefix);
            i += prefixLen;

            if (prefixLen < child.prefix.length) {
                Node newChild = new Node(new String(child.prefix, prefixLen, child.prefix.length - prefixLen));
                newChild.children = child.children;
                newChild.airportIds.addAll(child.airportIds);

                child.prefix = Arrays.copyOf(child.prefix, prefixLen);
                child.children = new TreeMap<>();
                child.children.put(newChild.prefix[0], newChild);

                if (i < lowerCaseValue.length()) {
                    Node newNode = new Node(lowerCaseValue.substring(i));
                    newNode.airportIds.add(airportId);
                    child.children.put(lowerCaseValue.charAt(i), newNode);
                }
                return;
            }

            current = child;
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
            int prefixLen = commonPrefixLength(lowerCasePrefix.substring(i), child.prefix);

            if (prefixLen == 0) {
                return new ArrayList<>();
            }

            if (prefixLen < child.prefix.length) {
                if (new String(child.prefix).startsWith(lowerCasePrefix.substring(i))) {
                    return collectAirportIds(child);
                } else {
                    return new ArrayList<>();
                }
            }

            i += prefixLen;
            current = child;
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
        Set<Integer> uniqueAirportIds = new HashSet<>();
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            uniqueAirportIds.addAll(current.airportIds);

            for (Node child : current.children.values()) {
                stack.push(child);
            }
        }

        List<Integer> sortedAirportIds = new ArrayList<>(uniqueAirportIds);
        Collections.sort(sortedAirportIds);
        return sortedAirportIds;
    }

    /**
     * Вычисляет длину общего префикса двух строк.
     *
     * @param a Первая строка.
     * @param b Вторая строка.
     * @return Длина общего префикса.
     */
    private int commonPrefixLength(String a, char[] b) {
        int len = Math.min(a.length(), b.length);
        for (int i = 0; i < len; i++) {
            if (a.charAt(i) != b[i]) {
                return i;
            }
        }
        return len;
    }
}
