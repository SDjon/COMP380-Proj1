public class Proj1 {

    public static void main(String[] args){
        //first read the file
        //do the algorithm
        //print the output

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

    int classifyLinearCombination(double linearCombination, double threshold){
        if(linearCombination > threshold){
            return 1;
        }
        else if(linearCombination < (-1 * threshold)){
            return -1;
        }
        else{
            return 0;
        }
    }
}
