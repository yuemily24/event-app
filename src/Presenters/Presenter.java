package Presenters;

import java.util.List;

public class Presenter {

    /**
     * Prints the String that is passed as a parameter to the console.
     *
     * @param output The String to be printed to the console.
     */
    public void print(String output) {
        System.out.println(output);
    }

    /**
     * Prints the List that is passed as a parameter to the console.
     *
     * @param output The List to be printed to the console.
     */
    public void print(List<String> output) {
        System.out.println(output);
    }
}
