import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class testing {
    //global var for testing
    public static String trainedWeightsFileName;
    public static String testingDataFileName;
    public static String outputTestResultsFileName;

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

        //test and save outputs to a file
        runTesting();
        
    }

    public static void runTesting() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputTestResultsFileName))) {
            int correctPredictions = 0;
            int totalSamples = Proj1.global_input_letter_i.length;

            for (int i = 0; i < totalSamples; i++) {
                // Get the actual and classified output
                int[] actualOutput = Proj1.global_target_letter_j[i];
                int[] classifiedOutput = classifySample(Proj1.global_input_letter_i[i]);

                // Convert classified output to letter
                char actualLetter = (char) ('A' + i);
                char classifiedLetter = (char) ('A' + i); // Assume classification maps the same

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

            System.out.println("Testing completed. Results saved to " + outputTestResultsFileName);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static int[] classifySample(double[] input) {
        int[] output = new int[Proj1.outputDimension];

        for (int j = 0; j < Proj1.outputDimension; j++) {
            double netInput = 0.0;

            for (int k = 0; k < input.length; k++) {
                netInput += input[k] * Proj1.global_weights_j_i[j][k];
            }

            // Apply threshold to determine output as -1, 0, or 1
            output[j] = (netInput > 0) ? 1 : -1;
        }
        return output;
    }

}
