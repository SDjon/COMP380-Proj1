/**
 * Program Name: Project 1 - Perceptron Neural Network
 * Authors: Kian Drees, Precee Ginigeme, Jonathan Rivera, Gianpaolo Tabora
 * Last Date Modified: 3/5/2025
 * Description:
 *      This program implements a simple perceptron neural network for training
 *      on letter data. It reads input data, initializes weights, trains the model
 *      using a learning rate, and saves the final weights after convergence.
 *      The perceptron uses bipolar values (-1,1) and the threshold theta 
 *      determines classification sensitivity.
 * 
 * Usage: 
 *      Run the program and follow the prompts to train or use the perceptron net.
 *      The trained weights will be saved to an output file. The test results will be saved to an output file.
 * 
 * Inputs:
 *      Training data file with letter representations.
 *      Learning rate, maximum epochs, threshold theta, and threshold weight change.
 *      Test file to tes the net with a specified trained weight file.
 * 
 * Outputs:
 *      Trained weights stored in a file.
 *      Test results stored in a file.
 *      Console message indicating convergence status.
 */

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Proj1 {

    //data
    public static double[][] global_input_letter_i;
    public static int[][] global_target_letter_j;
    public static double[][] global_weights_j_i;

    //labels;
    public static String[] global_labels;

    //header values from reading in data
    public static int rowDimension;
    public static int colDimension;
    public static int outputDimension;
    public static int numberOfLetters;

    public static void main(String[] args) {
        System.out.println("Welcome to our first neural network - A Perceptron Net!");
        System.out.println("1) Enter 1 to train the net on a data file");
        System.out.println("2) Enter 2 to test the net on a data file");
        System.out.println("3) Enter 3 to quit");

        Scanner scanner = new Scanner(System.in);

        //return null if input is not an integer
        if (validateInteger(scanner)) return;
        int action = scanner.nextInt();
        while (action != 3) {
                
            if (action == 1) {
                training.trainingSpecs(scanner);
            } else if (action == 2) {
                testing.testingSpecs(scanner);
            }
            System.out.println("Welcome to our first neural network - A Perceptron Net!");
            System.out.println("1) Enter 1 to train the net on a data file");
            System.out.println("2) Enter 2 to test the net on a data file");
            System.out.println("3) Enter 3 to quit");
            action = scanner.nextInt();
        }
        System.out.println("Thank you for using the Net. Come back soon!");

    }

    /**
     *
     * Validates if the user input is an integer.
     * If the input is not an integer, it prints an error message,
     * closes the Scanner, and returns {@code true} indicating invalid input.
     * Otherwise, it returns {@code false}.
     *
     * @param myScanner: the Scanner object to read input from
     * @return {@code true} if the input is invalid (not an integer), {@code false} otherwise
     *
     */
    public static boolean validateInteger(Scanner myScanner) {
        if (!myScanner.hasNextInt()) {
            System.out.println("Invalid x: Please enter an integer.");
            myScanner.close();
            return true;
        }
        return false;
    }

/**
 *
 * Reads input data from a specified file and initializes the input, target, and weight matrices
 * for letter recognition. This method parses header information, initializes weights if required,
 * and reads input and target letter representations into corresponding arrays.
 *
 * @param filename: The file to read the data from
 *
 */
    public static void readData(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line; //reused var, for when contents from readline are read

            //headerData
            rowDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            colDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            outputDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            numberOfLetters = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            reader.readLine();

            double[][] weights_j_i = new double[outputDimension][rowDimension * colDimension + 1];
            double[][] input_letter_i = new double[numberOfLetters][rowDimension * colDimension + 1];
            int[][] target_letter_j = new int[numberOfLetters][outputDimension];
            String[] labels = new String[numberOfLetters];

            Random random = new Random();
            //Initialize weights according to user input
            System.out.println(training.initWeightsBool);
            if (training.initWeightsBool == 1){
                for(int row = 0; row < colDimension; row++){
                    for(int col = 0; col < (colDimension*rowDimension); col++){
                        weights_j_i[row][col] = -0.5 + random.nextDouble();
                    }
                }
            }


            for(int letter = 0; letter < numberOfLetters; letter++) {

                int placeInInputVector = 1;
                //bias is the first index of each row in the input matrix, bias input is always 1
                input_letter_i[letter][0] = 1;
                for (int row = 0; row < rowDimension; row++) {

                    line = reader.readLine();
                    String[] numbers = line.trim().split("\\s+");

                    for (int i = placeInInputVector; i < placeInInputVector+colDimension; i++) {
                        //read all into 1 vector
                        input_letter_i[letter][i] = Integer.parseInt(numbers[(i-1) % colDimension]);
                    }
                    placeInInputVector += colDimension;
                }
                reader.readLine(); //skip the space between input and target

                //target vector data
                line = reader.readLine();
                String[] targetNumberValues = line.trim().split("\\s+");
                for (int j = 0; j < outputDimension; j++){
                    target_letter_j[letter][j] = Integer.parseInt(targetNumberValues[j]);
                }
                labels[letter] = String.valueOf(reader.readLine().charAt(0));
                reader.readLine(); //skip space
            }

            global_input_letter_i = input_letter_i;
            global_target_letter_j = target_letter_j;
            global_weights_j_i = weights_j_i;
            global_labels = labels;


        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }
    }

}