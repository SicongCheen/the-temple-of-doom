package student;

import game.EscapeState;
import game.Node;

import java.util.*;

/**
 * This class includes the methods to find the exit (and pick up as much gold as possible) before the entrance collapses.
 *
 * @author Sicong Chen (schen08)
 */
public class ExitFinder {

    /**
     * Find the exit within the time limit and pick up as much gold as possible.
     *
     * @param state current escape state
     */
    public static void find(EscapeState state) {

        Node start = state.getCurrentNode();
        Node end = state.getExit();
        int timeRemaining;

        // pick up gold if there is any on the first tile
        if (start.getTile().getGold() > 0) pickUpGold(state);

        // repeat the following steps until there is no more qualified gold to pick up
        while (true) {

            // update current position and time remaining
            start = state.getCurrentNode();
            timeRemaining = state.getTimeRemaining();

            // try to find qualified nearest gold from current position
            Node nearestGold = findNearestGold(state, start, end, timeRemaining);

            // if no (qualified nearest) gold found, go to the exit
            if (nearestGold == null) break;

            // if found, get the shortest path to this gold
            Stack<Node> shortestPathToNearestGold = getShortestPath(state, start, nearestGold);
            // go to the nearest gold along the shortest path
            traverse(state, shortestPathToNearestGold);
        }

        // get the shortest path to the exit if no more gold to pick up
        Stack<Node> shortestPathToExit = getShortestPath(state, start, end);
        // go to the exit along the shortest path
        traverse(state, shortestPathToExit);

    }

    /**
     * Get the nearest gold from the current position.
     *
     * @param state current escape node
     * @param start start node
     * @param end end node
     * @param timeRemaining time remaining
     * @return nearest gold or null if not found
     */
    private static Node findNearestGold(EscapeState state, Node start, Node end, int timeRemaining) {

        // store qualified nodes with their distances to the current node in a HashMap
        HashMap<Node, Integer> qualifiedNodes = new HashMap<>();

        for(Node n : state.getVertices()) {
            // condition #1. gold > 0
            if (n.getTile().getGold() > 0) {
                // get distance from the current node to the gold
                int costFromStart = Dijkstras.getLeastCost(state, start, n);
                // get distance from the gold to the exit
                int costToEnd = Dijkstras.getLeastCost(state, n, end);
                // condition #2. the sum of the distances above < time remaining (exclude the current node itself)
                if (costFromStart + costToEnd < timeRemaining && costFromStart != 0) {
                    qualifiedNodes.put(n, costFromStart);
                }
            }
        }

        // if at least one qualified node found
        if (qualifiedNodes.size() != 0) {
            // get the one nearest to the current node
            Optional<Map.Entry<Node, Integer>> nearestGold = qualifiedNodes.entrySet().stream().min(Comparator.comparing(a -> a.getValue()));
            if (nearestGold.isPresent()) return nearestGold.get().getKey();
        }

        return null;
    }

    /**
     * Get the shortest path from start node to end node using Dijkstras algorithm.
     *
     * @param state current escape state
     * @param start start node
     * @param end end node
     * @return the shortest path from start node to end node
     */
    private static Stack<Node> getShortestPath(EscapeState state, Node start, Node end) {
        return Dijkstras.getShortestPath(state, start, end);
    }


    /**
     * Go to the nearest gold or exit and pick up gold if there is any.
     *
     * @param state current escape state
     * @param shortestPath shortest path to gold or exit
     */
    private static void traverse(EscapeState state, Stack<Node> shortestPath) {
        while (!shortestPath.isEmpty()) {
            state.moveTo(shortestPath.pop());
            pickUpGold(state);
        }
    }


    /**
     * Pick up gold if there is any.
     *
     * @param state current escape state
     */
    private static void pickUpGold(EscapeState state) {
        if (state.getCurrentNode().getTile().getGold() > 0) state.pickUpGold();
    }

}
