package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

public class ClockConfig implements Configuration {
    private static int HOURS;
    private static int END;
    private int current;

    public ClockConfig(int current, int hours, int end) {
        HOURS = hours;
        END = end;
        this.current = current;
    }
    private ClockConfig(int current) {
        this.current = current;
    }

    @Override
    public boolean isSolution() {
        return this.current == END;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        neighbors.add(new ClockConfig((this.current%HOURS)+1));
        int temp = this.current-1;
        if(temp == 0) {
            temp = HOURS;
        }
        neighbors.add(new ClockConfig(temp));
        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof ClockConfig oClock) {
            return this.current == oClock.current;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.current);
    }

    @Override
    public String toString() {
        return Integer.toString(this.current);
    }
}
