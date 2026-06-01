package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Problem {
    private int number_of_items;
    private int seed;
    private int lowerBound;
    private int upperBound;
    private List<Item> items;

    // konstruktor
    public Problem(int number_of_items, int seed, int lowerBound, int upperBound) {
        this.number_of_items = number_of_items;
        this.seed = seed;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.items = new ArrayList<>(number_of_items);

        Random random = new Random(seed);

        int zakres = (upperBound - lowerBound) + 1;

        for (int i = 0; i < number_of_items; i++) {
            int weight = random.nextInt(zakres) + lowerBound;
            int value = random.nextInt(zakres) + lowerBound;
            items.add(new Item(i, weight, value));
        }
    }

    public Result Solve(int capacity) {
        Result result = new Result();

        List<Item> sorted_Items = new ArrayList<>(this.items);
        sorted_Items.sort(Comparator.comparingDouble((Item x) -> (double) x.getValue() / x.getWeight()).reversed());

        int remainingCapacity = capacity;

        for (Item item : sorted_Items) {
            if (remainingCapacity <= 0) break;

            int quantity = remainingCapacity / item.getWeight();

            if (quantity > 0) {
                result.getId_Items().add(item.getID());
                result.getQuantities().add(quantity);
                result.setTotal_Weight(result.getTotal_Weight() + (quantity * item.getWeight()));
                result.setTotal_Value(result.getTotal_Value() + (quantity * item.getValue()));
                remainingCapacity -= quantity * item.getWeight();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "Items count: " + number_of_items + ", Seed: " + seed + ", Range: [" + lowerBound + ", " + upperBound + "]\nAll items:\n";
        for (Item item : items) {
            result += item.toString() + "\n";
        }
        return result;
    }
}