package puzzles.jam.model;

public class Car {

    private String name;
    private int row;
    private int col;
    private int length;
    private String orientation;
    private String color;

    public Car(String name, int row, int col, int length, String orientation, String color) {
        this.name = name;
        this.row = row;
        this.col = col;
        this.length = length;
        this.orientation = orientation;
        this.color = color;
    }
    public Car(Car car, int row, int col) {
        this.name = car.getName();
        this.row = row;
        this.col = col;
        this.length = car.getLength();
        this.orientation = car.getOrientation();
        this.color = car.getColor();
    }

    public String getName() {
        return name;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public String getColor() {
        return color;
    }
    public int getLength() {return length;}
    public String getOrientation() {return orientation;}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + row + col;
    }
}
