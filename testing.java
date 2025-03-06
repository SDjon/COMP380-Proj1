import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class testing {
    //global var for testing
    public static String trainedWeightsFileName;
    public static String testingDataFileName;
    public static String outputTestResultsFileName;

    public static String[] possibleOutputs = {"A","B","C","D","E","J","K"};

    /**
     *
     * Gathers testing specifications from the user via console input and initiates the testing process
     * for the net.
     * @param scanner The {@code Scanner} object used to read user input from the console.
     *
     */
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
        Proj1.readData(testingDataFileName);

        //call another method to read weights
        readWeights();

        //test and save outputs to a file
        runTesting();
        
    }
    /**
     * Reads weights from a file in the format:
     * ThresholdTheta x Epoch y
     * [-1.0, -1.0, 0.0, 1.0, 0.0, ...
     * ...
     * Extracts theta into training.thresholdTheta and all weights into 2d double array Proj1.global_weights_j_i
     */
    public static void readWeights(){
        try (BufferedReader reader = new BufferedReader(new FileReader(trainedWeightsFileName))){
            String line;
            training.thresholdTheta = Double.parseDouble(reader.readLine().trim().split(" ")[1]); //get rid of header
            for (int j = 0; j < Proj1.outputDimension; j++){
                line = reader.readLine();
                String[] parts = line.split("[\\[\\],]+");
                double[] nextWeightArray = new double[64];
                for (int i =0; i< 64; i++){
                    nextWeightArray[i] = Double.parseDouble(parts[i+1]);
                }
                Proj1.global_weights_j_i[j] = nextWeightArray;
            }
        } catch (Exception e){
            System.out.println("Error: problem getting weights from file");
        }
    }
    /**
     * Runs algorithm using given weights saved in Proj1.global_weights_j_i, and compare the resulting
     * letter to the letter specified in the test file. Outputs these results to a new file in user
     * specified filename, along with accuracy of model
     *
     */
    public static void runTesting() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputTestResultsFileName))) {
            int correctPredictions = 0;
            int totalSamples = Proj1.global_input_letter_i.length;

            for (int i = 0; i < totalSamples; i++) {
                // Get the actual and classified output
                int[] actualOutput = Proj1.global_target_letter_j[i];
                int[] classifiedOutput = classifySample(Proj1.global_input_letter_i[i]);

                // Convert classified output to letter
                String actualLetter = Proj1.global_labels[i];
                //figure out what character is being indicated
                String classifiedLetter = getString(classifiedOutput);

                // Write to file
                writer.write("Actual Output:\n");
                writer.write(actualLetter + "\n");
                writer.write(Arrays.toString(actualOutput).replace(",", "").replace("[", "").replace("]", "") + "\n");

                writer.write("Classified Output:\n");
                writer.write(classifiedLetter + "\n");
                writer.write(Arrays.toString(classifiedOutput).replace(",", "").replace("[", "").replace("]", "") + "\n\n");

                // Compare actual vs classified
                if (Arrays.equals(actualOutput, classifiedOutput)) {
                    correctPredictions++;
                }
            }

            // Compute accuracy
            double accuracy = (correctPredictions / (double) totalSamples) * 100;
            writer.write("Overall Classification Accuracy: " + String.format("%.2f", accuracy) + "%\n");

            System.out.println("Testing completed. Results saved to " + outputTestResultsFileName +"\n");

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Given the results of the algorithm, which outputs outputDimension bipolar target values, identify
     * what letter that represents. Outputs "undecided" if there are 2 or more 1 values or
     * if there are zero 1 values.
     * @param classifiedOutput (int[]) target array of outputDimension values corresponding to the outputDimension possible letters.
     * @return (String) The string letter associated with the target output, or the word "undecided"
     * if there is not exactly one 1 in the array
     *
     */
    private static String getString(int[] classifiedOutput) {
        String resultLetter = "";
        int oneCounter = 0;
        for (int i = 0; i < Proj1.outputDimension; i++){

            if(classifiedOutput[i] == 1) {
                resultLetter = Proj1.global_labels[i];
                oneCounter++;
            }
        }
        if(oneCounter != 1){
            resultLetter = "undecided";
        }

        return resultLetter;
    }
    /**
     * Returns the outputDimension length target array calculated using given weights in Proj1.global_weights_j_i
     * with input from test file
     * @param input (double[]) A double array of the input from the test file, with input[0] = 1 for bias,
     *              and row*col more bipolar values
     * @return (int[]) The outputDimension length target array, corresponding to a classified letter
     *
     */
    private static int[] classifySample(double[] input) {
        int[] output = new int[Proj1.outputDimension];
        for (int j = 0; j < Proj1.outputDimension; j++) {

            double netInput = training.calculateY_in(Proj1.global_weights_j_i[j], input);

            // Apply threshold to determine output as -1, 0, or 1
            output[j] = training.classifyLinearCombination(netInput,training.thresholdTheta);
        }
        return output;
    }

}
