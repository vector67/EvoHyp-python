#!/usr/bin/env python
#
#  * This class implements a problem domain and implements ProblemDomain abstract
#  * class.
#  *
#  * N. Pillay
#  *
#  * 30 August 2016 
#  
# package: solveproblem
# Import statements
from ProblemDomain import ProblemDomain
from examples.GeneticAlgorithm.ComOptSoln import ComOptSoln


class ComOptProb(ProblemDomain):
    """ generated source for class ComOptProb """
    # Methods that are abstract in ProblemDomain that need to be implemented
    # /
    def evaluate(self, heuristic_combination):
        """ generated source for method evaluate """
        # Implements the abstract method to create a solution using heuristicComb
        # using an instane of the InitialSoln class which is also used to calculate
        # the fitness using the objective value of the created solution.
        soln = ComOptSoln()
        soln.set_heuristic_combination(heuristic_combination)
        soln.create_solution()
        return soln

    # /

