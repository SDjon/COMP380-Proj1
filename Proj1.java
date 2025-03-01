
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

        Scanner scan2 = new Scanner(System.in);

        System.out.println("Enter the training data file name:");
        String fileNameInput = scan2.nextLine();
        System.out.println(fileNameInput);

        System.out.println("Enter 0 to initialize weights to 0, enter 1 to initialize weights to random values between -0.5 and 0.5:");
        int initWeightsBool = scan2.nextInt();

        System.out.println("Enter the maximum number of training epochs:");
        int maxEpochs = scan2.nextInt();

        System.out.println("Enter a file name to save the trained weight values:");
        String fileNameOutput = scan2.nextLine();

        System.out.println("Enter the learning rate alpha from 0 to 1 but not including 0:");
        double learningRate = scan2.nextDouble();

        System.out.println("Enter the threshold theta:");
        double thresholdTheta = scan2.nextDouble();

        System.out.println("Enter the threshold to be used for measuring weight changes:");
        double thresholdWeightChange = scan2.nextDouble();

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

            //headerData
            int rowDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            int colDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            int outputDimension = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            int numberOfLetters = Integer.parseInt(reader.readLine().trim().split("\\s+")[0]);
            reader.readLine();

            System.out.println(rowDimension + ", " + colDimension + ", " + outputDimension + ", " + numberOfLetters);

            //initialize weights and stuff
            int[][] weights_j_i = new int[7][64];
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
                reader.readLine(); //in the future, keep this as label
                reader.readLine();
            }
            int count = 0;
            for (int i = 0; i < 21; i++){
                for (int j= 0;j< 64; j++){
                    System.out.print(input_letter_i[i][j] + " ");
                }
                System.out.println();
                count++;
            }
            System.out.println(count);


            //train
            //trainAlgorithm(weights_j_i,input_letter_i,target_letter_j);



        } catch (IOException e) {
            // Handle file reading errors
            e.printStackTrace();
        }
    }

    public static void trainAlgorithm(String file_path, int initWeightsBool, int max_training_epochs, String fileNameOutput, double learning_rate, double thresholdTheta, double thresholdWeightChange){
        boolean converged = false;
    }
}