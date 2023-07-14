package src.simulation;

import java.util.Scanner;

public class DataGetter {
    private static Scanner scanner = new Scanner(System.in);

    private DataGetter() {};

    public static Integer getDataInteger(String message) {
        System.out.print(message);
        
        return Integer.valueOf(scanner.nextInt());
    }
}
