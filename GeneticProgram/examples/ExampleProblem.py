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

from GeneticProgram.Problem import Problem
from GeneticProgram.examples.ExampleSolution import ExampleSolution


class ExampleProblem(Problem):
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
        #     * using an instance of the Solution class which is also used to calculate
        #     * the fitness using the objective value of the created solution.
        #     
        solution = ExampleSolution()
        solution.set_heuristic(heuristic)
        solution.create_solution(self.attributes)
        return solution

    def set_attributes(self, attributes):
        #
        #     * Sets the attribute string.
        #     
        self.attributes = attributes
