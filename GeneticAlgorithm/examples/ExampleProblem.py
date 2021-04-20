#
#  * This class implements a problem domain and implements ProblemDomain abstract
#  * class.
#  *
#  * N. Pillay
#  *
#  * 30 August 2016 
#  
# package: solveproblem

from GeneticAlgorithm.Problem import Problem
from GeneticAlgorithm.examples.ExampleSolution import ExampleSolution


class ExampleProblem(Problem):
    # Methods that are abstract in ProblemDomain that need to be implemented
    def evaluate(self, heuristic_combination):
        # Implements the abstract method to create a solution using heuristicComb
        # using an instane of the InitialSoln class which is also used to calculate
        # the fitness using the objective value of the created solution.
        soln = ExampleSolution()
        soln.set_heuristic_combination(heuristic_combination)
        soln.create_solution()
        return soln

