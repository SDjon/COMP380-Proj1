import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class training {
    //global var for training
    public static String trainingDataFileName;
    public static int initWeightsBool;
    public static int maxEpochs;
    public static String outputWeightFileName;
    public static double learningRate;
    public static double thresholdTheta;
    public static double thresholdWeightChange;

    //global data
    public static double[][] global_input_letter_i;
    public static int[][] global_target_letter_j;
    public static double[][] global_weights_j_i;

    //global header vals
    public static int rowDimension;
    public static int colDimension;
    public static int outputDimension;
    public static int numberOfLetters;
    

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
        Proj1.readData(trainingDataFileName);

        //train
        int epochCountAfterTraining = training.trainAlgorithm(maxEpochs, outputWeightFileName, learningRate, thresholdTheta, thresholdWeightChange, global_weights_j_i, global_input_letter_i, global_target_letter_j, outputDimension, colDimension*outputDimension);
        if(epochCountAfterTraining == 0){
            System.out.println("Training did not converge in less than the max amount of epochs.");
        }
        else{
            System.out.println("Training converged after "+epochCountAfterTraining+ " epochs");
        }

        //save weights to file


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
            }
        }

        saveWeightsToFile(outputFileName, epochCount, weights);

        return (epochCount == maxEpochs) ? 0 : epochCount;
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
