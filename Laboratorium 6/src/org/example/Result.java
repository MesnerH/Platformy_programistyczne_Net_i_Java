package org.example;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private List<Integer> id_Items;
    private List<Integer> quantities;
    private int total_Value;
    private int total_Weight;

    // konstruktor
    public Result() {
        id_Items = new ArrayList<>();
        quantities = new ArrayList<>();
        total_Value = 0;
        total_Weight = 0;
    }

    public List<Integer> getId_Items() {
        return id_Items;
    }
    public List<Integer> getQuantities() {
        return quantities;
    }
    public int getTotal_Value() {
        return total_Value;
    }
    public void setTotal_Value(int total_Value) {
        this.total_Value = total_Value;
    }
    public int getTotal_Weight() {
        return total_Weight;
    }
    public void setTotal_Weight(int total_Weight) {
        this.total_Weight = total_Weight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Items in knapsack:\n");
        for (int i = 0; i < id_Items.size(); i++) {
            sb.append("ID: ").append(id_Items.get(i)).append(" | Quantity: ").append(quantities.get(i)).append("\n");
        }
        sb.append("total value: ").append(total_Value).append("\ntotal weight: ").append(total_Weight);
        return sb.toString();
    }
}