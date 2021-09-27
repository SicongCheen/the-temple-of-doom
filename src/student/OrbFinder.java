package student;

import game.ExplorationState;
import game.NodeStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class includes the methods to find the orb under the temple (in as few steps as possible).
 *
 * @author Sicong Chen (schen08)
 */
public class OrbFinder {

    /**
     * Store the nodes that have been visited.
     */
    private static ArrayList<Long> visited = new ArrayList<>();

    /**
     * Find the path to the orb using DFS (Depth First Search) algorithm recursively.
     * The bonus multiplier is optimised through always selecting the neighbour with the shortest distance to the orb.
     *
     * @param state current exploration state
     */
    public static void find(ExplorationState state) {

        // check if current position reaches the orb or not
        if (state.getDistanceToTarget() == 0) return;

        // add current position to the visited list
        long current = state.getCurrentLocation();
        visited.add(state.getCurrentLocation());

        // store the adjacent neighbours at each position to neighbours list
        ArrayList<NodeStatus> neighbours = new ArrayList<>();
        for (NodeStatus n : state.getNeighbours()) neighbours.add(n);

        // sort the neighbours list based on distanceToTarget
        Collections.sort(neighbours, Comparator.comparingInt(NodeStatus::distanceToTarget));

        for (NodeStatus neighbour : neighbours) {

            // check if this neighbour has already been visited
            if (!visited.contains(neighbour.nodeID())) {

                // if not, move to this neighbour
                state.moveTo(neighbour.nodeID());
                // apply the same DFS algorithm recursively
                find(state);

                // check if current position reaches the orb or not
                if (state.getDistanceToTarget() == 0) return;

                // move back if dead end encountered
                state.moveTo(current);
            }
        }
    }
}
