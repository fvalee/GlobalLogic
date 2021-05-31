package hr.globallogic;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        // String representation of a list of characters that the algorithm is going to avoid
        // when calculating frequency of wanted characters
        String specialCharacters = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        // String representation of a list of characters that the algorithm is going to look for
        String allowedCharacters = "LOGIC";

        FrequencyAlgorithm algorithm = new FrequencyAlgorithm(specialCharacters, allowedCharacters);

        // If you provided two command line arguments, the first argument needs to be the path to the file
        // containing the phrase and the second argument the path to the file where the algorithm will dump
        // the output of the executed algorithm. In case the two arguments were not provided, you will have to
        // manually provide the phrase inside the command line after which you will be presented with the output.
        if (args.length == 2) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(args[0]));
                FileWriter writer = new FileWriter(args[1]);
                String phrase = reader.readLine();
                writer.write(algorithm.Execute(phrase));
                writer.close();
            }
            catch (IOException e) {
                System.out.println("Oh, no! An error has occurred.");
                e.printStackTrace();
            }
        }
        else {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String phrase = reader.readLine();
                System.out.println(algorithm.Execute(phrase));
            }
            catch (IOException e) {
                System.out.println("Oh, no! An error has occurred.");
                e.printStackTrace();
            }
        }
    }
}
