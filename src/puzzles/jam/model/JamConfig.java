package puzzles.jam.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class JamConfig implements Configuration {
    private final static String X_CAR_COLOR = "#DF0101";

    private static int numRows;
    private static int numCols;
    private Car[][] grid;
    private HashSet<Car> cars = new HashSet<>();
    private Car redCar;

    public JamConfig(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] rowCol = br.readLine().split(" ");
            numRows = Integer.parseInt(rowCol[0]);
            numCols = Integer.parseInt(rowCol[1]);
            grid = new Car[numRows][numCols];
            br.readLine();
            String nextLine = br.readLine();
            while (nextLine != null) {
                String[] line = nextLine.split(" ");
                String name = line[0];
                int startRow = Integer.parseInt(line[1]);
                int startCol = Integer.parseInt(line[2]);
                int endRow = Integer.parseInt(line[3]);
                int endCol = Integer.parseInt(line[4]);


                if(name.equals("X")) {
                    Car car = new Car(name, startRow, startCol, endCol - startCol + 1, "H", X_CAR_COLOR);
                    cars.add(car);
                    redCar = car;
                } else if (startRow == endRow) {

                    Car car = new Car(name, startRow, startCol, endCol - startCol + 1, "H", generateRandomHexColor());
                    cars.add(car);
                } else if (startCol == endCol) {
                    Car car = new Car(name, startRow, startCol, endRow - startRow + 1, "V", generateRandomHexColor());
                    cars.add(car);
                }

                nextLine = br.readLine();
            }
            updateGrid();
        }
    }

    public JamConfig(JamConfig orig, HashSet<Car> cars) {
        this.cars = cars;
        updateGrid();
    }

    /**
     * Updates grid representation of game based on cars hashset
     */
    public void updateGrid() {
        grid = new Car[numRows][numCols];
        for(Car car : cars) {
            for(int i = 0; i < car.getLength(); i++) {
                if(car.getName().equals("X")) {
                    this.grid[car.getRow()][car.getCol() + i] = car;
                    redCar = car;
                } else if(car.getOrientation().equals("H")) {
                    this.grid[car.getRow()][car.getCol() + i] = car;
                } else if(car.getOrientation().equals("V")) {
                    this.grid[car.getRow() + i][car.getCol()] = car;
                }
            }
        }
    }

    @Override
    public boolean isSolution() {
        return redCar.getCol() + redCar.getLength() == numCols;
    }

    /**
     * Finds all possible neighbors of current game configuration
     * @return Collection(Configuration) of all possible configs
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new ArrayList<>();

        for(Car car: cars) {
            if(car.getOrientation().equals("H")) {
                if(car.getCol() != 0 && this.grid[car.getRow()][car.getCol() - 1] == null) {
                    HashSet<Car> temp = new HashSet<>(cars);
                    temp.remove(car);
                    temp.add(new Car(car, car.getRow(), car.getCol()-1));
                    neighbors.add(new JamConfig(this, temp));
                }
                if(car.getCol() + car.getLength() < numCols && this.grid[car.getRow()][car.getCol() + car.getLength()] == null) {
                    HashSet<Car> temp = new HashSet<>(cars);
                    temp.remove(car);
                    temp.add(new Car(car, car.getRow(), car.getCol()+1));
                    neighbors.add(new JamConfig(this, temp));
                }
            } else if(car.getOrientation().equals("V")) {
                if(car.getRow() != 0 && this.grid[car.getRow() - 1][car.getCol()] == null) {
                    HashSet<Car> temp = new HashSet<>(cars);
                    temp.remove(car);
                    temp.add(new Car(car, car.getRow()-1, car.getCol()));
                    neighbors.add(new JamConfig(this, temp));
                }
                if(car.getRow() + car.getLength() < numRows && this.grid[car.getRow() + car.getLength()][car.getCol()] == null) {
                    HashSet<Car> temp = new HashSet<>(cars);
                    temp.remove(car);
                    temp.add(new Car(car, car.getRow() + 1, car.getCol()));
                    neighbors.add(new JamConfig(this, temp));
                }
            }
        }

        return neighbors;
    }

    public Car[][] getGrid() {
        return grid;
    }
    public HashSet<Car> getCars() {
        return cars;
    }
    public int getNumRows() {
        return numRows;
    }
    public int getNumCols() {
        return numCols;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof JamConfig oJam) {
                for(int i = 0; i < numRows; i++) {
                    for(int j = 0; j < numCols; j++) {
                        if(this.grid[i][j] == null || oJam.grid[i][j] == null) {
                            return this.grid[i][j] == oJam.grid[i][j];
                        } else if(!this.grid[i][j].getName().equals(oJam.grid[i][j].getName())) {
                            return false;
                        }
                    }
                }
                return true;
        }
        return false;
    }

    public static String generateRandomHexColor() {
        Random random = new Random();
        // Generate random RGB values between 0 and 255
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        // Format the RGB values as a hex string
        return String.format("#%02x%02x%02x", r, g, b);
    }

    @Override
    public int hashCode() { return Arrays.deepHashCode(this.grid); }

    @Override
    public String toString() {
        StringBuilder gridString = new StringBuilder("   ");
        StringBuilder lines = new StringBuilder("  ");
        for(int i = 0; i < numCols; i++) {
            gridString.append(i).append(" ");
            lines.append("--");
        }
        gridString.append("\n");
        gridString.append(lines);
        gridString.append("\n");
        for(int row = 0; row < numRows; row++) {
            gridString.append(row).append("| ");
            for(int col = 0; col < numCols; col++) {
                if(grid[row][col] != null) {
                    gridString.append(grid[row][col].getName()).append(" ");
                } else {
                    gridString.append(". ");
                }
            }
            gridString.append("\n");
        }
        return gridString.toString();
    }
}
