package dev.zwazel.autobattler.classes.utils;

import com.google.gson.JsonObject;

import java.util.Random;

import static java.lang.Math.atan2;

public class Vector {
    private int x;
    private int y;

    public Vector() {
    }

    public Vector(JsonObject json) {
        this.x = json.get("x").getAsInt();
        this.y = json.get("y").getAsInt();
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    public static Vector getRandomDirection(boolean diagonalAllowed) {
        Random rand = new Random();
        int n = rand.nextInt((diagonalAllowed) ? Vector.DIRECTION.values().length : DIRECTION.DOWN.getNonDiagonalAmount());
        return DIRECTION.values()[n].getDirection();
    }

    public double directionTo360(Vector vector) {
        return atan2(vector.y - this.y, vector.x - this.x);
    }

    public Vector directionTo(Vector vector) {
        int x = Integer.compare(vector.x, this.x);
        int y = Integer.compare(vector.y, this.y);
        return new Vector(x, y);
    }

    public void add(int num) {
        add(new Vector(num, num));
    }

    public void add(Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
    }

    public Vector subtract(int num) {
        return subtract(new Vector(num, num));
    }

    public Vector subtract(Vector vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();

        return this;
    }

    public void multiply(int num) {
        this.x *= num;
        this.y *= num;
    }

    public Long toNumberId() {
        return Long.valueOf(this.x + "" + this.y);
    }

    public boolean greaterThan(Vector vector) {
        return (this.x > vector.x || this.y > vector.y);
    }

    public boolean smallerThan(Vector vector) {
        return (this.x < vector.x || this.y < vector.y);
    }

    public Double getDistanceFrom(Vector vector) {
        int xx = vector.x - this.x;
        int yy = vector.y - this.y;
        return Math.sqrt((xx * xx) + (yy * yy));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            Vector other = (Vector) obj;
            return (other.getX() == this.x && other.getY() == this.y);
        }
        return false;
    }

    public String toSize() {
        return "{\"width\": " + x +
                ",\"height\": " + y + "}";
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public enum DIRECTION {
        RIGHT(1, 0),
        LEFT(-1, 0),
        UP(0, -1),
        DOWN(0, 1),
        UP_RIGHT(1, -1),
        UP_LEFT(-1, -1),
        DOWN_RIGHT(1, 1),
        DOWN_LEFT(-1, 1);

        final Vector direction;

        DIRECTION(int x, int y) {
            this.direction = new Vector(x, y);
        }

        public Vector getDirection() {
            return direction;
        }

        public int getNonDiagonalAmount() {
            return 4;
        }
    }
}
