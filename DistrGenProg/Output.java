/**
 * Defines the class to store output from each core.
 * <p>
 * Nelishia Pillay
 * 16 January 2017
 */
package distrgenprog;

//Import statements

import solution.*;

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
    private Solution best;

    /**
     * Stores the subpopulation of heuristic combinations created on the core.
     */
    private Solution subPop[];

    /*****************************************************************************/

    /*****************************************************************************/

    /**
     *
     * @return The best heuristic combination and solution stored.
     */
    public Solution getBest() {
        return best;
    }
    /*****************************************************************************/

    /*****************************************************************************/

    /**
     * Stores the best solution together with the heuristic producing
     * it during initial population generation.
     * @param best Best solution and heuristic found during initial
     * population generation.
     */
    public void setBest(Solution best) {
        this.best = best;
    }
    /*****************************************************************************/

    /*****************************************************************************/

    /**
     *
     * @return Returns a subpopulation.
     */
    public Solution[] getSubPop() {
        return subPop;
    }
    /*****************************************************************************/

    /*****************************************************************************/

    /**
     * Stores the subpopulation created on a core.
     * @param subPop Subpopulation of heuristics and corresponding
     * solutions.
     */
    public void setSubPop(Solution subPop[]) {
        this.subPop = subPop;
    }
    /*****************************************************************************/
}

