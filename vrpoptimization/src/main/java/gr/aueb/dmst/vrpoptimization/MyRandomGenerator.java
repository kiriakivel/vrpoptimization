package gr.aueb.dmst.vrpoptimization;

import java.util.ArrayList;

/**
 *
 * @author mzaxa
 */
public class MyRandomGenerator {

    ArrayList <Double> randomNumbers;
    int numberOfColours;
    int lastIndexUsed = 0;

    double epsilon = 0.000001;

    int firstSwapOptions;
    int secondSwapOptions;

    public MyRandomGenerator(int totalNumberOfColours)
    {
        randomNumbers = new ArrayList();

        randomNumbers.add(0.406);
        randomNumbers.add(0.152);
        randomNumbers.add(0.800);
        randomNumbers.add(0.231);
        randomNumbers.add(0.177);
        randomNumbers.add(0.730);
        randomNumbers.add(0.166);
        randomNumbers.add(0.113);
        randomNumbers.add(0.759);
        randomNumbers.add(0.751);
        randomNumbers.add(0.924);
        randomNumbers.add(0.355);
        randomNumbers.add(0.352);
        randomNumbers.add(0.338);
        randomNumbers.add(0.024);
        randomNumbers.add(0.835);

        randomNumbers.add(0.458);
        randomNumbers.add(0.543);
        randomNumbers.add(0.217);
        randomNumbers.add(0.960);
        randomNumbers.add(0.567);
        randomNumbers.add(0.434);
        randomNumbers.add(0.222);
        randomNumbers.add(0.111);


        numberOfColours = totalNumberOfColours;

        firstSwapOptions = numberOfColours;
        secondSwapOptions = firstSwapOptions - 1;
    }



    int positionForFirstSwapped()
    {
        double randomNumber = randomNumbers.get(lastIndexUsed);
        lastIndexUsed++;

        double denominator = 1.0 / firstSwapOptions;

        double result = randomNumber / denominator - epsilon;

        int finalResult = (int) result;
        
        //System.out.print(finalResult + "random"+ firstSwapOptions);
        return finalResult;
    }

    int positionForSecondSwapped(int positionOfFirst)
    {
        double randomNumber = randomNumbers.get(lastIndexUsed);
        lastIndexUsed++;

        double denominator = 1.0 / secondSwapOptions;

        double result = randomNumber / denominator - epsilon;

        int finalResult = (int) result;

        if (finalResult < positionOfFirst )
        {
            return finalResult;
        }
        else
        {
            return finalResult + 1;
        }
    }

    double probabilityForMoveAcceptance()
    {
        double randomNumber = randomNumbers.get(lastIndexUsed);
        lastIndexUsed++;
        return randomNumber;
    }
}
