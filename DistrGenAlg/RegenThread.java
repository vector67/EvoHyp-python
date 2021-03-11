package distrgenalg;


import distrgenalg.Output;
import distrgenalg.Selection;
import initialsoln.InitialSoln;

import java.util.Random;
import java.util.concurrent.Callable;

import problemdomain.ProblemDomain;

/**
 * Thread for creating the new population using mutation.
 * <p>
 * Nelishia Pillay
 * 18 December 2016
 */

/**
 * Implements the thread for regeneration to create successive generations 
 * using mutation and crossover.
 */
public class RegenThread implements Callable {
    /******************************************************************************/
//Data elements
    private InitialSoln bestInd;  //Stores the best individual passed to it
    private int popSize; //Stores the population size
    private Random ranGen; //Stores the random number generator
    private Selection tournSelec; //Selection method
    private int mutationLength; //Stores the mutation length
    private int offspringMaxLength;//Stores the offspring maximum length
    private String heuristics; //Stores the heuristics
    private ProblemDomain problem;
    private int mLimit; //Stores the number of mutations to be performed.
    private int cLimit; //Stores the number of crossovers to be performed.
    private int rLimit; //Stores the number of reproductions to be performed.
/******************************************************************************/

/******************************************************************************/
    /**
     * Constructor for the thread which takes in parameter values for the process
     * of regeneration.
     * @param bestInd The best individual found during initial population generation.
     * @param popSize The size of the subpopulation to be created.
     * @param ranGen Random number generator.
     * @param tournSelec Tournament selection instance.
     * @param mutationLength Maximum length of the substring created by mutation.
     * @param offspringMaxLength Maximum permitted offspring length.
     * @param heuristics A string composed of characters representing the heuristics
     * for the problem.
     * @param problem An instance of the problem domain.
     * @param mLimit The number of offspring to be created using mutation.
     * @param cLimit The number of offspring to be created using crossover.
     */
    public RegenThread(InitialSoln bestInd, int popSize, Random ranGen,
                       Selection tournSelec, int mutationLength, int offspringMaxLength,
                       String heuristics, ProblemDomain problem, int mLimit, int cLimit,
                       int rLimit) {
        this.bestInd = bestInd;
        this.popSize = popSize;
        this.ranGen = ranGen;
        this.tournSelec = tournSelec;
        this.mutationLength = mutationLength;
        this.offspringMaxLength = offspringMaxLength;
        this.heuristics = heuristics;
        this.problem = problem;
        this.mLimit = mLimit;
        this.cLimit = cLimit;
        this.rLimit = rLimit;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * Method call for the thread for regeneration.
     */
    public Output call() {
        InitialSoln best = bestInd;
        InitialSoln subPop[] = new InitialSoln[popSize];
        int index = 0;

        for (int count = 1; count <= mLimit; ++count) {

            subPop[index++] = mutation(tournSelec.Selection());

            if (subPop[index - 1].fitter(best) == 1)
                best = subPop[index - 1];
        }//endfor

        for (int count = 1; count <= cLimit; ++count) {
            subPop[index++] = crossover(tournSelec.Selection(), tournSelec.Selection());
            if (subPop[index - 1].fitter(best) == 1)
                best = subPop[index - 1];
        }//endfor

        for (int count = 1; count <= rLimit; ++count) {

            subPop[index++] = tournSelec.Selection();

            if (subPop[index - 1].fitter(best) == 1)
                best = subPop[index - 1];
        }//endfor

        Output output = new Output();
        output.setBest(best);
        output.setSubPop(subPop);

        return output;
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln crossover(InitialSoln parent1, InitialSoln parent2) {
        //Performs the crossover operator

        String p1 = parent1.getHeuCom();
        String p2 = parent2.getHeuCom();

        //Choose crossover points
        int point1 = ranGen.nextInt(p1.length());
        int point2 = ranGen.nextInt(p2.length());

        //Store fragments
        String frag11 = p1.substring(0, point1);
        String frag12 = p1.substring(point1, p1.length());
        String frag21 = p2.substring(0, point2);
        String frag22 = p2.substring(point2, p2.length());

        //Combine fragments to form offspring
        String os1 = new String(frag11 + frag22);
        String os2 = new String(frag21 + frag12);

        if (offspringMaxLength > 0) {
            if (os1.length() > offspringMaxLength)
                os1 = os1.substring(0, offspringMaxLength);

            if (os2.length() > (offspringMaxLength))
                os2 = os2.substring(0, offspringMaxLength);
        }

        InitialSoln offspring1 = problem.evaluate(os1);
        offspring1.setHeuCom(os1);

        InitialSoln offspring2 = problem.evaluate(os2);
        offspring2.setHeuCom(os2);

        if (offspring1.getFitness() < offspring2.getFitness())
            return offspring1;
        else
            return offspring2;
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln mutation(InitialSoln parent) {
        /*Performs the mutation operator. */

        String com = parent.getHeuCom(); //Get the combination
        System.out.println("/**************************");
        System.out.println("Entered");
        int mpoint = ranGen.nextInt(com.length()); //Randomly select the mutation point
        int len = ranGen.nextInt(mutationLength) + 1;
        String tem;
        System.out.println("Parent: " + com);
        System.out.println("Point: " + mpoint);
        String hh = createStr(len);
        System.out.println("HH: " + hh);
        String begin = com.substring(0, mpoint);
        String end = com.substring(mpoint + 1, com.length());
        tem = new String(begin + hh + end);
        System.out.println("EOffspring: " + tem);
        if (offspringMaxLength > 0 && (tem.length() > 0) && tem.length() > offspringMaxLength) {
            tem = tem.substring(0, offspringMaxLength);
        }
        InitialSoln offspring = problem.evaluate(tem);
        offspring.setHeuCom(tem);
        System.out.println("EOffspring: " + tem);
        System.out.println(offspring.getHeuCom());
        System.out.println("/**************************");
        System.out.println();
        return offspring;
    }
/******************************************************************************/

    /******************************************************************************/
    private String createStr(int limit) {
        //Creates a substring for mutation.

        String str = new String();

        for (int count = 0; count < limit; ++count) {
            str += heuristics.charAt(ranGen.nextInt(heuristics.length()));

        }//EndForCount

        return str;
    }
/******************************************************************************/
}
