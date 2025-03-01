
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Proj1 {

    public static void main(String[] args) {
        //first read the file
        //do the algorithm
        //print the output
        System.out.println("Welcome to our first neural network - A Perceptron Net!");
        System.out.println("1) Enter 1 to train the net on a data file");
        System.out.println("2) Enter 2 to test the net on a data file");
        System.out.println("3) Enter 3 to quit");

        //create scanner for input
        Scanner scanner = new Scanner(System.in);

        //return null if input is not an integer
        if (validateInteger(scanner)) return;
        int action = scanner.nextInt();

        if (action == 1) {
            trainingSpecs(scanner);
        } else if (action == 2) {
            testingSpecs(scanner);
        }


    }

    double calculateY_in( double [] weights, int[] inputs) {
        double linearCombination = 0;
        //Add the bias
        linearCombination+= inputs[0];
        for(int idx = 1; idx < inputs.length; idx++){
            double product = (inputs[idx] * weights[idx]);
            linearCombination += product;
        }
        return linearCombination;
    }

    int classifyLinearCombination(double linearCombination, double threshold) {
        if (linearCombination > threshold) {
            return 1;
        } else if (linearCombination < (-1 * threshold)) {
            return -1;
        } else {
            return 0;
        }
    }

    public static void trainingSpecs(Scanner scanner){

        //number of epochs before convergence
        int epochsUsed = 0;

        System.out.println("Enter the training data file name:");
        String fileNameInput = scanner.nextLine();

        System.out.println("Enter 0 to initialize weights to 0, enter 1 to initialize weights to random values between -0.5 and 0.5:");
        int initWeightsBool = scanner.nextInt();

        System.out.println("Enter the maximum number of training epochs:");
        int maxEpochs = scanner.nextInt();

        System.out.println("Enter a file name to save the trained weight values:");
        String fileNameOutput = scanner.nextLine();

        System.out.println("Enter the learning rate alpha from 0 to 1 but not including 0:");
        double learningRate = scanner.nextDouble();

        System.out.println("Enter the threshold theta:");
        double thresholdTheta = scanner.nextDouble();

        System.out.println("Enter the threshold to be used for measuring weight changes:");
        double thresholdWeightChange = scanner.nextDouble();

        //call training method
        //params are fileNameInput, initWeightsBool, maxEpochs, fileNameOutput, learningRate, thresholdTheta, thresholdWeightChange

    }

    public static void testingSpecs(Scanner scanner){
        System.out.println("testing");


    }
    private static boolean validateInteger(Scanner myScanner) {
        if (!myScanner.hasNextInt()) {
            System.out.println("Invalid x: Please enter an integer.");
            myScanner.close();
            return true;
        }
        return false;
    }
    private static boolean validateString(Scanner myScanner){
        if (!myScanner.hasNextLine()){
            System.out.println("Invalid x: Please enter a string");
            myScanner.close();
            return true;
        }
        return false;
    }
}

public class training(file_path, max_training_epochs, threshold, learning_rate)
    String file = file_path;
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;

        int[][] w_i_j = new int[7][9];
        int[][] w_b_c
        while ((line = reader.readLine()) != null) {
            // Skip first 5 lines
            for (int i = 0; i < 5; i++) {
                reader.readLine();
            }
            
            for (int i = 0; i < 9; i++) {
                line = reader.readLine();
                String[] numbers = line.trim().split("\\s+");
                if (numbers.length != 7) { // Ensure each row has exactly 7 elements
                    System.out.println("Invalid row length at line " + (row + 1));
                    continue;
                }
                for (int j = 0; j < 7; j++) {
                    w_i_j[i][j] = Integer.parseInt(numbers[j]);
                }
                i++;
            }
            
        }
    } catch (IOException e) {
        // Handle file reading errors
        e.printStackTrace();
    }
