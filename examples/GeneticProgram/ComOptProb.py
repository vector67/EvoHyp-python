#
#  * This class extends the Problem abstract class to implement the problem 
#  * domain. The class constructs a solution to the problem using an evolved
#  * heuristic instead of an existing heuristic. The heuristic has to be 
#  * calculated for each entity that is added at each point in constructing a
#  * solution.
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#  * 
#  
# package: createheuristic

from GeneticProgram.Problem import Problem
from examples.GeneticProgram.ComOptSoln import ComOptSoln


class ComOptProb(Problem):
    #
    #   * Data elements
    #   
    # 
    #    * Stores the attributes with each character in the string representing a 
    #    * different attribute.
    #    
    attributes = str()

    def evaluate(self, heuristic):
        #
        #     * Implements the abstract method to create a solution using heuristicComb
        #     * using an instance of the InitialSoln class which is also used to calculate
        #     * the fitness using the objective value of the created solution.
        #     
        soln = ComOptSoln()
        soln.setHeuristic(heuristic)
        soln.createSoln(self.attributes)
        return soln

    def setAttribs(self, attributes):
        #
        #     * Sets the attribute string.
        #     
        self.attributes = attributes
