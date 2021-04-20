#
#  * This class implements a problem domain and implements ProblemDomain abstract
#  * class.
#  *
#  * N. Pillay
#  *
#  * 30 August 2016 
#  
# package: solveproblem

from GeneticAlgorithm.ProblemDomain import ProblemDomain
from GeneticAlgorithm.examples.GeneticAlgorithm.ComOptSoln import ComOptSoln


class ComOptProb(ProblemDomain):
    # Methods that are abstract in ProblemDomain that need to be implemented
    def evaluate(self, heuristic_combination):
        # Implements the abstract method to create a solution using heuristicComb
        # using an instane of the InitialSoln class which is also used to calculate
        # the fitness using the objective value of the created solution.
        soln = ComOptSoln()
        soln.set_heuristic_combination(heuristic_combination)
        soln.create_solution()
        return soln

