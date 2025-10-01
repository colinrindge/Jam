package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.IOException;
import java.util.List;

public class Jam {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        } else {
            JamConfig start = new JamConfig(args[0]);
            Solver solver = new Solver();
            List<Configuration> path = solver.buildPathBFS(start);
            if(path == null) {
                System.out.println("No path found");
                return;
            }
            System.out.println("Total Configs: " + solver.getTotalConfigs());
            System.out.println("Unique Configs: " + solver.getUniqueConfigs());
            for(Configuration c : path) {
                System.out.println(c.toString());
            }

        }
    }
}