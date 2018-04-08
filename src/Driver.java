import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Driver{
    private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String args[]){
        Restaurant restaurant = new Restaurant();
        String name;
        int seats;
        int tables;
        System.out.println("Enter your restaurant configuration:");
        System.out.print("\nHow many tables does your pet-friendly section have? ");
        tables = Integer.parseInt(stdin.readLine());
        for(int i = 0; i < tables; i++){
            System.out.print("\nEnter table name: ");
            name = stdin.readLine();
            System.out.print("\nEnter number of seats: ");
            seats = Integer.parseInt(stdin.readLine());
        }
    }   
}   
