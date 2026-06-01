package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seed: ");
        int seed = Integer.parseInt(scanner.nextLine());
        System.out.println("How many items: ");
        int n = Integer.parseInt(scanner.nextLine());
        System.out.println("Lower bound (np. 1): ");
        int lowerBound = Integer.parseInt(scanner.nextLine());
        System.out.println("Upper bound (np. 10): ");
        int upperBound = Integer.parseInt(scanner.nextLine());

        // konstruktor
        Problem problem = new Problem(n, seed, lowerBound, upperBound);
        System.out.println(problem.toString());

        System.out.println("\nCapacity of Knapsack:");
        int capacity = Integer.parseInt(scanner.nextLine());

        Result final_Result = problem.Solve(capacity);
        System.out.println(final_Result.toString());

        scanner.close();
    }
}