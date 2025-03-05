import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class testing {
    //global var for testing
    public static String trainedWeightsFileName;
    public static String testingDataFileName;
    public static String outputTestResultsFileName;

    public static String[] possibleOutputs = {"A","B","C","D","E","J","K"};

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

    public static void readWeights(){
        try (BufferedReader reader = new BufferedReader(new FileReader(trainedWeightsFileName))){
            String line;
            reader.readLine(); //get rid of header
            for (int j = 0; j < 7; j++){
                line = reader.readLine();
                String[] parts = line.split("[\\[\\],]+");
                double[] nextWeightArray = new double[64];
                for (int i =0; i< 64; i++){
                    nextWeightArray[i] = Double.parseDouble(parts[i+1]);
                }
                Proj1.global_weights_j_i[j] = nextWeightArray;
            }
        } catch (Exception e){
            System.out.println("problem getting weigths from file");
        }
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
                String actualLetter = Proj1.global_labels[i];
                //figure out what character is being indicated
                String outputChar = "";
                int oneCounter = 0;
                if(classifiedOutput[0] == 1){
                    outputChar = "A";
                    oneCounter++;
                } else if(classifiedOutput[1] == 1){
                    outputChar = "B";
                    oneCounter++;

                } else if(classifiedOutput[2] == 1){
                    outputChar = "C";
                    oneCounter++;

                } else if(classifiedOutput[3] == 1){
                    outputChar = "D";

                    oneCounter++;

                } else if(classifiedOutput[4] == 1){
                    outputChar = "E";
                    oneCounter++;


                } else if(classifiedOutput[5] == 1){
                    outputChar = "J";
                    oneCounter++;


                } else if(classifiedOutput[6] == 1){
                    outputChar = "K";
                    oneCounter++;

                }
                if(oneCounter != 1){
                    outputChar = "Unclassified";
                }

                String classifiedLetter = outputChar;

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
            output[j] = training.classifyLinearCombination(netInput,1.0); //TODO this should use activation function with specified theta
        }
        return output;
    }

}
