/**
 * This class performs selection over the distributed population to obtain
 * parents to create offspring of the next generation.
 * <p>
 * Nelishia Pillay
 * 16 January 2016
 */
package distrgenprog;

import java.util.Random;

import solution.*;

/**
 * Performs tournament selection across subpopulations created on different 
 * cores.
 */
public class Selection {
    /******************************************************************************/
//Data elements
    private Solution population[][];
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
    public Selection(Solution population[][], Random ranGen, int tournSize,
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
     * @return The selected parent of type <tt>Solution</tt>.
     */
    public Solution Selection() {

        //Select a parent
        Solution winner = GetEle();

        for (int count = 2; count <= tournSize; ++count) {
            Solution current = GetEle();

            if (current.fitter(winner) == 1)
                winner = current;
        }//endfor_count

        return winner;
    }
/******************************************************************************/

    /******************************************************************************/
    private Solution GetEle() {
        int p = ranGen.nextInt(cores);

        return population[p][ranGen.nextInt(population[p].length)];
    }
/******************************************************************************/
}

