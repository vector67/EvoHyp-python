/**
 * Defines the class to store output from each core.
 * <p>
 * Nelishia Pillay
 * 10 December 2016
 */
package distrgenalg;

import initialsoln.InitialSoln;

/**
 * Class to store the subpopulations and best individual over the subpopulations. 
 */
public class Output {
    /*****************************************************************************/
    //Data elements
    /**
     * Stores the fittest heuristic combination from the subpopulation created on
     * the core.
     */
    private InitialSoln best;

    /**
     * Stores the subpopulation of heuristic combinations created on the core.
     */
    private InitialSoln subPop[];

    /*****************************************************************************/

    /*****************************************************************************/

    /**
     *
     * @return The best heuristic combination and solution stored.
     */
    public InitialSoln getBest() {
        return best;
    }
    /*****************************************************************************/

    /*****************************************************************************/

    /**
     * Stores the best solution together with the heuristic combination producing
     * it during initial population generation.
     * @param best Best solution and heuristic combination found during initial
     * population generation.
     */
    public void setBest(InitialSoln best) {
        this.best = best;
    }
    /*****************************************************************************/

    /*****************************************************************************/

    /**
     *
     * @return Returns a subpopulation.
     */
    public InitialSoln[] getSubPop() {
        /**
         * Returns the subpopulation created on the core.
         */

        return subPop;
    }
    /*****************************************************************************/

    /*****************************************************************************/

    /**
     * Stores the subpopulation created on a core.
     * @param subPop Subpopulation of heuristic combinations and corresponding
     * solutions.
     */
    public void setSubPop(InitialSoln subPop[]) {
        this.subPop = subPop;
    }
    /*****************************************************************************/
}
