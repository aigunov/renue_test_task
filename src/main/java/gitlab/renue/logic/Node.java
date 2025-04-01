package gitlab.renue.logic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    Map<Character, Node> children;
    List<Integer> airportIds;

    public Node() {
        children = new HashMap<>();
        airportIds = new ArrayList<>();
    }
}