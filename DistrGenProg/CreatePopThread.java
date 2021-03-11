/**
 * Thread for creating part of the initial population.
 * <p>
 * Nelishia Pillay
 * 16 January 2017
 */
package distrgenprog;

import java.util.Random;
import java.util.concurrent.Callable;

import problem.Problem;
import solution.Solution;

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
    private Solution subPop[];

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
     * Stores and instances of the random number generator to be used by the genetic
     * algorithm.
     */
    private Random ranGen;

/******************************************************************************/

/******************************************************************************/
    /**
     * Constructor for the thread for initial population generation.
     * @param popSize Specifies the population size.
     * @param problem Specifies the problem domain.
     * @param opts Specifies the general operators, e.g. if-then-else.
     * heuristic.
     * @param arithOpts Specifies the arithmetic operators.
     * @param relOpts Specifies the relational operators.
     * @param terms Specifies the terminals.
     * @param maxDepth Specifies the maximum depth permitted for parse trees
     * created during initial population generation.
     * @param ranGen Random number generator.
     */
    public CreatePopThread(int popSize, Problem problem, String opts[],
                           String arithOpts[], String relOpts[], String terms[],
                           int maxDepth, Random ranGen) {
        this.popSize = popSize;
        this.problem = problem;
        this.opts = opts;
        this.arithOpts = arithOpts;
        this.relOpts = relOpts;
        this.terms = terms;
        this.maxDepth = maxDepth;
        this.ranGen = ranGen;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * The call method for the thread for initial population generation.
     */
    public Output call() {
        //Creates the initial population.

        subPop = new Solution[popSize];
        Solution best = null;

        for (int count = 0; count < popSize; ++count) {
            Node prog = create();
            subPop[count] = problem.evaluate(prog);

            subPop[count].setHeuristic(prog);
            prog = null;

            if (count == 0)
                best = subPop[count];
            else if (subPop[count].fitter(best) == 1)
                best = subPop[count];

        }//endfor_count

        Output output = new Output();
        output.setBest(best);
        output.setSubPop(subPop);

        return output;
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

        Node temp = null;
        try {
            temp = createInd(root, 1);
        } catch (Exception e) {
            System.out.println("Problem");
        }
        return temp;
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

}
