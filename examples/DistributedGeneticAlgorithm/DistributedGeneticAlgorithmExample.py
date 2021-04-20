#
#  * This is an example program used to illustrate how to use the EvoHyp library
#  * to create an initial solution to a problem using the genetic algorithm
#  * hyper-heuristic provided by the library.
#  *
#  * N. Pillay
#  *
#  * 30 August 2016
#
from DistrGenAlg.DistrGenAlg import DistrGenAlg
from examples.DistributedGeneticAlgorithm.ComOptProb import ComOptProb


class SolveProblem(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GenAlg library can be used to solve a combinatorial optimization problem.
        problem = ComOptProb()
        seed = 12347862715392
        heuristics = str("abc")
        schh = DistrGenAlg(seed, heuristics, 4)
        schh.set_parameters("../../DistrGenAlg/Parameters.txt")
        schh.set_problem(problem)
        solution = schh.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness: ", solution.get_fitness())
        print("Heuristic combination: ", solution.get_heuristic_combination())
        print("Solution: ")
        SolveProblem.displaySolution(solution.get_solution())

    @classmethod
    def displaySolution(cls, solution):
        # Displays the solution.
        print(' '.join(solution))

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    SolveProblem.main(sys.argv)