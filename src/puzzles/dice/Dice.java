package puzzles.dice;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Dice {
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Usage: java Dice start end die1 die2...");
        } else {
            String startDice = args[0];
            String goal = args[1];
            ArrayList<List<Configuration>> allPaths = new ArrayList<>();
            int totalConfigs = 0;
            int uniqueConfigs = 0;
            for(int i = 0; i < startDice.length(); i++) {
                Solver solver = new Solver();
                String file = "die-" + args[i+2] + ".txt";
                DiceConfig start = new DiceConfig(String.valueOf(startDice.charAt(i)),
                        String.valueOf(goal.charAt(i)), file);

                List<Configuration> path = solver.buildPathBFS(start);
                allPaths.add(path);

                String numFaces = args[i+2];
                if(numFaces.equals("6a") || numFaces.equals("6b")) {
                    numFaces = "6";
                }
                System.out.println("Dice#" + i + ": File: " + file +", Faces: " + numFaces);
                LinkedHashMap<String, ArrayList<Configuration>> neighbors = DiceConfig.getNeighborsList();
                for(String side: neighbors.sequencedKeySet()) {
                    System.out.println("\t" + side + "=" + neighbors.get(side));
                }
                totalConfigs += solver.getTotalConfigs();
                uniqueConfigs += solver.getUniqueConfigs();
            }
            System.out.println("Start: " + startDice + ", End: " + goal);
            System.out.println("Total configs: " + totalConfigs);
            System.out.println("Unique configs: " + uniqueConfigs);

            for(List<Configuration> path : allPaths) {
                if(path == null) {
                    System.out.println("No path found");
                    return;
                }
            }

            int counter = 0;
            StringBuilder bothDice = new StringBuilder();
            for(List<Configuration> path: allPaths) {
                bothDice.append(path.getFirst());
            }

            System.out.println("Step " + counter + ": " + bothDice);
            counter++;
            for(int i = 0; i < allPaths.size(); i++) {
                for(int k = 1; k <= allPaths.get(i).size()-1; k++) {
                    bothDice.setCharAt(i, allPaths.get(i).get(k).toString().charAt(0));
                    System.out.println("Step " + counter + ": " + bothDice);
                    counter++;
                }
            }
        }

    }
}