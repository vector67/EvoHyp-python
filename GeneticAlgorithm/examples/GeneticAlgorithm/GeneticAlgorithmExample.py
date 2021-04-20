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

import time

from GeneticAlgorithm.GeneticAlgorithm import GeneticAlgorithm
from GeneticAlgorithm.examples.ExampleProblem import ExampleProblem


class GeneticAlgorithmExample(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GeneticAlgorithm library can be used to solve a combinatorial optimization problem.
        problem = ExampleProblem()
        seed = round(time.time() * 1000)
        heuristics = "abc"
        genetic_algorithm = GeneticAlgorithm(seed, heuristics)
        genetic_algorithm.set_parameters("Parameters.txt")
        genetic_algorithm.set_problem(problem)
        solution = genetic_algorithm.evolve()
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
    def main(cls):
        cls.solve()


if __name__ == '__main__':
    GeneticAlgorithmExample.main()
