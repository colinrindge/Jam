package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class JamModel {
    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private JamConfig currentConfig;

    //filename
    private String defaultConfig;

    private Car selectedCar;
    private int selectedRow;
    private int selectedCol;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void notifyObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Sets the current state of the game to the next step in solving it
     */
    public void getHint() {
        Solver solver = new Solver();
        List<Configuration> path = solver.buildPathBFS(currentConfig);
        if(path == null) {
            notifyObservers("No path found");
            return;
        }
        if(path.size() > 1) {
            this.currentConfig = (JamConfig)path.get(1);
            selectedCar = null;
            notifyObservers("Next Step:");
        } else {
            notifyObservers("Puzzle is Already Solved");
        }
    }

    /**
     * Loads a file and sets the current config to that file
     * @param filename file to load
     */
    public void loadFile(String filename) {
        try {
            this.currentConfig = new JamConfig(filename);
            this.defaultConfig = filename;
            selectedCar = null;
            notifyObservers("Loaded: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to Load: " + filename);
        }

    }

    /**
     * Returns the grid to the default state of the last loaded file
     */
    public void loadDefault() {
        try {
            this.currentConfig = new JamConfig(this.defaultConfig);
        } catch (IOException e) {
            System.out.println("Failed to Load: " + defaultConfig);
            return;
        }
        selectedCar = null;
        notifyObservers("Puzzle Reset");
    }

    /**
     * Either selects the car at the passed index and saves it, or if a car has already been selected,
     * moves the car to the selected index
     * @param row row of car to select/location to move to
     * @param col col of car to select/location to move to
     */
    public void selectCar(int row, int col) {
        if(0 > row || row >= currentConfig.getNumRows() ) {
            notifyObservers("Not a valid row");
            return;
        }
        if(0 > col || col >= currentConfig.getNumCols() ) {
            notifyObservers("Not a valid row");
            return;
        }

        if(selectedCar == null) {
            Car[][] grid = currentConfig.getGrid();
            if(grid[row][col] == null) {
                notifyObservers("No car at (" + row + ", " + col + ")");
            } else {
                selectedCar = grid[row][col];
                notifyObservers("Selected (" + row + ", " + col + ")");
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            Car[][] grid = currentConfig.getGrid();
            if(selectedCar.getOrientation().equals("H") && selectedCar.getRow() != row) {
                notifyObservers("Can't move from (" + selectedRow + ", " + selectedCol
                        + ") to (" + row + ", " + col + ")");
                selectedCar = null;
                return;
            } else if(selectedCar.getOrientation().equals("V") && selectedCar.getCol() != col) {
                notifyObservers("Can't move from (" + selectedRow + ", " + selectedCol
                        + ") to (" + row + ", " + col + ")");
                selectedCar = null;
                return;
            }

            if(selectedCar.getOrientation().equals("H")) {
                //increment between distance from back of car and the part of the car selected
                int increment = selectedCol - selectedCar.getCol();
                int start = Math.min(selectedCar.getCol(), col);
                int end = Math.max(selectedCar.getCol()-increment, col+selectedCar.getLength()-increment-1);
                if(end >= currentConfig.getNumCols()) {
                    increment += end-currentConfig.getNumCols()+1;
                    end = currentConfig.getNumCols()-1;

                }
                if(start < 0) {
                    increment += start;
                    start = 0;
                }

                for(int i = start; i <= end; i++) {
                    if(grid[row][i] != null) {
                        if(!grid[row][i].getName().equals(selectedCar.getName())) {
                            //this code sucks but I don't want to rewrite it
                            if(i > col && start == selectedCar.getCol()) {
                                increment++;
                                break;
                            } else {
                                notifyObservers("Can't move from (" + selectedRow + ", " + selectedCol
                                        + ") to (" + row + ", " + col + ")");
                                selectedCar = null;
                                return;
                            }
                        }
                    }
                }

                if(start == col) {
                    for(int i = col-1; i >= col-increment; i--) {
                        if(i < 0) {
                            increment = col;
                        } else if(grid[row][i] != null && !grid[row][i].getName().equals(selectedCar.getName())) {
                            increment -= increment - (col-1-i);
                            break;
                        }
                    }
                }

                if(col-increment < 0) {
                    increment = col;
                }
                Car newCar = new Car(selectedCar, row, col-increment);
                HashSet<Car> cars = currentConfig.getCars();
                cars.remove(selectedCar);
                cars.add(newCar);
                currentConfig = new JamConfig(currentConfig, cars);
                notifyObservers("Moved from (" + selectedRow + ", " + selectedCol
                        + ") to (" + row + ", " + col + ")");
                selectedCar = null;

            } else if(selectedCar.getOrientation().equals("V")) {
                int increment = selectedRow - selectedCar.getRow();
                int start = Math.min(selectedCar.getRow(), row);
                int end = Math.max(selectedCar.getRow()-increment, row+selectedCar.getLength()-increment-1);
                if(end >= currentConfig.getNumRows()) {
                    increment += end-currentConfig.getNumRows()+1;
                    end = currentConfig.getNumRows()-1;

                }
                if(start < 0) {
                    increment += start;
                    start = 0;
                }

                for(int i = start; i <= end; i++) {
                    if(grid[i][col] != null) {
                        if(!grid[i][col].getName().equals(selectedCar.getName())) {
                            if(i > row && start == selectedCar.getRow()) {
                                increment++;
                                break;
                            } else {
                                notifyObservers("Can't move from (" + selectedRow + ", " + selectedCol
                                        + ") to (" + row + ", " + col + ")");
                                selectedCar = null;
                                return;
                            }
                        }
                    }
                }

                if(start == row) {
                    for(int i = row-1; i >= row-increment; i--) {
                        if(i < 0) {
                            increment = row;
                        } else if(grid[i][col] != null && !grid[i][col].getName().equals(selectedCar.getName())) {
                            increment -= increment - (row-1-i);
                            break;
                        }
                    }
                }

                if(row-increment < 0) {
                    increment = row;
                }
                Car newCar = new Car(selectedCar, row-increment, col);
                HashSet<Car> cars = currentConfig.getCars();
                cars.remove(selectedCar);
                cars.add(newCar);
                currentConfig = new JamConfig(currentConfig, cars);
                notifyObservers("Moved from (" + selectedRow + ", " + selectedCol
                        + ") to (" + row + ", " + col + ")");
                selectedCar = null;
            }
        }
    }

    public int getNumRows() {
        return currentConfig.getNumRows();
    }
    public int getNumCols() {
        return currentConfig.getNumCols();
    }
    public Car getCar(int row, int col) {
        return currentConfig.getGrid()[row][col];
    }

    @Override
    public String toString() {
        return currentConfig.toString();
    }

    //empty because can't call loadFile until the file with main is added to observers (in the PTUI)
    public JamModel() {
    }

    public JamModel(String filename) {
        try {
            this.currentConfig = new JamConfig(filename);
            this.defaultConfig = filename;
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(1);
        }
    }
}
