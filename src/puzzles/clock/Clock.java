package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

public class Clock {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start end");
        } else {
            ClockConfig start = new ClockConfig(Integer.parseInt(args[1]), Integer.parseInt(args[0]), Integer.parseInt(args[2]));
            Solver solver = new Solver();
            List<Configuration> path = solver.buildPathBFS(start);
            if(path == null) {
                System.out.println("No path found");
                return;
            }
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            System.out.println("Total configs: " + solver.getTotalConfigs());
            System.out.println("Unique configs: " + solver.getUniqueConfigs());
            for(int i = 0; i < path.size(); i++) {
                System.out.println("Step " + i + ": " + path.get(i));
            }
        }
    }
}
