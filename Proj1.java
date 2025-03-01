
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Proj1 {



    public static void main(String[] args) {
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
        training(fileNameInput, initWeightsBool, maxEpochs, fileNameOutput, learningRate, thresholdTheta, thresholdWeightChange);
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

    public static void training(String file_path, int initWeightsBool, int max_training_epochs, String fileNameOutput, double learning_rate, double thresholdTheta, double thresholdWeightChange){
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String line;



            // Skip first 5 lines
            for (int i = 0; i < 5; i++) {
                reader.readLine();
            }
            //initialize weights and stuff
            int[][] w_j_i = new int[7][64];
            int[][] input_letter_i = new int[21][64];
            int[][] target_letter_j = new int[21][7];


            while ((line = reader.readLine()) != null) {


                // 9 row data is found in header (start reading block
                for (int letter = 0; letter < 9 +3; letter++) {

                    //at beginning of block of sample data
                    //bias is awlays 1
                    input_letter_i[letter][0] = 1;

                    line = reader.readLine();
                    String[] numbers = line.trim().split("\\s+");
                    if (numbers.length != 7) { // Ensure each row has exactly 7 elements
                        System.out.println("Invalid row length at line " + (numbers.length + 1));
                        continue;
                        //error
                    }
                    for (int i = 0; i < 7; i++) {
                        input_letter_i[letter][i] = Integer.parseInt(numbers[letter]);
                        //read all into 1 vector
                    }

                    reader.readLine(); //skip the space between input and target

                    //target data
                    //get 1 vector of training data

                    line = reader.readLine();
                    String[] targetNumberValues = line.trim().split("\\s+");
                    for (int j = 0; j < 7; j++){
                        target_letter_j[letter][j] = Integer.parseInt(targetNumberValues[j]);
                    }
                    reader.readLine(); //in the future, keep this as label
                }

            }

            //train

        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }
    }

    public static void trainAlgorithm(String file_path, int initWeightsBool, int max_training_epochs, String fileNameOutput, double learning_rate, double thresholdTheta, double thresholdWeightChange){
        boolean converged = false
    }
}