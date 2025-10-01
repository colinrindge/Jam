package puzzles.dice;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class DiceConfig implements Configuration {
    private static String goal;
    private String currentFace;
    private static String filename;
    private static LinkedHashMap<String, ArrayList<Configuration>> neighbors = new LinkedHashMap<>();
    private static int numSides;
    private static String[] sides;


    public DiceConfig(String currentFace, String currentGoal, String filename) throws IOException {
        DiceConfig.goal = currentGoal;
        this.currentFace = currentFace;
        DiceConfig.filename = filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            DiceConfig.numSides = Integer.parseInt(br.readLine());
            DiceConfig.sides = br.readLine().split(" ");
            String nextLine = br.readLine();
            while(nextLine != null) {
                String[] splitLine = nextLine.split(" ");
                ArrayList<Configuration> neighborsList = new ArrayList<>();
                for(int i = 1; i < splitLine.length; i++) {
                    neighborsList.add(new DiceConfig(splitLine[i]));
                }
                neighbors.put(splitLine[0], neighborsList);
                nextLine = br.readLine();
            }
        }
    }

    private DiceConfig(String currentFace) {
        this.currentFace = currentFace;
    }

    public static LinkedHashMap<String, ArrayList<Configuration>> getNeighborsList() {
        return neighbors;
    }

    @Override
    public boolean isSolution() {
        return this.currentFace.equals(DiceConfig.goal);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return neighbors.get(this.currentFace);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof DiceConfig oDice) {
            return this.currentFace.equals(oDice.currentFace);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.currentFace.hashCode();
    }

    @Override
    public String toString() {
        return this.currentFace;
    }
}
