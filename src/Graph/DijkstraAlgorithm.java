package Graph;

/**
 * Created by nachomora on 11/28/16.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DijkstraAlgorithm {

    private final List<Device> nodes;
    private final List<Edge> edges;
    private Set<Device> settledNodes;
    private Set<Device> unSettledNodes;
    private Map<Device, Device> predecessors;
    private Map<Device, Integer> distance;

    public DijkstraAlgorithm(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Device>(graph.getVertexes());
        this.edges = new ArrayList<Edge>(graph.getEdges());
    }

    public void execute(Device source) {
        settledNodes = new HashSet<Device>();
        unSettledNodes = new HashSet<Device>();
        distance = new HashMap<Device, Integer>();
        predecessors = new HashMap<Device, Device>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Device node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Device node) {
        List<Device> adjacentNodes = getNeighbors(node);
        for (Device target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(Device node, Device target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Device> getNeighbors(Device node) {
        List<Device> neighbors = new ArrayList<Device>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private Device getMinimum(Set<Device> vertexes) {
        Device minimum = null;
        for (Device vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Device vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Device destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<String> getPath(Device target) {
        LinkedList<String> path = new LinkedList<String >();
        Device step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step.getMACAddress());
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step.getMACAddress());
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
