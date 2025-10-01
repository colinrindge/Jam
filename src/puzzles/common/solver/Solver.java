package puzzles.common.solver;

import java.util.*;

public class Solver {
    private int totalConfigs;
    private int uniqueConfigs;

    public List<Configuration> buildPathBFS(Configuration start) {

        // Every node we visit will have a predecessor.
        // A node has been assigned a predecessor iff it has been visited,
        // so the keys of our predecessor map create a visited set!
        Map<Configuration, Configuration> predecessor = new HashMap<>();

        predecessor.put(start, null);
        // null predecessor indicates this is the starting point.
        Queue<Configuration> toVisit = new LinkedList<>();
        toVisit.offer(start);
        totalConfigs++;

        // run the BFS algorithm
        while (!toVisit.isEmpty() && !toVisit.peek().isSolution()) {
            Configuration thisConfig = toVisit.remove();
            if(thisConfig.getNeighbors() != null) {
                for (Configuration nbr : thisConfig.getNeighbors()) {
                    if (!predecessor.containsKey(nbr)) {
                        predecessor.put(nbr, thisConfig);
                        toVisit.offer(nbr);
                    }
                    totalConfigs++;
                }
            }
        }

        this.uniqueConfigs = predecessor.size();
        // construct the path, if one exists.
        if ( toVisit.isEmpty() ) {
            return null;   // we never found the finish node.
        }
        else {
            List< Configuration > path = new LinkedList<>();
            Configuration fin = toVisit.peek();
            while (fin != null) {
               path.addFirst(fin);
               fin = predecessor.get(fin);
            }

            return path;
        }
    }

    public int getUniqueConfigs() {
        return uniqueConfigs;
    }
    public int getTotalConfigs() {
        return totalConfigs;
    }
}