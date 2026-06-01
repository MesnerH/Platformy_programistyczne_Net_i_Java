package org.example;

public class Item {
    private int id;
    private int weight;
    private int value;

    // konstruktor
    public Item(int id, int weight, int value) {
        this.id = id;
        this.weight = weight;
        this.value = value;
    }

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Weight: " + weight + ", Value: " + value;
    }
}