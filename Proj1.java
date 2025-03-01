
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Proj1 {

    //global var for training
    public static String trainingDataFileName;
    public static int initWeightsBool;
    public static int maxEpochs;
    public static String outputWeightFileName;
    public static double learningRate;
    public static double thresholdTheta;
    public static double thresholdWeightChange;

    //global var for testing
    public static String trainedWeightsFileName;
    public static String testingDataFileName;
    public static String outputTestResultsFileName;

    //global data
    public static int[][] global_input_letter_i;
    public static int[][] global_target_letter_j;
    public static double[][] global_weights_j_i;

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

        System.out.println("Enter the training data file name:");
        scanner.nextLine(); //get rid of newline
        trainingDataFileName = scanner.nextLine();

        System.out.println("Enter 0 to initialize weights to 0, enter 1 to initialize weights to random values between -0.5 and 0.5:");
        initWeightsBool = scanner.nextInt();

        System.out.println("Enter the maximum number of training epochs:");
        maxEpochs = scanner.nextInt();

        System.out.println("Enter a file name to save the trained weight values:");
        scanner.nextLine();
        outputWeightFileName = scanner.nextLine();

        System.out.println("Enter the learning rate alpha from 0 to 1 but not including 0:");
        learningRate = scanner.nextDouble();

        System.out.println("Enter the threshold theta:");
        thresholdTheta = scanner.nextDouble();

        System.out.println("Enter the threshold to be used for measuring weight changes:");
        thresholdWeightChange = scanner.nextDouble();

        scanner.close(); // close scnner

        //call data read method
        readData(trainingDataFileName);

        //train
        trainAlgorithm(global_weights_j_i,global_input_letter_i,global_target_letter_j);


    }

    public static void testingSpecs(Scanner scanner){

        System.out.println("Enter the trained net weight file name:");
        scanner.nextLine(); //get rid of newline
        trainedWeightsFileName = scanner.nextLine();

        System.out.println("Enter the testing/deploying dataset file name:");
        testingDataFileName = scanner.nextLine();

        System.out.println("Enter a file name to save the testing/deploying results:");
        outputTestResultsFileName = scanner.nextLine();

        scanner.close();

        //call data read method
        readData(testingDataFileName);

        //call another method to read weights

        //test

    }

    private static boolean validateInteger(Scanner myScanner) {
        if (!myScanner.hasNextInt()) {
            System.out.println("Invalid x: Please enter an integer.");
            myScanner.close();
            return true;
        }
        return false;
    }
    /**
     * this function reads the input from a file into input_letter_i and target_letter_j, which encases all the input for each letter
     * */
    public static void readData(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line; //reused var, for when contents from readline are read

            //headerData
            int rowDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            int colDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            int outputDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            int numberOfLetters = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            reader.readLine();

            System.out.println(rowDimension + ", " + colDimension + ", " + outputDimension + ", " + numberOfLetters);

            //initialize weights and stuff
            double[][] weights_j_i = new double[7][64];
            int[][] input_letter_i = new int[21][64];
            int[][] target_letter_j = new int[21][7];

            for(int letter = 0; letter < 21; letter++) {

                // 9 row data is found in header (start reading block
                int placeInInputVector = 1;
                //at beginning of block of sample data
                //bias is awlays 1
                input_letter_i[letter][0] = 1;
                for (int row = 0; row < 9; row++) {

                    line = reader.readLine();
                    String[] numbers = line.trim().split("\\s+");


                    for (int i = placeInInputVector; i < placeInInputVector+7; i++) {
                        input_letter_i[letter][i] = Integer.parseInt(numbers[(i-1) % 7]);
                        //read all into 1 vector
                    }
                    placeInInputVector += 7;


                }
                reader.readLine(); //skip the space between input and target

                //target data
                //get 1 vector of training data
                line = reader.readLine();
                String[] targetNumberValues = line.trim().split("\\s+");
                for (int j = 0; j < 7; j++){
                    target_letter_j[letter][j] = Integer.parseInt(targetNumberValues[j]);
                }
                reader.readLine(); //in the future, keep this as label maybe
                reader.readLine();//skip space
            }

            print2DIntArray(input_letter_i);
            print2DIntArray(target_letter_j);
            global_input_letter_i = input_letter_i;
            global_target_letter_j = target_letter_j;
            global_weights_j_i = weights_j_i;


        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }
    }

    public static void print2DIntArray(int[][] arr){
        int count = 0;
        for (int i = 0; i < arr.length; i++){
            for (int j= 0;j< arr[i].length; j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
            count++;
        }
        System.out.println("Number of Rows: " + count);
    }

    public static void trainAlgorithm(double[][] weights_j_i,int[][] input_letter_i,int[][] target_letter_j){
        boolean converged = false;

        //write algorithm
    }
}