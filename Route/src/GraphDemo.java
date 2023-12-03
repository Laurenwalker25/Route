import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network, showing the functionality of GraphProcessor and using
 * Visualize
 * To do: Add your name(s) as authors
 */
public class GraphDemo {
    public static void main(String[] args) {
        //from
        System.out.println(" ");
        System.out.println("Where are you coming from?");
        System.out.print("Enter a city and state abreivation (e.g. Durham NC): ");
        Scanner input1 = new Scanner(System.in);
        String s1 = input1.nextLine();
        System.out.println("You entered " + s1 + " as the starting point");

        //to
        System.out.println(" ");
        System.out.println("Where are you going to?");
        System.out.print("Enter a city and state abreivation (e.g. Durham NC): ");
        Scanner input2 = new Scanner(System.in);
        String s2 = input2.nextLine();
        System.out.println("You entered " + s2 + " as the destination");
        





    }
}