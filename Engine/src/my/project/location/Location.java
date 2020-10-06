package my.project.location;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.awt.*;
import java.util.Objects;

public class Location {
    public static final int xMaxCoordinate = 50;
    public static final int yMaxCoordinate = 50;
    public static final int xMinCoordinate = 1;
    public static final int yMinCoordinate = 1;

    private int x;
    private int y;

    public Location(int x, int y){
        setX(x);
        setY(y);
    }

    public Location(Point point){
        setX(point.x);
        setY(point.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) throws ValueException {
        if (x <= xMaxCoordinate && x >= xMinCoordinate) {
            this.x = x;
        } else {
            throw new ValueException("Coordinate x is out of range");
        }
    }

    public void setY(int y) throws ValueException {
        if (y <= yMaxCoordinate && y >= yMinCoordinate) {
            this.y = y;
        } else {
            throw new ValueException("Coordinate y is out of range");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
