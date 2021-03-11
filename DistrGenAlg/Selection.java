/**
 * This class performs selection over the distributed population to obtain
 * parents to create offspring of the next generation.
 * <p>
 * Nelishia Pillay
 * 17 December 2016
 */
package distrgenalg;

import java.util.Random;

import initialsoln.*;

/**
 * Performs tournament selection across subpopulations created on different 
 * cores.
 */
public class Selection {
    /******************************************************************************/
//Data elements
    private InitialSoln population[][];
    private Random ranGen;
    private int tournSize;
    private int cores;
/******************************************************************************/

/******************************************************************************/
    /**
     * Constructor for the selection class.
     * @param population Subpopulations created on different cores.
     * @param ranGen Random number generator.
     * @param tournSize Tournament size.
     * @param cores Number of cores.
     */
    public Selection(InitialSoln population[][], Random ranGen, int tournSize,
                     int cores) {
        this.population = population;
        this.ranGen = ranGen;
        this.tournSize = tournSize;
        this.cores = cores;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * Performs tournament selection across the subpopulations created on different
     * cores.
     * @return The selected parent of type <tt>InitialSoln</tt>.
     */
    public InitialSoln Selection() {
        /*Implements the tournament selection method. */

        //Select a parent
        InitialSoln winner = GetEle();

        for (int count = 2; count <= tournSize; ++count) {
            InitialSoln current = GetEle();

            if (current.fitter(winner) == 1)
                winner = current;
        }//endfor_count

        return winner;
    }
/******************************************************************************/

    /******************************************************************************/
    private InitialSoln GetEle() {
        int p = ranGen.nextInt(cores);

        return population[p][ranGen.nextInt(population[p].length)];
    }
/******************************************************************************/
}
