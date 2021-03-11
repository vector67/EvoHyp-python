/**
 * Thread for creating part of the initial population.
 * <p>
 * Nelishia Pillay
 * 10 December 2016
 */
package distrgenalg;

//Import statements

import initialsoln.InitialSoln;

import java.util.Random;

import problemdomain.ProblemDomain;

import java.util.concurrent.Callable;

/**
 * Implements the thread for initial population generation.
 */
public class CreatePopThread implements Callable {
/******************************************************************************/
    /*Data elements*/

    /**
     * This variable stores the size of the subpopulation.
     */
    private int popSize;

    /**
     * Stores the subpopulation. Each element is an instance of type InitialSoln.
     * This instance stores the heuristic combination, fitness and initial solution.
     */
    private InitialSoln subPop[];

    /**
     * This variable stores a problem domain instance.
     */
    private ProblemDomain problem;

    /**
     * Stores the maximum length for the chromosomes create in the initial
     * population.
     */
    private int initialMaxLength;

    /**
     * Stores a character for each problem specific heuristic. For example, "lse",
     * in which case there are three problem specific heuristics,represented by
     * "l", "s" and "e" respectively.
     */
    private String heuristics;

    /**
     * Stores and instances of the random number generator to be used by the genetic
     * algorithm.
     */
    private Random ranGen;

/******************************************************************************/

/******************************************************************************/
    /**
     * Constructor for the thread for initial population generation.
     *
     * @param popSize          Specifies the population size.
     * @param heuristics       String comprised of characters representing each of the
     *                         heuristics.
     * @param initialMaxLength Specifies the maximum length permitted for heuristic
     *                         combinations created in the initial population.
     * @param problem          Specifies the problem domain.
     * @param ranGen           Random number generator.
     */
    public CreatePopThread(int popSize, String heuristics, int initialMaxLength,
                           ProblemDomain problem, Random ranGen) {
        //Constructor

        this.popSize = popSize;
        this.heuristics = heuristics;
        this.initialMaxLength = initialMaxLength;
        this.problem = problem;
        this.ranGen = ranGen;
    }
/******************************************************************************/

    /******************************************************************************/
    private String createComb() {
        //This method creates the heuristic combination to construct or perturb a
        //solution.

        String heuComb = new String();

        int length = ranGen.nextInt(initialMaxLength) + 1;

        for (int count = 1; count <= length; ++count) {
            int index = ranGen.nextInt(heuristics.length());
            heuComb += heuristics.substring(index, index + 1);

        }//EndForCount


        return heuComb;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * The call method for the thread for initial population generation.
     */
    public Output call() {
        //Creates the initial population. Duplicates are not allowed.

        InitialSoln best = null;
        subPop = new InitialSoln[popSize];
        for (int count = 0; count < popSize; ++count) {
            String ind;
            do {
                ind = createComb();
            } while (exists(ind, count));

            subPop[count] = evaluate(ind);
            subPop[count].setHeuCom(ind);

            if (count == 0)
                best = subPop[count];
            else if (subPop[count].fitter(best) == 1)
                best = subPop[count];

            ind = null;
        }//EndForCount*/

        Output output = new Output();
        output.setBest(best);
        output.setSubPop(subPop);

        return output;

    }
/******************************************************************************/

    /******************************************************************************/
    private void displayPopulation() {
        //Displays the population.
        for (int count = 0; count < subPop.length; ++count) {
            System.out.println(subPop[count].getHeuCom() + " " + subPop[count].getFitness());
        }//endforcount
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln evaluate(String ind) {
        return problem.evaluate(ind);
    }
/******************************************************************************/

    /******************************************************************************/
    private boolean exists(String heuComb, int pos) {
        //Checks whether the Comb already exists in the population.

        for (int count = 0; count < pos; ++count) {
            if (heuComb.compareTo(subPop[count].getHeuCom()) == 0)
                return true;
        }//EndForCount

        return false;
    }
/******************************************************************************/

}
