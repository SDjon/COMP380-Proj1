import java.util.Scanner;

public class Proj1 {
    public static void main(String[] args){
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
        } else if (action == 2){
            testingSpecs(scanner);
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
