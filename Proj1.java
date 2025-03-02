
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

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
    public static double[][] global_input_letter_i;
    public static int[][] global_target_letter_j;
    public static double[][] global_weights_j_i;

    //global header vals
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
                trainingSpecs(scanner);
            } else if (action == 2) {
                testingSpecs(scanner);
            }
            System.out.println("Welcome to our first neural network - A Perceptron Net!");
            System.out.println("1) Enter 1 to train the net on a data file");
            System.out.println("2) Enter 2 to test the net on a data file");
            System.out.println("3) Enter 3 to quit");
            action = scanner.nextInt();
        }
        System.out.println("Thank you for using the Net. Come back soon!");


    }

    public static double calculateY_in( double [] weights, double[] inputs) {
        double linearCombination = 0;
        //Add the bias
        for(int idx = 0; idx < inputs.length; idx++){
            double product = (inputs[idx] * weights[idx]);
            linearCombination += product;
        }
        return linearCombination;
    }

    public static int classifyLinearCombination(double linearCombination, double threshold) {
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

        //scanner.close(); // close scnner

        //call data read method
        readData(trainingDataFileName);

        //train
        int epochCountAfterTraining = trainAlgorithm(maxEpochs, outputWeightFileName, learningRate, thresholdTheta, thresholdWeightChange, global_weights_j_i, global_input_letter_i, global_target_letter_j, outputDimension, colDimension*outputDimension);
        if(epochCountAfterTraining == 0){
            System.out.println("Training did not converge in less than the max amount of epochs.");
        }
        else{
            System.out.println("Training converged after "+epochCountAfterTraining+ " epochs");
        }

        //save weights to file


    }

    public static void testingSpecs(Scanner scanner){

        System.out.println("Enter the trained net weight file name:");
        scanner.nextLine(); //get rid of newline
        trainedWeightsFileName = scanner.nextLine();

        System.out.println("Enter the testing/deploying dataset file name:");
        testingDataFileName = scanner.nextLine();

        System.out.println("Enter a file name to save the testing/deploying results:");
        outputTestResultsFileName = scanner.nextLine();

        //scanner.close();

        //call data read method
        readData(testingDataFileName);

        //call another method to read weights

        //test and save outputs to a file

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
            rowDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            colDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            outputDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            numberOfLetters = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            reader.readLine();

            System.out.println(rowDimension + " rows, " + colDimension + " columns, " + outputDimension + " output neurons, " + numberOfLetters + " different letter variations");

            //initialize weights and stuff
            double[][] weights_j_i = new double[7][64];
            double[][] input_letter_i = new double[21][64];
            int[][] target_letter_j = new int[21][7];

            Random random = new Random();
            //Initialize weights according to user input
            if (initWeightsBool == 1){
                for(int row = 0; row < colDimension; row++){
                    for(int col = 0; col < (colDimension*rowDimension); col++){
                        weights_j_i[row][col] = random.nextDouble(-0.5,0.5);
                    }
                }
            }

            for(int letter = 0; letter < numberOfLetters; letter++) {

                int placeInInputVector = 1;
                //bias is the first index of each row in the input matrix, making it 64 instead of 63
                input_letter_i[letter][0] = 1; // TODO: Should bias be 1 or 0 to start?
                for (int row = 0; row < rowDimension; row++) {

                    line = reader.readLine();
                    String[] numbers = line.trim().split("\\s+");

                    for (int i = placeInInputVector; i < placeInInputVector+7; i++) {
                        //read all into 1 vector
                        input_letter_i[letter][i] = Integer.parseInt(numbers[(i-1) % 7]);
                    }
                    placeInInputVector += 7;
                }
                reader.readLine(); //skip the space between input and target

                //target vector data
                line = reader.readLine();
                String[] targetNumberValues = line.trim().split("\\s+");
                for (int j = 0; j < 7; j++){
                    target_letter_j[letter][j] = Integer.parseInt(targetNumberValues[j]);
                }
                reader.readLine(); //TODO: n the future, keep this as label (maybe?)
                reader.readLine(); //skip space
            }
            //Print loaded data for verification
            for (int i = 0; i < numberOfLetters; i++) {
                System.out.print("Input Vector " + (i + 1) + ": ");
                for (int j = 0; j < 64; j++) {
                    System.out.print(input_letter_i[i][j] + " ");
                }
                System.out.println();
            }

            print2DDoubleArray(input_letter_i);
            print2DIntArray(target_letter_j);
            print2DDoubleArray(weights_j_i);
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
    public static void print2DDoubleArray(double[][] arr){
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

        //write algorithm
    public static int trainAlgorithm(int maxEpochs, String outputFileName,
                                     double learningRate, double thresholdTheta,
                                     double thresholdWeightChange, double[][] weights,
                                     double[][] inputSamples, int[][] targetOutputs,
                                     int numOutputNeurons, int numInputFeatures) {

        boolean hasConverged = false;
        int epochCount = 0;
        double[] weightChangeAmounts = new double[numInputFeatures + 1];  // Track weight changes

        while (!hasConverged && epochCount < maxEpochs) {
            boolean weightUpdated = false;
            Arrays.fill(weightChangeAmounts, 0.0);  // Reset weight changes for this epoch

            for (int outputNeuron = 0; outputNeuron < numOutputNeurons; outputNeuron++) {
                for (int sampleIdx = 0; sampleIdx < targetOutputs.length; sampleIdx++) {

                    int expectedOutput = targetOutputs[sampleIdx][outputNeuron];  // Get correct target
                    if (expectedOutput != -1 && expectedOutput != 1) {
                        System.err.println("Error: Target output must be -1 or 1, but got: " + expectedOutput);
                        continue; // Skip invalid data
                    }

                    //Compute perceptron output
                    double netInput = calculateY_in(weights[outputNeuron], inputSamples[sampleIdx]);
                    int predictedOutput = classifyLinearCombination(netInput, thresholdTheta);

                    //Check if weights need updating
                    if (expectedOutput != predictedOutput) {
                        for (int inputIdx = 0; inputIdx < numInputFeatures; inputIdx++) {
                            double previousWeight = weights[outputNeuron][inputIdx];
                            weights[outputNeuron][inputIdx] += learningRate * expectedOutput * inputSamples[sampleIdx][inputIdx];
                            weightChangeAmounts[inputIdx] = Math.abs(weights[outputNeuron][inputIdx] - previousWeight); // Track weight change
                        }

                        weightUpdated = true;
                    }
                }
            }

            //Check for convergence
            double maxWeightChange = findMaxWeightChange(weightChangeAmounts);
            if (maxWeightChange < thresholdWeightChange) {
                hasConverged = true;
            } else if (!weightUpdated) {
                hasConverged = true;
            } else {
                epochCount++;
                saveWeightsToFile(outputFileName, epochCount, weights);
            }
        }

        return (epochCount == maxEpochs) ? 0 : epochCount;
    }


    public static double findMaxWeightChange(double[] weightChanges) {
        double maxWeightChange = Math.abs(weightChanges[0]);  //Ensure absolute values are considered
        for (int i = 1; i < weightChanges.length; i++) {  //Start from 1 since 0 is initialized
            if (Math.abs(weightChanges[i]) > maxWeightChange) {
                maxWeightChange = Math.abs(weightChanges[i]);
            }
        }
        return maxWeightChange;
    }

    public static void saveWeightsToFile(String filename, int epoch, double[][] weights) {
        try {
            Path path = Paths.get(filename);
            StringBuilder weightData = new StringBuilder();
            weightData.append("Epoch ").append(epoch).append(System.lineSeparator());

            for (double[] outputNeuronWeights : weights) {
                weightData.append(Arrays.toString(outputNeuronWeights)).append(System.lineSeparator());
            }

            Files.write(path, weightData.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Error writing weights to file: " + e.getMessage());
        }
    }
}