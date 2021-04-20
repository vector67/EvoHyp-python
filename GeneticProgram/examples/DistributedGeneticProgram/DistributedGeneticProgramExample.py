#
#  * This class contains the driver program for the class illustrate the use of
#  * the GeneticProgram library.
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#
import time

from GeneticProgram.Distributed.DistributedGeneticProgram import DistributedGeneticProgram
from GeneticProgram.Solution import Solution
from GeneticProgram.examples.DistributedGeneticProgram.ComOptProb import ComOptProb


class DistributedGeneticProgramExample(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GeneticAlgorithm library can be used to solve a combinatorial optimization problem.
        problem = ComOptProb()
        seed = round(time.time() * 1000)
        attribs = str("abc")
        problem.setAttribs(attribs)
        gchh = DistributedGeneticProgram(seed, attribs, 1, 4)
        gchh.set_parameters("Parameters.txt")
        gchh.set_problem(problem)
        solution = gchh.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness:", solution.getFitness())
        print("Heuristic: ")
        print((solution.getHeuristic()).__str__())
        print("Solution: ")
        DistributedGeneticProgramExample.displaySolution(solution)

    @classmethod
    def displaySolution(cls, soln: Solution):
        # Displays the solution.
        print(' '.join([' '.join(list(map(str, [attribs.getAttribs()[i] for i in range(3)]))) for attribs in soln.getSoln()]))

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    DistributedGeneticProgramExample.main(sys.argv)
