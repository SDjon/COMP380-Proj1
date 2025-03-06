import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class training {
    public static String trainingDataFileName;
    public static int initWeightsBool;
    public static int maxEpochs;
    public static String outputWeightFileName;
    public static double learningRate;
    public static double thresholdTheta;
    public static double thresholdWeightChange;

    /**
     *
     * Gathers training specifications from the user via console input and initiates the training process
     * for the net.
     * @param scanner The {@code Scanner} object used to read user input from the console.
     *
     */
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

        Proj1.readData(trainingDataFileName);

        int epochCountAfterTraining = training.trainAlgorithm(maxEpochs, outputWeightFileName, learningRate, thresholdTheta, thresholdWeightChange, Proj1.global_weights_j_i, Proj1.global_input_letter_i, Proj1.global_target_letter_j, Proj1.outputDimension, Proj1.colDimension*Proj1.rowDimension);
        if(epochCountAfterTraining == 0){
            System.out.println("Training did not converge in less than the max amount of epochs.\n");
        }
        else{
            System.out.println("Training converged after "+epochCountAfterTraining+ " epochs\n");
        }
    }
    /**
     *
     * Trains the net using the given input samples and target outputs.
     * The algorithm adjusts the weight matrix iteratively using the perceptron learning rule
     * until convergence is achieved or the maximum number of epochs is reached.
     * @param maxEpochs: The maximum number of training iterations (epochs).
     * @param outputFileName: The file name where trained weight values will be saved.
     * @param learningRate: The learning rate (alpha), a value between 0 and 1.
     * @param thresholdTheta: The activation threshold for classifying perceptron output.
     * @param thresholdWeightChange: The minimum weight change threshold for determining convergence.
     * @param weights: A 2D array representing the weight matrix for output neurons.
     * @param inputSamples: A 2D array of input samples, each representing a feature vector.
     * @param targetOutputs: A 2D array representing expected target outputs for each input sample.
     * @param numOutputNeurons: The number of output neurons in the network.
     * @param numInputFeatures: The number of features in each input sample.
     * @return The number of epochs taken for convergence, or 0 if convergence was not reached
     * within the maximum number of epochs.
     *
     */
    public static int trainAlgorithm(int maxEpochs, String outputFileName,
                                     double learningRate, double thresholdTheta,
                                     double thresholdWeightChange, double[][] weights,
                                     double[][] inputSamples, int[][] targetOutputs,
                                     int numOutputNeurons, int numInputFeatures) {

        boolean hasConverged = false;
        int epochCount = 1;
        double[] weightChangeAmounts = new double[numInputFeatures + 1];  // Track weight changes

        while (!hasConverged && epochCount < maxEpochs) {
            boolean weightUpdated = false;
            Arrays.fill(weightChangeAmounts, 0.0);  // Reset weight changes for this epoch

            for (int outputNeuron = 0; outputNeuron < numOutputNeurons; outputNeuron++) {
                for (int sampleIdx = 0; sampleIdx < Proj1.numberOfLetters; sampleIdx++) {

                    int expectedOutput = targetOutputs[sampleIdx][outputNeuron];
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
                            // Track weight change
                            weightChangeAmounts[inputIdx] = Math.abs(weights[outputNeuron][inputIdx] - previousWeight);
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
    /**
     * Computes the weighted sum (linear combination) of input features and their corresponding weights.
     * @param weights A double array representing the weight values for each input feature.
     * @param inputs A double array representing the input feature values.
     * @return The computed weighted sum (linear combination) of inputs and weights.
     *
     */
    public static double calculateY_in( double [] weights, double[] inputs) {
        double linearCombination = 0;
        for(int idx = 0; idx < inputs.length; idx++){
            double product = (inputs[idx] * weights[idx]);
            linearCombination += product;
        }
        return linearCombination;
    }
    /**
     *
     * Classifies a given linear combination output based on a specified threshold.
     * @param linearCombination The computed weighted sum (net input) of the perceptron.
     * @param threshold The activation threshold for classification.
     * @return {@code 1} if the input exceeds the positive threshold,
     *         {@code -1} if it falls below the negative threshold,
     *         or {@code 0} if it is within the threshold range.
     *
     */
    public static int classifyLinearCombination(double linearCombination, double threshold) {
        if (linearCombination > threshold) {
            return 1;
        } else if (linearCombination < (-1 * threshold)) {
            return -1;
        } else {
            return 0;
        }
    }
    /**
     *
     * Finds the maximum absolute weight change from an array of weight changes.
     * @param weightChanges A double array representing the changes in weight values during training.
     * @return The maximum absolute weight change found in the array.
     *
     */
    public static double findMaxWeightChange(double[] weightChanges) {
        double maxWeightChange = Math.abs(weightChanges[0]);  //Ensure absolute values are considered
        for (int i = 1; i < weightChanges.length; i++) {  //Start from 1 since 0 is the bias
            if (Math.abs(weightChanges[i]) > maxWeightChange) {
                maxWeightChange = Math.abs(weightChanges[i]);
            }
        }
        return maxWeightChange;
    }
    /**
     *
     * Saves the trained weight values to a specified file.
     * @param filename The name of the file where weights will be saved.
     * @param epoch The final number of epochs completed in training.
     * @param weights A 2D array representing the trained weight values for output neurons.
     *
     */
    public static void saveWeightsToFile(String filename, int epoch, double[][] weights) {
        try {
            Path path = Paths.get(filename);
            StringBuilder weightData = new StringBuilder();
            weightData.append("ThresholdTheta ").append(thresholdTheta).append(" Epoch ").append(epoch).append(System.lineSeparator());

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
