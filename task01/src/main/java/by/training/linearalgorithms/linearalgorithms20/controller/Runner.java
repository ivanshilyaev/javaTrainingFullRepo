package by.training.linearalgorithms.linearalgorithms20.controller;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {
    public static double findAreaByCircumference(double circumference) {
        return Math.pow(circumference, 2) / (4 * Math.PI);
    }

    public static double readCircumference() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter circumference - positive double:");
        return scanner.nextDouble();
    }

    public static void main(String[] args) {
        double circumference = 0;
        try {
            circumference = readCircumference();
        } catch (InputMismatchException e) {
            System.out.println("Incorrect data");
            System.exit(0);
        }
        System.out.println("Area is " + findAreaByCircumference(circumference));
    }
}
