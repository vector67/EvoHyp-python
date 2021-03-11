/**
 * This package implements a genetic algorithm hyper-heuristic using a
 * distributed multi-core architecture.
 * <p>
 * Nelishia Pillay
 * 10 December 2016
 */
package distrgenalg;

import initialsoln.InitialSoln;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import problemdomain.ProblemDomain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This package implements a genetic algorithm selection hyper-heuristic using a 
 * distributed multi-core architecture.
 */
public class DistrGenAlg {
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
     * selected parent. The length of the string is randomly selected between 1 and
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
     * Stores a character for each problem specific heuristic. For example, "lse",
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
     * Stores the population. Each element is an instance of type InitialSoln. This
     * instance stores the heuristic combination, fitness and initial solution.
     */
    private InitialSoln population[];
    private InitialSoln pop[][];
    /**
     * Stores the number of cores.
     */
    private int noOfCores;

    /**
     * Flag used to determine whether to print output or not.
     */

    private boolean print;
/******************************************************************************/

/******************************************************************************/
    /**
     * This is the constructor for the class.
     * @param seed The seed for the random number generator.
     * @param heuristics A string of characters representing each of the low-level
     * @param noOfCores Specifies the number of cores that the genetic algorithm
     * will be distributed over.
     */
    public DistrGenAlg(long seed, String heuristics, int noOfCores) {

        this.heuristics = heuristics;

        ranGen = new Random(seed);

        this.noOfCores = noOfCores;

        print = true;
    }
/******************************************************************************/

/**
 * Methods for setting parameter values for the genetic algorithm*
 **/

/******************************************************************************/
    /**
     * Reads the parameters from a file and stores them as data element.
     * @param parameterFile The name of the file the parameter values are stored
     * in.
     */
    public void setParameters(String parameterFile) {
        try {
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
     * @param noOfGenerations Parameter value for the number of generations.
     */
    public void setNoOfGenerations(int noOfGenerations) {
        this.noOfGenerations = noOfGenerations;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the maximum permitted length for the new substring created by mutation
     * to be inserted at a randomly selected point in a copy of the parent.The
     * length of the substring to be inserted is randomly selected to be between 1
     * and the this limit.
     * @param mutationLength Parameter value specifying the mutation length.
     */
    public void setMutationLength(int mutationLength) {
        this.mutationLength = mutationLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return The population size.
     */
    public int getPopulationSize() {
        return populationSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the population size.
     * @param populationSize Parameter value for the population size.
     */
    public void setPopulationSize(int populationSize) {

        this.populationSize = populationSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return The tournament size.
     */
    public int getTournamentSize() {
        return tournamentSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the tournament size.
     * @param tournamentSize Parameter value for the tournament size.
     */
    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the number of generations.
     */
    public int getnoOfGenerations() {
        return noOfGenerations;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the mutation rate.
     */
    public double getMutationRate() {
        return mutationRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the mutation rate.
     * @param mutationRate Parameter value for the mutation rate. The value must be
     * a fraction, e.g. 0.5.
     */
    public void setMutationRate(int mutationRate) {
        this.mutationRate = mutationRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the crossover rate.
     */
    public double getCrossoverRate() {
        return crossoverRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the crossover rate.
     * @param crossoverRate Parameter value for the crossover rate. The value must
     * be a fraction, e.g. 0.5.
     */
    public void setCrossoverRate(int crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the reproduction rate.
     */
    public double getReproductionRate() {
        return reproductionRate;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
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
     * @param initialMaxLength Parameter value specifying the maximum length
     * permitted for heuristic combinations created in the initial population.
     */
    public void setInitialMaxLength(int initialMaxLength) {
        this.initialMaxLength = initialMaxLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
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
     * @param offspringMaxLength Parameter value specifying the maximum length
     * permitted for offspring created by the mutation and crossover operators.
     */

    public void setOffspringMaxLength(int offspringMaxLength) {
        this.offspringMaxLength = offspringMaxLength;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the mutation length.
     */
    public int getNoOfCores() {
        return noOfCores;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the number of cores that the genetic algorithm hyper-heuristics will be
     * distributed over.
     * @param noOfCores Number of cores available.
     */
    public void setNoOfCores(int noOfCores) {
        this.noOfCores = noOfCores;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
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
     * @param print A value of false or true indicating whether output for each
     * generation must be printed to the screen or not.
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
     * @param heuristics The string of characters representing the low-level
     * heuristics.
     */
    public void setHeuristics(String heuristics) {
        this.heuristics = heuristics;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * This method sets the string of characters, each representing a low-level
     * heuristic for the problem domain.
     * @param problem Is an instance of <tt>ProblemDomain</tt> which defines the
     * problem domain.
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
            heuristics.charAt(ranGen.nextInt(heuristics.length()));

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
            System.out.println("Individual " + count);
            String ind;
            do {
                ind = createComb();

            } while (exists(ind, count));

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
    private InitialSoln createPop() {
        /**
         * Creates the initial population. Subpopulations are created on each core.
         */

        //Initialize the array
        pop = new InitialSoln[noOfCores][populationSize / noOfCores];

        int diff = populationSize - (populationSize / noOfCores) * noOfCores;

        if (diff > 0)
            pop[0] = new InitialSoln[pop[0].length + diff];

        //Stores the subpopulations created on each core.
        Object subPops[] = new Object[noOfCores];

        //Initialize array to store subpopulations
        try {
            ExecutorService es = Executors.newFixedThreadPool(noOfCores);

            for (int count = 1; count <= noOfCores; ++count) {
                subPops[count - 1] = es.submit(new CreatePopThread(populationSize / noOfCores, heuristics,
                    initialMaxLength, problem, ranGen));
            }//endforcount

            es.shutdown();

            InitialSoln best = null;

            for (int count = 0; count < noOfCores; ++count) {
                Future<Output> f = (Future<Output>) subPops[count];

                if (best == null)
                    best = f.get().getBest();
                else if (f.get().getBest().fitter(best) == 1)
                    best = f.get().getBest();

                pop[count] = f.get().getSubPop();

            }//endforcount

            return best;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }

    }
/******************************************************************************/

    /******************************************************************************/
    private void displayPopulation() {
        //Displays the population.
        for (int count = 0; count < population.length; ++count) {
            System.out.println(population[count].getHeuCom());
        }//endforcount
    }
/******************************************************************************/

    /******************************************************************************/
    private void displayPop() {
        //Displays the population.
        for (int count = 0; count < pop.length; ++count) {
            System.out.println("/***********************/");
            System.out.println("Subpopulation " + count);
            for (int cnt = 0; cnt < pop[count].length; ++cnt) {
                System.out.println(pop[count][cnt].getHeuCom() + " " +
                    pop[count][cnt].getFitness());
            }
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
        String p2 = parent2.toString();

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

        if (os1.length() > offspringMaxLength)
            os1 = os1.substring(0, offspringMaxLength);

        if (os2.length() > (offspringMaxLength))
            os2 = os2.substring(0, offspringMaxLength);

        InitialSoln offspring1 = evaluate(os1);
        offspring1.setHeuCom(os1);

        InitialSoln offspring2 = evaluate(os2);
        offspring2.setHeuCom(os2);

        if (offspring1.getFitness() < offspring2.getFitness())
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
        String end = com.substring(mpoint, com.length());
        tem = begin + hh + end;

        if (tem.length() > 0 && (tem.length() > offspringMaxLength))
            tem = tem.substring(0, offspringMaxLength);
        InitialSoln offspring = evaluate(tem);
        offspring.setHeuCom(tem);

        return offspring;
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln regen(InitialSoln bestInd) {

        //Stores the subpopulations created on each core.
        Object subPops[] = new Object[noOfCores];
        Selection tournSelec = new Selection(pop, ranGen, tournamentSize,
            noOfCores);

        boolean repro;
        if ((mutationRate + crossoverRate) == 1)
            repro = false;
        else
            repro = true;

        int subpop = populationSize / noOfCores;

        int popSize[] = new int[noOfCores];
        for (int count = 0; count < noOfCores; ++count) {
            popSize[count] = subpop;
        }//endfor

        int diff = populationSize - (subpop * noOfCores);
        int index = 0;

        if (diff > 0) {
            for (int count = 1; count <= diff; ++count) {
                ++popSize[index++];
            }//endfor

        }//endif

        //Determine the number of individuals to apply each genetic operator to.
  /* int minds = (int)(mutationRate*populationSize);
   int cinds = (int)(crossoverRate*populationSize);*/


        //Check for in case entire population size is not reached
  /* if((minds+cinds)!=populationSize)
   {
     if(cinds != 0)
      cinds+=populationSize -(minds+cinds);
     else if(minds!=0)
      minds+=populationSize -(minds+cinds);
   }*/

        try {
            ExecutorService es = Executors.newFixedThreadPool(noOfCores);

            for (int count = 1; count <= noOfCores; ++count) {
                int mLimit = 0, cLimit = 0, rLimit = 0;

                mLimit = (int) (popSize[count - 1] * mutationRate);
                cLimit = (int) (popSize[count - 1] * crossoverRate);

                if (repro)
                    rLimit = (int) ((1 - (crossoverRate + mutationRate)) * popSize[count - 1]);

                int tot = mLimit + cLimit + rLimit;

                if (tot < popSize[count - 1]) {
                    int cdiff = (popSize[count - 1] - tot);
                    while (cdiff > 0) {
                        if (ranGen.nextInt(3) == 0 && mLimit != 0) {
                            ++mLimit;
                            --cdiff;
                        } else if (ranGen.nextInt(3) == 1 && cLimit != 0) {
                            ++cLimit;
                            --cdiff;
                        } else if (repro) {
                            ++rLimit;
                            --cdiff;
                        }
                    }//endfor_cnt
                }//endif_tot

     
    /* if(minds==0)
     {
       mLimit=0;
       if(cinds >=subpop)
       {
         cLimit=subpop;
         cinds-=subpop;
         
         if(count==noOfCores)
         {
           cLimit+=cinds;
           cinds=0;
         }
       }
       else
       {
         cLimit=cinds;  
       }
     }
     else if(minds >= subpop)
     {
       mLimit=subpop;
       minds-=mLimit;
       cLimit=0;
     }
     else if(minds < subpop)
     {
       mLimit=minds;
       minds=0;
       
       if(cinds >0)
       {
         if(cinds > (subpop-minds))  
         {
           cLimit=subpop-mLimit;
           cinds-=cLimit;      
         }  
         else
         {
           cLimit = cinds;
           cinds=0;
         }
       }
       
     }*/

                subPops[count - 1] = es.submit(new RegenThread(bestInd, popSize[count - 1],
                    ranGen, tournSelec,
                    mutationLength, offspringMaxLength,
                    heuristics,
                    problem, mLimit, cLimit, rLimit));
            }//endforcount

            es.shutdown();

            InitialSoln best = null;

            for (int count = 0; count < noOfCores; ++count) {
                Future<Output> f = (Future<Output>) subPops[count];

                if (best == null)
                    best = f.get().getBest();
                else if (f.get().getBest().fitter(best) == 1)
                    best = f.get().getBest();

                pop[count] = f.get().getSubPop();

            }//endforcount

            return best;

        } catch (Exception e) {
            System.out.println("Problem " + e.toString());
            return bestInd;
        }
    }
/******************************************************************************/


/***Methods for genetic algorithm***/

/******************************************************************************/
    /**
     * This method implements the genetic algorithm selection hyper-heuristic.
     * @return An instance of type <tt>InitialSoln</tt> which includes the best
     * performing heuristic combination, its fitness and the solution created using
     * it.
     */
    public InitialSoln evolve() {

        if (print)
            System.out.println("Generation 0");

        InitialSoln best = createPop();


        if (print)
            System.out.println(best.getFitness() + " " + best.getHeuCom());

        for (int count = 1; count <= noOfGenerations; ++count) {
            if (print)
                System.out.println("Generation " + count);

            InitialSoln ind = regen(best);
            if (ind.fitter(best) == 1)
                best = ind;

            if (print)
                System.out.println(best.getFitness() + " " + best.getHeuCom());

        }//endfor_count

        System.out.println("Completed evolving heuristic combination.");
        return best;
    }
/******************************************************************************/
}
