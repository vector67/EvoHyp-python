/**
 * Thread for creating the new population using mutation.
 * <p>
 * Nelishia Pillay
 * 16 January 2017
 */
package distrgenprog;

//Import statemetnts

import java.util.Random;
import java.util.concurrent.Callable;

import solution.*;
import problem.*;

/**
 * Implements the thread for regeneration to create successive generations 
 * using mutation and crossover.
 */
public class RegenThread implements Callable {
    /******************************************************************************/
//Data elements
    private Solution bestInd;  //Stores the best individual passed to it
    private int popSize; //Stores the population size
    private Random ranGen; //Stores the random number generator
    private Selection tournSelec; //Selection method
    private Problem problem;
    private String opts[];
    private String arithOpts[];
    private String relOpts[];
    private String terms[];
    private int maxDepth;
    private int offspringDepth;
    private int curPoint;
    private int mutationDepth;
    private int crPoint;
    /**
     * Temporary node instance used by the crossover operator.
     */
    private Node crChild;
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
     * @param mutationDepth Maximum length of the substring created by mutation.
     * @param problem An instance of the problem domain.
     * @param opts The general operators, e.g. if-then-else.
     * @param arithOpts The arithmetic operators.
     * @param relOpts The relational operators.
     * @param terms The terminal set.
     * @param mutationDepth The maximum depth permitted for the subtree created by
     * the mutation operator.
     * @param offspringDepth Maximum permitted offspring length.
     * @param mLimit The number of offspring to be created using mutation.
     * @param cLimit The number of offspring to be created using crossover.
     */
    public RegenThread(Solution bestInd, int popSize, Random ranGen,
                       Selection tournSelec, Problem problem, String opts[],
                       String arithOpts[], String relOpts[], String terms[],
                       int mutationDepth, int offspringDepth,
                       int mLimit, int cLimit, int rLimit) {
        this.bestInd = bestInd;
        this.popSize = popSize;
        this.ranGen = ranGen;
        this.tournSelec = tournSelec;
        this.problem = problem;
        this.opts = opts;
        this.arithOpts = arithOpts;
        this.relOpts = relOpts;
        this.terms = terms;
        this.maxDepth = maxDepth;
        this.mutationDepth = mutationDepth;
        this.offspringDepth = offspringDepth;
        this.mLimit = mLimit;
        this.cLimit = cLimit;
        this.rLimit = rLimit;
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
    /**
     * Method call for the thread for regeneration.
     */
    public Output call() {
        Solution best = bestInd;

        Solution subPop[] = new Solution[popSize];
        int index = 0;


        for (int mc = 1; mc <= mLimit; ++mc) {
            subPop[index++] = mutation(tournSelec.Selection());
            if (subPop[index - 1].fitter(best) == 1)
                best = subPop[index - 1];

        }//endfor_mc

        for (int rc = 1; rc <= rLimit; ++rc) {
            subPop[index++] = tournSelec.Selection();
            if (subPop[index - 1].fitter(best) == 1)
                best = subPop[index - 1];

        }//endfor_rc
        int co = 0;
        while (cLimit > 0 && co < cLimit) {
            Solution ctemp[] = crossover(tournSelec.Selection(), tournSelec.Selection());
            if (index < subPop.length) {
                subPop[index++] = ctemp[0];
                ++co;
                if (ctemp[0].fitter(best) == 1)
                    best = ctemp[0];
            }
            if (index < subPop.length) {
                subPop[index++] = ctemp[1];
                ++co;
                if (ctemp[1].fitter(best) == 1)
                    best = ctemp[1];
            }

        }//endwhile


        Output output = new Output();
        output.setBest(best);
        output.setSubPop(subPop);

        return output;
    }
/******************************************************************************/
}
