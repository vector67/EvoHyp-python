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
from typing import List

from GeneticAlgorithm.DistributedGeneticAlgorithm import DistributedGeneticAlgorithm
from GeneticAlgorithm.Solution import Solution
from GeneticAlgorithm.examples.ExampleProblem import ExampleProblem


class SolveProblem(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GeneticAlgorithm library can be used to solve a combinatorial optimization problem.
        problem = ExampleProblem()
        seed = round(time.time() * 1000)
        heuristics = str("abc")
        schh = DistributedGeneticAlgorithm(seed, heuristics, 4)
        schh.set_parameters("../../Distributed/Parameters.txt")
        schh.set_problem(problem)
        solution: Solution = schh.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness: ", solution.get_fitness())
        print("Heuristic combination: ", solution.get_heuristic_combination())
        print("Solution: ")
        SolveProblem.displaySolution(solution.get_solution())

    @classmethod
    def displaySolution(cls, solution: List):
        # Displays the solution.
        print(' '.join(solution))

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    SolveProblem.main(sys.argv)
