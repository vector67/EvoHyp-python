/**
 * This class implements a genetic programming generation constructive
 * hyper-heuristic for generating new low-level construction heuristics for a
 * problem domain. The running of the algorithm is distributed over a multicore
 * architecture.
 * <p>
 * Nelishia Pillay
 * 16 January 2017
 */
package distrgenprog;

//Import statements

import solution.*;
import problem.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class implements a genetic programming generation constructive 
 * hyper-heuristic for generating new low-level construction heuristics for a 
 * problem domain.
 */
public class DistrGenProg {
/******************************************************************************/
//Data elements

    /**
     *Stores a string of characters with each character representing a problem
     *attribute that will form elements of the terminal set.
     */

    private String attributes;

    /**Stores the operators.These include the arithmetic operators, "if" operator and
     *arithmetic logical operators:+, -, * and /. The / is protected division which
     *returns a value of 1 if the denominator is 0. The if operator performs the
     *function of the standard if-then-else operator and takes 3 arguments.
     *Relational operators first argument of the "if" operator: <, >, ==, //!=, >=,
     *<=.
     */
    private String opts[];

    /**
     * Stores the problem domain.
     */
    private Problem problem;
    /**
     * Stores the arithmetic operators only.
     */
    private String arithOpts[];

    /**
     * Stores the relational operators only.
     */
    private String relOpts[];

    /**
     * Stores the terminals. Each character in the string "attributes" is stored as
     * a terminal.
     */
    private String terms[]; //Stores the terminals

    /**
     * Stores the maximum depth of the trees created during initial population
     * generation.
     */
    private int maxDepth;

    /**
     * Stores the maximum depth of the offspring created by the genetic operators.
     */
    private int offspringDepth;

    /**
     * Stores the depth of the subtree created by the mutation operator.
     */
    private int mutationDepth;

    /**
     * Stores the population size.
     */
    private int populationSize;
    private Solution pop[][];
    /**
     * Stores the number generations.
     */
    private int noOfGenerations;

    /**
     * Stores the reproduction application rate.
     */
    private double reproductionRate;

    /**
     * Stores the mutation application rate.
     */
    private double mutationRate;

    /**
     * Stores the crossover application rate.
     */
    private double crossoverRate;

    /**
     * Stores the tournament size.
     */
    private int tournamentSize; //Stores the tournament size

    /**
     * Stores the population of heuristics.
     */
    private Solution population[];

    /**
     * Stores the seed for the random number generator.
     */
    private long seed;

    /**
     * Stores an instance of the random number generator.
     */
    private Random ranGen;

    /**
     * A temporary pointer used by the mutation operator.
     */
    private int curPoint;

    /**
     * A temporary pointer used by the crossover operator.
     */
    private int crPoint;

    /**
     * Temporary node instance used by the crossover operator.
     */
    private Node crChild;

    /**
     * Temporary position variable used by the crossover operator.
     */
    private int crPos;

    /**
     * Stores an integer value indicating whether an arithmetic function or rule
     * should be evolved. A value of 0 indicates and arithmetic function a value of
     * 1 indicates a rule.
     */
    private int heuType;

    /**
     * Stores the number of cores.
     */
    private int noOfCores;

    //Is a flag used to determine whether output for each generation must be printed
//to the screen or not. If it is set to true output is printed. If it is set to
//false output is not printed. The default is true;
    private boolean print;

/******************************************************************************/

/******************************************************************************/
    /**
     * Constructor to set the random number generator seed, attributes and type of
     * heuristic.The random number generator is initialized in the constructor.
     * @param seed A long value specifying the seed to be used for the random number
     * generator.
     * @param attributes A string composed of characters, each representing an
     * attribute for the problem.
     * @param heuType An integer value indicating whether an arithmetic function or
     * rule should be evolved. A value of 0 indicates and arithmetic function a value
     * of 1 indicates a rule.
     * @param noOfCores Specifies the number of available cores that implementation of the
     * genetic programming hyper-heuristic will be distributed over.
     */
    public DistrGenProg(long seed, String attributes, int heuType, int noOfCores) {
        this.seed = seed;

        ranGen = new Random(seed);

        this.attributes = attributes;

        this.heuType = heuType;

        this.noOfCores = noOfCores;

        print = true;

        setOpts();
        setTerms();
    }
/******************************************************************************/
/***Methods for setting parameter values for the genetic algorithm***/

/******************************************************************************/
    /**
     * Reads the parameters from a file and stores them as data element.
     * @param parameterFile The name of the file the parameter values are stored
     * in.
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
            maxDepth = (new Double(df.readLine())).intValue();
            offspringDepth = (new Double(df.readLine())).intValue();
            mutationDepth = (new Double(df.readLine())).intValue();

        } catch (IOException ioe) {
            System.out.println("The file " + parameterFile + " cannot be found. "
                + "Please check the details provided.");

        }
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * This method sets the string of characters, each representing a low-level
     * heuristic for the problem domain.
     * @param problem Is an instance of <tt>Problem</tt> which defines the
     * problem domain.
     */
    public void setProblem(Problem problem) {
        this.problem = problem;
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
     * Sets the maximum permitted depth for the new subtree created by mutation
     * to be inserted at a randomly selected point in a copy of the parent.
     * @param mutationDepth Parameter value specifying the maximum depth of the
     * subtree created by mutation.
     */
    public void setmutationDepth(int mutationDepth) {
        this.mutationDepth = mutationDepth;
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
    public void setMutationRate(double mutationRate) {
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
    public void setCrossoverRate(double crossoverRate) {
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
     * @return Returns the  maximum depth permitted for parse trees representing
     * heuristics created in the initial population.
     */
    public int getMaxDepth() {
        return maxDepth;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the maximum depth of a parse tree in the initial population.
     * @param maxDepth Parameter value specifying the maximum depth
     * permitted for heuristics created in the initial population.
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the maximum offspring depth.
     */
    public int getOffspringDepth() {
        return offspringDepth;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the maximum depth of the offspring produced by the genetic operators.If
     * the offspring size exceeds this depth the function nodes at the maximum depth
     * are replaced by randomly selected terminals.
     * @param offspringDepth Parameter value specifying the maximum depth
     * permitted for offspring created by the mutation and crossover operators.
     */
    public void setOffspringDepth(int offspringDepth) {
        this.offspringDepth = offspringDepth;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     *
     * @return Returns the mutation depth.
     */
    public int getMutationDepth() {
        return mutationDepth;
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

    /******************************************************************************/
    private void setOpts() {
        /**
         * Sets the three function arrays opts, arithOpts and relOpts.
         */

        //Store all the operators.
        if (heuType == 1)
            opts = new String[5];
        else
            opts = new String[4];

        opts[0] = new String("+");
        opts[1] = new String("-");
        opts[2] = new String("*");
        opts[3] = new String("/");

        if (heuType == 1)
            opts[4] = new String("if");

        //Stores the arithmetic operators.
        arithOpts = new String[4];
        arithOpts[0] = new String("+");
        arithOpts[1] = new String("-");
        arithOpts[2] = new String("*");
        arithOpts[3] = new String("/");

        //Stores the relationaloperators.
        relOpts = new String[6];
        relOpts[0] = new String("<");
        relOpts[1] = new String(">");
        relOpts[2] = new String("==");
        relOpts[3] = new String("!=");
        relOpts[4] = new String("<=");
        relOpts[5] = new String(">=");
    }
/******************************************************************************/

    /******************************************************************************/
    private void setTerms() {
        /**
         * Stores the terminals from the attributes string. Each character in
         * "attributes" is an element of the terminal set. */

        //Initialize array.
        terms = new String[attributes.length()];

        //Store attributes as terminals.
        for (int count = 0; count < attributes.length(); ++count) {
            terms[count] = new String(attributes.substring(count, count + 1));

        }//EndForCount

    }
/******************************************************************************/

    /******************************************************************************/
    private void displayFunctionSet() {
        //Displays the function set.

        for (int count = 0; count < opts.length; ++count) {
            System.out.print(opts[count] + " ");

        }//EndForCount
    }
/******************************************************************************/

    /******************************************************************************/
    private void displayTerminalSet() {
        //Displays the function set.

        for (int count = 0; count < terms.length; ++count) {
            System.out.print(terms[count] + " ");

        }//EndForCount
    }
/******************************************************************************/

    /******************************************************************************/
    private void prune(Node root, int currDepth) {
        if (currDepth == offspringDepth ||
            (root.getLabel().compareTo("if") == 0 && currDepth == (offspringDepth - 1))) {
            if (root.getArity() > 0) {
                root.setLabel(terms[ranGen.nextInt(terms.length)]);
                root.setArity(0);
                root.clearChildren();
            }
        } else {
            ++currDepth;
            for (int count = 0; count < root.getArity(); ++count) {
                prune(root.getChild(count), currDepth);
                //root.addChild(child, count);
            }
        }
        //return root;
    }
/******************************************************************************/

    /******************************************************************************/
    private void printInd(Node root) {
        System.out.print(root.getLabel());

        for (int count = 0; count < root.getArity(); ++count) {
            //if(root.get_child(count)!=null)
            printInd(root.getChild(count));

        }//endfor_count
    }
/******************************************************************************/

    /******************************************************************************/
    private void displayPop() {
        for (int count = 0; count < population.length; ++count) {
            ((Node) population[count].getHeuristic()).prefix();
            System.out.print(" " + population[count].getFitness());
            System.out.println();
        }//endfor
    }
/******************************************************************************/

    /******************************************************************************/
    private Solution creatPop() {
        /**
         * Creates the initial population. Subpopulations are created on each core.
         */

        //Initialize the array
        pop = new Solution[noOfCores][populationSize / noOfCores];

        int diff = populationSize - (populationSize / noOfCores) * noOfCores;

        if (diff > 0)
            pop[0] = new Solution[pop[0].length + diff];

        //Stores the subpopulations created on each core.
        Object subPops[] = new Object[noOfCores];

        //Initialize array to store subpopulations
        try {
            ExecutorService es = Executors.newFixedThreadPool(noOfCores);

            for (int count = 1; count <= noOfCores; ++count) {
                subPops[count - 1] = es.submit(new CreatePopThread(populationSize / noOfCores,
                    problem, opts, arithOpts, relOpts, terms, maxDepth,
                    ranGen));
            }//endforcount

            es.shutdown();

            Solution best = null;

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
    private Solution createPop() {
        //Creates the initial population.

        population = new Solution[populationSize];
        Solution best = null;

        for (int count = 0; count < populationSize; ++count) {
            Node prog = create();

            population[count] = problem.evaluate(prog);
            population[count].setHeuristic(prog);
            prog = null;

            if (count == 0)
                best = population[count];
            else if (population[count].fitter(best) == 1)
                best = population[count];

        }//endfor_count

        return best;
    }
/******************************************************************************/

    /******************************************************************************/
    private Node create() {
        //Creates an individual.
        Node root = new Node();
        int cur_dep = 1;

        root.setLabel(opts[ranGen.nextInt(opts.length)]);

        //root.setLabel("if");
        if (root.getLabel().compareTo("if") == 0)
            root.setArity(3);
        else
            root.setArity(2);


        return createInd(root, 1);
    }
/******************************************************************************/

    /******************************************************************************/
    private Node createInd(Node parent, int curDep) {
        ++curDep;
        for (int count = 0; count < parent.getArity(); ++count) {
            Node child = new Node();
            if (curDep == maxDepth) {
                child.setLabel(terms[ranGen.nextInt(terms.length)]);
                child.setArity(0);
            }//endif
            else {
                if (parent.getLabel().compareTo("if") == 0 && count == 0) {
                    child.setLabel(relOpts[ranGen.nextInt(relOpts.length)]);
                    child.setArity(2);
                } else {
                    boolean test = ranGen.nextBoolean();

                    if (test) {
                        child.setLabel(terms[ranGen.nextInt(terms.length)]);
                        child.setArity(0);
                    } else {
                        if ((maxDepth - curDep) < 2)
                            child.setLabel(arithOpts[ranGen.nextInt(arithOpts.length)]);
                        else
                            child.setLabel(opts[ranGen.nextInt(opts.length)]);

                        if (child.getLabel().compareTo("if") == 0)
                            child.setArity(3);
                        else
                            child.setArity(2);
                    }//endelse_rgen...
                }//endelse_parent...
            }//endelse_cur_depth...

            parent.addChild(createInd(child, curDep));
        }//endfor_count
        return parent;
    }
/******************************************************************************/

    /******************************************************************************/
    private Solution mutation(Solution parent) {
        Solution offspring;
        // System.out.println("Mutation");
        Node par = (Node) parent.getHeuristic();
        int mpoint = ranGen.nextInt(getSize(par));

        Node prog;
        if (mpoint == 0) {
            prog = create();
        } else {
            prog = mprog((Node) parent.getHeuristic(), mpoint);
        }
        if (offspringDepth > 0) {
            prune(prog, 0);
        }
        offspring = problem.evaluate(prog);
        offspring.setHeuristic(prog);
        prog = null;

        return offspring;
    }
/******************************************************************************/

    /******************************************************************************/
    private Node mprog(Node parent, int point) {
        Node new_prog = new Node();

        curPoint = 0;

        new_prog.setLabel(parent.getLabel());
        new_prog.setArity(parent.getArity());
        //System.out.println("Mutation Point "+point);
        //System.out.println("Parent ");
        //print_ind(parent);
        //System.out.println();
        //System.out.println("Offspring ");
        Node offspring = mutate(new_prog, parent, point);
        //print_ind(offspring);
        //System.out.println();
        return offspring;
    }
/******************************************************************************/

    /******************************************************************************/
    private Node mutate(Node oprog, Node pprog, int mpoint) {

        for (int count = 0; count < oprog.getArity(); ++count) {
            ++curPoint;

            if (mpoint == curPoint) {
                Node child;
                if (ranGen.nextBoolean()) {
                    child = new Node();
                    child.setLabel(terms[ranGen.nextInt(terms.length)]);
                    child.setArity(0);
                } else {

                    if (oprog.getLabel().compareTo("if") == 0 && count == 0)
                        child = mcreate(true);
                    else
                        child = mcreate(false);
                }
                // System.out.println("Subtree");
                //print_ind(child);
                //System.out.println();
                oprog.addChild(child);

            } else {
                Node child = new Node();
                child.setLabel(pprog.getChild(count).getLabel());
                child.setArity(pprog.getChild(count).getArity());
                oprog.addChild(mutate(child, pprog.getChild(count), mpoint));
            }
        }//endfor_count

        return oprog;
    }
/******************************************************************************/

    /******************************************************************************/
    private Node mcreate(boolean isif) {
        //Creates an individual.
        Node root = new Node();
        if (isif) {
            isif = false;
            root.setLabel(relOpts[ranGen.nextInt(relOpts.length)]);
        } else if (mutationDepth <= 2)
            root.setLabel(arithOpts[ranGen.nextInt(arithOpts.length)]);
        else
            root.setLabel(opts[ranGen.nextInt(opts.length)]);

        if (root.getLabel().compareTo("if") == 0)
            root.setArity(3);
        else
            root.setArity(2);

        return mcreateInd(root, 1, isif);
    }
/******************************************************************************/

    /******************************************************************************/
    private Node mcreateInd(Node parent, int cur_dep, boolean isif) {
        ++cur_dep;
        for (int count = 0; count < parent.getArity(); ++count) {
            Node child = new Node();
            if (cur_dep == mutationDepth) {

                child.setLabel(terms[ranGen.nextInt(terms.length)]);
                child.setArity(0);
            }//endif
            else {
                if (isif || (parent.getLabel().compareTo("if") == 0 && count == 0)) {
                    child.setLabel(relOpts[ranGen.nextInt(relOpts.length)]);
                    child.setArity(2);

                    if (isif)
                        isif = false;
                } else {
                    boolean test = ranGen.nextBoolean();

                    if (test) {
                        child.setLabel(terms[ranGen.nextInt(terms.length)]);
                        child.setArity(0);
                    } else {
                        if ((mutationDepth - cur_dep) < 2)
                            child.setLabel(arithOpts[ranGen.nextInt(arithOpts.length)]);
                        else
                            child.setLabel(opts[ranGen.nextInt(opts.length)]);

                        if (child.getLabel().compareTo("if") == 0)
                            child.setArity(3);
                        else
                            child.setArity(2);
                    }//endelse_rgen...
                }//endelse_parent...
            }//endelse_cur_depth...

            parent.addChild(mcreateInd(child, cur_dep, isif));
        }//endfor_count
        return parent;
    }
/******************************************************************************/

    /******************************************************************************/
    private int getSize(Node root) {
        int size = 1;

        for (int count = 0; count < root.getArity(); ++count) {
            size += getSize(root.getChild(count));
        }//endfor_count

        return size;
    }
/******************************************************************************/

    /******************************************************************************/
    private Solution[] crossover(Solution parent1, Solution parent2) {
        //Performs crossover

        //System.out.println("/******************************************/");
        Node p1 = (Node) parent1.getHeuristic();  //Gets the first parent
        Node p2 = (Node) parent2.getHeuristic();  //Gets the second parent

        //System.out.println("Parent 1");
        //print_ind(p1);

        //System.out.println();
        //System.out.println("Parent 2");
        //print_ind(p2);

        int c1 = getSize(p1);
        int c2 = getSize(p2);

        int cp1 = ranGen.nextInt(c1);
        int cp2 = ranGen.nextInt(c2);

        //System.out.println();
        //System.out.println(cp1+" "+cp2);
        Node o1 = new Node();

        o1.setLabel(p1.getLabel());
        o1.setArity(p1.getArity());
        Node sp1;
        if (cp1 == 0)
            sp1 = copy(p1, o1);
        else {
            crPoint = 0;
            getChild(cp1, p1);
            sp1 = crChild;
        }

        Node o2 = new Node();
        o2.setLabel(p2.getLabel());
        o2.setArity(p2.getArity());
        Node sp2;
        if (cp2 == 0)
            sp2 = copy(p2, o2);
        else {
            crPoint = 0;
            getChild(cp2, p2);
            crPoint = 0;
            sp2 = crChild;
        }

        if (cp1 == 0)
            o1 = sp2;

        else {
            crPoint = 0;
            o1 = subprog(cp1, p1, o1, sp2);
        }

        if (offspringDepth > 0) {
            prune(o1, 0);
        }

        if (cp2 == 0)
            o2 = sp1;
        else {
            crPoint = 0;
            o2 = subprog(cp2, p2, o2, sp1);
        }

        if (offspringDepth > 0) {
            prune(o2, 0);
        }

        Solution offspring[] = new Solution[2];

        offspring[0] = problem.evaluate(o1);
        offspring[0].setHeuristic(o1);

        offspring[1] = problem.evaluate(o2);
        offspring[1].setHeuristic(o2);

        return offspring;
    }
/******************************************************************************/

    /******************************************************************************/
    private Node copy(Node parent, Node cparent) {
        for (int count = 0; count < parent.getArity(); ++count) {
            Node child = parent.getChild(count);


            Node c = new Node();
            c.setLabel(child.getLabel());
            c.setArity(child.getArity());
            c = copy(child, c);

            cparent.addChild(c, count);

        }//endfor_count


        return cparent;
    }
/******************************************************************************/

    /******************************************************************************/
    private void getChild(int cp, Node parent) {
        boolean fin = false;
        for (int count = 0; !fin && count < parent.getArity(); ++count) {
            ++crPoint;
            Node child = parent.getChild(count);
            if (crPoint == cp) {
                crChild = child;
                fin = true;
            } else
                getChild(cp, child);
        }//endfor_count
    }
/******************************************************************************/

    /******************************************************************************/
    private Node subprog(int cp, Node parent, Node cparent, Node sp) {


        for (int count = 0; count < parent.getArity(); ++count) {
            ++crPoint;
            Node child = parent.getChild(count);
            if (crPoint != cp) {

                Node c = new Node();
                c.setLabel(child.getLabel());
                c.setArity(child.getArity());
                c = subprog(cp, child, c, sp);

                cparent.addChild(c, count);
            } else {
                cparent.addChild(sp, count);
            }
        }//endfor_count


        return cparent;

    }
/******************************************************************************/

    /******************************************************************************/
    private Solution selection() {
        //Selects a parent
        Solution best = population[ranGen.nextInt(population.length)];

        for (int count = 2; count <= tournamentSize; ++count) {
            Solution current = population[ranGen.nextInt(population.length)];

            if (current.fitter(best) == 1)
                best = current;


        }//endfor_count

        return best;
    }
/******************************************************************************/

    /******************************************************************************/
    private Solution regenerate(Solution best) {
        int mtimes = (int) Math.floor(populationSize * mutationRate);
        int ctimes = (int) Math.floor(populationSize * crossoverRate) / 2;

        if ((mtimes + ctimes * 2) < populationSize)
            mtimes += populationSize - (mtimes + ctimes * 2);

        //System.out.println("Rates "+ctimes+" "+mtimes);
        Solution npop[] = new Solution[populationSize];
        int index = 0;

        for (int mc = 1; mc <= mtimes; ++mc) {
            npop[index++] = mutation(selection());

            if (npop[index - 1].fitter(best) == 1)
                best = npop[index - 1];

        }//endfor_mc

        for (int cc = 1; cc <= ctimes; ++cc) {
            Solution ctemp[] = crossover(selection(), selection());
            npop[index++] = ctemp[0];
            if (ctemp[0].fitter(best) == 1)
                best = ctemp[0];
            npop[index++] = ctemp[1];
            if (ctemp[1].fitter(best) == 1)
                best = ctemp[1];
        }//endfor_cc

        population = npop;
        npop = null;

        return best;
    }
/******************************************************************************/

    /******************************************************************************/
    private Solution regen(Solution bestInd) {

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

                subPops[count - 1] = es.submit(new RegenThread(bestInd, popSize[count - 1],
                    ranGen, tournSelec, problem,
                    opts, arithOpts, relOpts, terms,
                    mutationDepth, offspringDepth,
                    mLimit, cLimit, rLimit));
            }//endforcount

            es.shutdown();

            Solution best = null;

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

/******************************************************************************/
    /**
     * This method implements the generation constructive hyper-heuristic.
     * @return The solution created by the heuristic. The instance of the solution
     * contains the heuristic, the fitness of the heuristic and the solution created
     * using the heuristic.
     */
    public Solution evolve() {
        //Implements the GP algorithm
        if (print)
            System.out.println("Generation 0");
        Solution best = creatPop();
        if (print) {
            printInd((Node) best.getHeuristic());
            System.out.print(" " + best.getFitness());
            System.out.println();
        }

        for (int count = 1; count <= noOfGenerations; ++count) {
            if (print)
                System.out.println("Generation " + count);
            Solution prog = regen(best);
            if (prog.fitter(best) == 1)
                best = prog;

            if (print) {
                printInd((Node) best.getHeuristic());
                System.out.print(" " + best.getFitness());
                System.out.println();
            }
        }//endfor_count
        System.out.println("Completed evolving heuristic");
        return best;

    }
/******************************************************************************/

}

