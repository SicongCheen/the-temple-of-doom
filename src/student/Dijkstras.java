package student;

import game.EscapeState;
import game.Node;

import java.util.*;

/**
 * This class includes the methods to find the shortest path or least cost from a start node to an end node based on Dijkstras algorithm.
 *
 * @author Sicong Chen (schen08)
 */
public class Dijkstras {

    /**
     * Get the shortest path from a start node to an end node based on Dijkstras algorithm.
     *
     * @param state escape state
     * @param start start node
     * @param end end node
     * @return the shortest path from start node to end node
     */
    public static Stack<Node> getShortestPath(EscapeState state, Node start, Node end) {

        // use a stack to store the shortest path
        Stack<Node> shortestPath = new Stack<>();

        // calculate shortest paths
        HashMap<Node, List<Object>> pathsInfo = calcShortestPaths(state, start);

        // add nodes on the shortest path from end node to start node to the stack (backwards)
        Node step = end;
        while (step != null && pathsInfo.get(step).get(1) != null) {
            shortestPath.push(step);
            step = (Node) pathsInfo.get(step).get(1);
        }

        return shortestPath;
    }

    /**
     * Get the least cost from a start node to an end node based on Dijkstras algorithm.
     *
     * @param state escape state
     * @param start start node
     * @param end end node
     * @return the least cost from start node no end node
     */
    public static int getLeastCost(EscapeState state, Node start, Node end) {

        // calculate shortest paths
        HashMap<Node, List<Object>> pathsInfo = calcShortestPaths(state, start);

        // calculate total cost
        Node step = end;
        int costTotal = 0;

        while (step != null) {
            costTotal += (Integer) pathsInfo.get(step).get(0);
            step = (Node) pathsInfo.get(step).get(1);
        }

        return costTotal;
    }


    /**
     * Calculate the shortest paths from a start node to all other nodes.
     *
     * @param state escape state
     * @param start start node
     * @return a list includes all nodes along with their cost and previous node info
     */
    private static HashMap<Node, List<Object>> calcShortestPaths(EscapeState state, Node start) {

        // use HashMap to track visited and unvisited nodes
        // here, key is Node and value is a list which contains cost (Integer) and previous node (Node)
        HashMap<Node, List<Object>> visited = new HashMap<>();
        HashMap<Node, List<Object>> unvisited = setUpUnvisited(state, start);

        boolean finished = false;

        // repeat the following steps until unvisited list is empty
        while (finished == false) {

            if (unvisited.size() == 0) {
                finished = true;
            }
            else {
                // get the node with the least cost
                Optional<Map.Entry<Node, List<Object>>> leastCostNode = unvisited.entrySet().stream().min(Comparator.comparing(a -> (Integer) a.getValue().get(0)));
                // update current node if found
                if (leastCostNode.isPresent()) start = leastCostNode.get().getKey();

                // examine neighbours
                for (Node n : start.getNeighbours()) {
                    // if not in visited list
                    if (!visited.containsKey(n)) {
                        // calculate new cost
                        int costNew;
                        if ((Integer) unvisited.get(n).get(0) == Integer.MAX_VALUE) costNew = n.getEdge(start).length();
                        else costNew = (Integer) unvisited.get(n).get(0) + n.getEdge(start).length();

                        // update cost and previous node if new cost is lower
                        if (costNew < (Integer) unvisited.get(n).get(0)) {
                            unvisited.get(n).set(0, costNew);
                            unvisited.get(n).set(1, start);
                        }
                    }
                }

                // update visited list
                List<Object> info = new ArrayList<>();
                info.add(unvisited.get(start).get(0));
                info.add(unvisited.get(start).get(1));
                visited.put(start, info);

                // remove from unvisited list
                unvisited.remove(start);
            }
        }
        return visited;
    }


    /**
     * Initialize unvisited list.
     *
     * @param state escape state
     * @param start start node
     */
    private static HashMap<Node, List<Object>> setUpUnvisited(EscapeState state, Node start) {

        HashMap<Node, List<Object>> unvisited = new HashMap<>();

        // add all nodes to the unvisited list
        for (Node n: state.getVertices()) {
            // set cost to maximum integer value and previous node to null as initial states for all nodes
            List<Object> info = new ArrayList<>();
            info.add(Integer.MAX_VALUE);
            info.add(null);
            unvisited.put(n, info);
        }

        // update the cost of start node to 0
        unvisited.get(start).set(0, 0);

        return unvisited;
    }

}
