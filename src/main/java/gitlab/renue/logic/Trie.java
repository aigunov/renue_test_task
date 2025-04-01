package gitlab.renue.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Trie представляет собой префиксное дерево (Trie) для хранения и поиска идентификаторов аэропортов по префиксам.
 * Поиск нечувствителен к регистру.
 * <p>
 * Особенности реализации:
 * - Использует массив Node[] children для хранения дочерних узлов.
 * - Размер массива children динамически увеличивается при необходимости.
 * - Поддерживает символы ASCII, так как использует их коды как индексы массива children.
 * - Хранит идентификаторы аэропортов в массиве int[] airportIds.
 */
public class Trie {

    /**
     * Внутренний класс Node представляет узел дерева Trie.
     */
    private static class Node {
        char symbol;
        Node[] children;
        int[] airportIds;
        int airportIdsSize;

        /**
         * Конструктор для создания объекта Node.
         *
         * @param symbol Символ, связанный с узлом.
         */
        Node(char symbol) {
            this.symbol = symbol;
            this.children = new Node[16]; // Начальный размер массива дочерних узлов
            this.airportIds = new int[10]; // Начальный размер массива идентификаторов аэропортов
            this.airportIdsSize = 0;
        }
    }

    private final Node root;

    /**
     * Конструктор для создания объекта Trie.
     */
    public Trie() {
        root = new Node('\0');
    }

    /**
     * Вставляет значение и идентификатор аэропорта в Trie.
     *
     * @param value     Значение для вставки.
     * @param airportId Идентификатор аэропорта, связанный со значением.
     */
    public void insert(String value, int airportId) {
        Node current = root;
        String lowerCaseValue = value.toLowerCase();
        for (int i = 0; i < lowerCaseValue.length(); i++) {
            char c = lowerCaseValue.charAt(i);
            if ((int) c >= current.children.length) {
                resizeChildren(current, (int) c * 2); // Увеличиваем размер массива дочерних узлов при необходимости
            }
            if (current.children[c] == null) {
                current.children[c] = new Node(c);
            }
            current = current.children[c];
        }
        addAirportId(current, airportId);
    }

    /**
     * Увеличивает размер массива дочерних узлов.
     *
     * @param node    Узел, массив которого необходимо увеличить.
     * @param newSize Новый размер массива.
     */
    private void resizeChildren(Node node, int newSize) {
        Node[] newChildren = new Node[newSize];
        System.arraycopy(node.children, 0, newChildren, 0, node.children.length);
        node.children = newChildren;
    }

    /**
     * Добавляет идентификатор аэропорта к узлу.
     *
     * @param node      Узел, к которому добавляется идентификатор.
     * @param airportId Идентификатор аэропорта для добавления.
     */
    private void addAirportId(Node node, int airportId) {
        if (node.airportIdsSize == node.airportIds.length) {
            int[] newAirportIds = new int[node.airportIds.length * 2];
            System.arraycopy(node.airportIds, 0, newAirportIds, 0, node.airportIds.length);
            node.airportIds = newAirportIds;
        }
        node.airportIds[node.airportIdsSize++] = airportId;
    }

    /**
     * Ищет идентификаторы аэропортов по заданному префиксу.
     *
     * @param prefix Префикс для поиска.
     * @return Массив идентификаторов аэропортов, найденных по префиксу.
     */
    public int[] search(String prefix) {
        Node current = root;
        String lowerCasePrefix = prefix.toLowerCase();
        for (int i = 0; i < lowerCasePrefix.length(); i++) {
            int index = lowerCasePrefix.charAt(i);
            if (index < current.children.length && current.children[index] != null) {
                current = current.children[index];
            } else {
                return new int[0];
            }
        }
        return collectAirportIds(current);
    }

    /**
     * Собирает идентификаторы аэропортов из узла и его потомков.
     *
     * @param node Узел, из которого начинается сбор идентификаторов.
     * @return Массив идентификаторов аэропортов.
     */
    private int[] collectAirportIds(Node node) {
        List<Integer> result = new ArrayList<>();
        collectAirportIdsRecursive(node, result);
        int[] resultArray = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            resultArray[i] = result.get(i);
        }
        return resultArray;
    }

    /**
     * Рекурсивно собирает идентификаторы аэропортов из узла и его потомков.
     *
     * @param node   Узел для рекурсивного обхода.
     * @param result Список для хранения собранных идентификаторов.
     */
    private void collectAirportIdsRecursive(Node node, List<Integer> result) {
        if (node.airportIdsSize > 0) {
            for (int i = 0; i < node.airportIdsSize; i++) {
                result.add(node.airportIds[i]);
            }
        }
        for (Node child : node.children) {
            if (child != null) {
                collectAirportIdsRecursive(child, result);
            }
        }
    }
}