#!/usr/bin/env python
#
#  * This is an example program used to illustrate how to use the EvoHyp library
#  * to create an initial solution to a problem using the genetic algorithm
#  * hyper-heuristic provided by the library.
#  *
#  * N. Pillay
#  *
#  * 30 August 2016
#  
# package: solveproblem
# Import statements
from GenAlg.GenAlg import GenAlg
from examples.ComOptProb import ComOptProb


class GeneticAlgorithmExample(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GenAlg library can be used to solve a combinatorial optimization problem.
        problem = ComOptProb()
        seed = 23498263498
        heuristics = "abc"
        schh = GenAlg(seed, heuristics)
        schh.set_parameters("../GenAlg/Parameters.txt")
        schh.set_problem(problem)
        solution = schh.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness:", solution.get_fitness())
        print("Heuristic combination: " + solution.get_heuristic_combination())
        print("Solution: ")
        GeneticAlgorithmExample.display_solution(solution.get_solution())

    @classmethod
    def display_solution(cls, solution):
        # Displays the solution.
        print(' '.join(solution))

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    GeneticAlgorithmExample.main(sys.argv)
