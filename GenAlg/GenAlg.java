/*
 * This package implements a genetic algorithm for selection hyper-heuristics.
 * Nelishia Pillay
 * 27 August 2016
 */
package genalg;

//Import statements

import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.Random;

import initialsoln.InitialSoln;
import problemdomain.*;

/**
 * This class implements a genetic algorithm selection hyper-heuristic
 * hyper-heuristic for selecting low-level constructive or perturbative
 * heuristics for a problem domain.
 */
public class GenAlg {
/******************************************************************************/
    /*Data elements*/

    /**
     * Stores the population size.
     */
    private int populationSize;

    /**
     * Stores the tournament size.
     */
    private int tournamentSize;

    /**
     * Stores the number of generations.
     */

    private int noOfGenerations;

    /**
     * Stores the mutation application rate.
     */

    private double mutationRate;

    /**
     * Stores the crossover application rate.
     */
    private double crossoverRate;

    /**
     * Stores the reproduction application rate.
     */
    private double reproductionRate;

    /**
     * Stores the length of the string created by mutation to insert into the
     * selected parent.The length of the string is randomly selected between 1 and t
     * the this limit.
     */
    private int mutationLength;

    /**
     * Stores the maximum length for the chromosomes create in the initial
     * population.
     */
    private int initialMaxLength;

    /**
     * Stores the maximum length or the chromosomes produced by the genetic
     * operators.
     */
    private int offspringMaxLength;

    /**
     * Stores a Boolean value indicating whether duplicates should be allowed in the
     * initial population or not.The default value is false.
     */
    private boolean allowDuplicates;

    /**
     * Stores a character for each problem specific heuristic.For example, "lse",
     * in which case there are three problem specific heuristics,represented by
     * "l", "s" and "e" respectively.
     */
    private String heuristics;

    /**
     * This variable stores a problem domain instance.
     */

    private ProblemDomain problem;

    /**
     * Stores and instances of the random number generator to be used by the genetic
     * algorithm.
     */
    private Random ranGen;

    /**
     * Stores the population.Each element is an instance of type InitialSoln.This
     * instance stores the heuristic combination, fitness and initial solution.
     */
    private InitialSoln population[];

    /**
     * Is a flag used to determine whether output for each generation must be
     * printed to the screen or not.If it is set to <tt>true</tt> output is
     * printed.If it is set to false output is not printed.The default is
     * <tt>true</tt>.
     */
    private boolean print;

/******************************************************************************/

/******************************************************************************/
    /**
     * This is the constructor for the class.
     *
     * @param seed       The seed for the random number generator.
     * @param heuristics A string of characters representing each of the low-level
     *                   heuristics for the problem domain.
     */
    public GenAlg(long seed, String heuristics) {

        this.heuristics = heuristics;

        System.out.println("Seed " + seed);

        //Initializes the random number generator.
        ranGen = new Random(seed);

        //Set the flag for printing output for each generation to true.

        print = true;

    }
/******************************************************************************/

/***Methods for setting parameter values for the genetic algorithm***/

/******************************************************************************/
    /**
     * Reads the parameters from a file and stores them as data element.
     *
     * @param parameterFile The name of the file the parameter values are stored
     *                      in.
     */
    public void setParameters(String parameterFile) {
        try {
            //Initialise input stream to read from a file
            File f = new File(parameterFile);
            FileInputStream f1 = new FileInputStream(f);
            BufferedReader df = new BufferedReader(new InputStreamReader(f1));

            populationSize = (new Double(df.readLine())).intValue();
            tournamentSize = (new Double(df.readLine())).intValue();
            noOfGenerations = (new Double(df.readLine())).intValue();
            mutationRate = (new Double(df.readLine())).doubleValue();
            crossoverRate = (new Double(df.readLine())).doubleValue();
            initialMaxLength = (new Double(df.readLine())).intValue();
            offspringMaxLength = (new Double(df.readLine())).intValue();
            mutationLength = (new Double(df.readLine())).intValue();
        } catch (IOException ioe) {
            System.out.println("The file " + parameterFile + " cannot be found. "
                + "Please check the details provided.");

        }
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the number of generations size.
     *
     * @param noOfGenerations Parameter value for the number of generations.
     */
    public void setNoOfGenerations(int noOfGenerations) {
        //Sets the number of generations.

        this.noOfGenerations = noOfGenerations;

    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return The population size.
     */
    public int getPopulationSize() {
        return populationSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the population size.
     *
     * @param populationSize Parameter value for the population size.
     */
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return The tournament size.
     */
    public int getTournamentSize() {
        return tournamentSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the tournament size.
     *
     * @param tournamentSize Parameter value for the tournament size.
     */
    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the number of generations.
     */
    public int getnoOfGenerations() {
        return noOfGenerations;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the mutation rate.
     */
    public double getMutationRate() {
        return mutationRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the mutation rate.
     *
     * @param mutationRate Parameter value for the mutation rate. The value must be
     *                     a fraction, e.g. 0.5.
     */
    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the crossover rate.
     */
    public double getCrossoverRate() {
        return crossoverRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the crossover rate.
     *
     * @param crossoverRate Parameter value for the crossover rate. The value must
     *                      be a fraction, e.g. 0.5.
     */
    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the reproduction rate.
     */
    public double getReproductionRate() {
        return reproductionRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the initial maximum length permitted for heuristic
     * combinations created in the initial population.
     */
    public int getInitialMaxLength() {
        return initialMaxLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the maximum length of chromosome in the initial population.
     *
     * @param initialMaxLength Parameter value specifying the maximum length
     *                         permitted for heuristic combinations created in the initial population.
     */
    public void setInitialMaxLength(int initialMaxLength) {
        this.initialMaxLength = initialMaxLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the maximum offspring length.
     */
    public int getOffspringMaxLength() {
        return offspringMaxLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the maximum length of the offspring produced by the genetic operators.If
     * the offspring size exceeds this length the substring equal to this value is
     * returned.
     *
     * @param offspringMaxLength Parameter value specifying the maximum length
     *                           permitted for offspring created by the mutation and crossover operators.
     */
    public void setOffspringMaxLength(int offspringMaxLength) {
        this.offspringMaxLength = offspringMaxLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the mutation length.
     */
    public int getMutationLength() {
        return mutationLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the maximum permitted length for the new substring created by mutation
     * to be inserted at a randomly selected point in a copy of the parent.The
     * length of the substring to be inserted is randomly selected to be between 1
     * and the this limit.
     *
     * @param mutationLength Parameter value specifying the mutation length.
     */
    public void setMutationLength(int mutationLength) {
        this.mutationLength = mutationLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the value of the Boolean flag that is used to specify if
     * duplicates are allowed or not.
     */

    public boolean getAllowDuplicates() {
        return allowDuplicates;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * This method sets the Boolean flag indicating whether duplicates are allowed
     * in the initial population of not.
     *
     * @param allowDuplicates A value of true or false which indicates whether
     *                        duplicates are allowed in the initial population or not.
     */
    public void setAllowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the value of the flag print used to determine whether to
     * print output to the screen.
     */
    public boolean getPrint() {
        return print;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the flag for printing output for each generation to the screen.If it is
     * set to <tt>true</tt> output is printed.If it is set to <tt>false</tt> output
     * is not printed.The default is <tt>true</tt>.The output printed to the screen
     * is the best heuristic combination for each generation and its fitness as well
     * as the best fitness obtained thus far in the run.
     *
     * @param print A value of false or true indicating whether output for each
     *              generation must be printed to the screen or not.
     */
    public void setPrint(boolean print) {
        this.print = print;
    }
/******************************************************************************/

/***Methods for setting problem specific information***/

/******************************************************************************/

    /**
     * This method sets the string of characters, each representing a low-level
     * heuristic for the problem domain.
     *
     * @param heuristics The string of characters representing the low-level
     *                   heuristics.
     */
    public void setHeuristics(String heuristics) {
        this.heuristics = heuristics;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * This method sets the string of characters, each representing a low-level
     * heuristic for the problem domain.
     *
     * @param problem Is an instance of <tt>ProblemDomain</tt> which defines the
     *                problem domain.
     */
    public void setProblem(ProblemDomain problem) {
        this.problem = problem;
    }

/******************************************************************************/

/***Methods for creating the initial population***/

    /******************************************************************************/
    private boolean exists(String heuComb, int pos) {
        //Checks whether the Comb already exists in the population.

        for (int count = 0; count < pos; ++count) {
            if (heuComb.compareTo(population[count].getHeuCom()) == 0)
                return true;
        }//EndForCount

        return false;
    }
/******************************************************************************/

    /******************************************************************************/
    private String createComb() {
        //This method creates the heuristic combination to construct or perturb a
        //solution.

        String heuComb = new String();

        int length = ranGen.nextInt(initialMaxLength) + 1;

        for (int count = 1; count <= length; ++count) {
            heuComb += heuristics.charAt(ranGen.nextInt(heuristics.length()));

        }//EndForCount

        return heuComb;
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln createPopulation() {
        //Creates the initial population. Duplicates are not allowed.

        InitialSoln best = null;
        population = new InitialSoln[populationSize];
        for (int count = 0; count < populationSize; ++count) {
            String ind;

            if (!allowDuplicates) {
                do {
                    ind = createComb();

                } while (exists(ind, count));
            } else
                ind = createComb();

            population[count] = evaluate(ind);
            population[count].setHeuCom(ind);

            if (count == 0)
                best = population[count];
            else if (population[count].fitter(best) == 1)
                best = population[count];


            ind = null;
        }//EndForCount*/

        return best;

    }
/******************************************************************************/

    /******************************************************************************/
    private void displayPopulation() {
        //Displays the population.
        System.out.println("Population");
        for (int count = 0; count < population.length; ++count) {
            System.out.println(population[count].getHeuCom() + " " + population[count].getFitness());
        }//endforcount
    }
    /******************************************************************************/

/***Methods for evaluation and selection***/

    /******************************************************************************/
    private InitialSoln evaluate(String ind) {
        return problem.evaluate(ind);
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln selection() {
        /*Implements the tournament selection method. */

        //Select a parent
        InitialSoln winner = population[ranGen.nextInt(populationSize)];
        for (int count = 2; count <= tournamentSize; ++count) {
            InitialSoln current = population[ranGen.nextInt(populationSize)];
            if (current.fitter(winner) == 1)
                winner = current;
        }//endfor_count
        return winner;
    }
/******************************************************************************/

/***Methods for genetic operators***/

    /******************************************************************************/
    private InitialSoln crossover(InitialSoln parent1, InitialSoln parent2) {
        //Performs the crossover operator

        String p1 = parent1.getHeuCom();
        String p2 = parent2.getHeuCom();

        //Choose crossover points
        int point1 = ranGen.nextInt(p1.length());
        int point2 = /*point1;*/ranGen.nextInt(p2.length());

        //Store fragments
        String frag11 = p1.substring(0, point1);
        String frag12 = p1.substring(point1, p1.length());
        String frag21 = p2.substring(0, point2);
        String frag22 = p2.substring(point2, p2.length());

        //Combine fragments to form offspring
        String os1 = new String(frag11 + frag22);
        String os2 = new String(frag21 + frag12);

        if (offspringMaxLength > 0 && os1.length() > offspringMaxLength)
            os1 = os1.substring(0, offspringMaxLength);

        if (offspringMaxLength > 0 && os2.length() > (offspringMaxLength))
            os2 = os2.substring(0, offspringMaxLength);

        InitialSoln offspring1 = evaluate(os1);
        offspring1.setHeuCom(os1);

        InitialSoln offspring2 = evaluate(os2);
        offspring2.setHeuCom(os2);

        if (offspring1.fitter(offspring2) == 1)
            return offspring1;
        else
            return offspring2;

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

    /******************************************************************************/
    private InitialSoln mutation(InitialSoln parent) {
        /*Performs the mutation operator. */

        String com = parent.getHeuCom(); //Get the combination
        int mpoint = ranGen.nextInt(com.length()); //Randomly select the mutation point
        int len = ranGen.nextInt(mutationLength) + 1;
        String tem;
        String hh = createStr(len);

        String begin = com.substring(0, mpoint);
        String end = com.substring(mpoint + 1, com.length());
        tem = begin + hh + end;

        if (offspringMaxLength > 0 && tem.length() > offspringMaxLength)
            tem = tem.substring(0, offspringMaxLength);

        InitialSoln offspring = evaluate(tem);
        offspring.setHeuCom(tem);

        return offspring;
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln regenerate(InitialSoln bestInd) {
        /*Creates the population of successive generations. */

        //Determine the number of individuals to apply each genetic operator to.
        int minds = (int) (mutationRate * populationSize);
        int cinds = (int) (crossoverRate * populationSize);
        if ((mutationRate + crossoverRate) < 1)
            reproductionRate = 1 - (mutationRate + crossoverRate);

        int rinds = (int) (reproductionRate * populationSize);

        //Check in case entire population size is not reached
        if ((minds + cinds + rinds) != population.length) {
            if (cinds != 0)
                cinds += population.length - (minds + cinds + rinds);
            else if (minds != 0)
                minds += population.length - (minds + cinds + rinds);
        }

        InitialSoln best = bestInd;
        int index = 0;
        InitialSoln newPop[] = new InitialSoln[populationSize];

        for (int rcount = 1; rcount <= rinds; ++rcount) {
            //System.out.println("Individual "+index);
            newPop[index++] = selection();

            if (newPop[index - 1].fitter(best) == 1)
                best = newPop[index - 1];

        }//endfor_rcount

        for (int mcount = 1; mcount <= minds; ++mcount) {
            //System.out.println("Individual "+index);
            newPop[index++] = mutation(selection());

            if (newPop[index - 1].fitter(best) == 1)
                best = newPop[index - 1];

        }//endfor_mcount

        for (int ccount = 1; ccount <= cinds; ++ccount) { //System.out.println("Individual "+index);
            newPop[index++] = crossover(selection(), selection());

            if (newPop[index - 1].fitter(best) == 1)
                best = newPop[index - 1];
        }//endfor_ccount

        population = newPop;
        newPop = null;

        return best;
    }
/******************************************************************************/

/***Methods for genetic algorithm***/

/******************************************************************************/
    /**
     * This method implements the genetic algorithm selection hyper-heuristic.
     *
     * @return An instance of type <tt>InitialSoln</tt> which includes the best
     * performing heuristic combination, its fitness and the solution created using
     * it.
     */
    public InitialSoln evolve() {

        if (print)
            System.out.println("Generation 0");
        InitialSoln best = createPopulation();
        if (print) {
            System.out.println("Best Fitness: " + best.getFitness());
            System.out.println("Heuristic Combination: " + best.getHeuCom());
            System.out.println();
        }

        for (int count = 1; count <= noOfGenerations; ++count) {
            if (print)
                System.out.println("Generation " + count);

            InitialSoln ind = regenerate(best);

            if (ind.fitter(best) == 1)
                best = ind;
            // displayPopulation();
            if (print) {
                System.out.println("Generation Best Fitness: " + ind.getFitness());
                System.out.println("Generation Best Heuristic Combination: " + ind.getHeuCom());
                System.out.println("Overall Best Fitness: " + best.getFitness());
                System.out.println("Overall Best Heuristic Combination: " + best.getHeuCom());
                System.out.println();
            }

        }//endfor_count
        System.out.println("Completed evolving heuristic combination");

        return best;
    }
/******************************************************************************/
}

