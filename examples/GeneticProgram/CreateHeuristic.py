#
#  * This class contains the driver program for the class illustrate the use of
#  * the GeneticProgram library.
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#  
# package: createheuristic
import time

from GeneticProgram.GeneticProgram import GeneticProgram
from examples.GeneticProgram.ComOptProb import ComOptProb


class CreateHeuristic(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GeneticAlgorithm library can be used to solve a combinatorial optimization problem.
        problem = ComOptProb()
        seed = round(time.time() * 1000)
        attribs = str("abc")
        problem.setAttribs(attribs)
        gchh = GeneticProgram(seed, attribs, 1)
        gchh.set_parameters("Parameters.txt")
        gchh.set_problem(problem)
        sol = gchh.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness:", sol.getFitness())
        print("Heuristic: ")
        print((sol.getHeuristic()).__str__())
        print("Solution: ")
        cls.displaySolution(sol.getSoln())

    @classmethod
    def displaySolution(cls, soln):
        # Displays the solution.
        count = 0
        while count < len(soln):
            attribs = soln[count].getAttribs()
            print(attribs[0], attribs[1], attribs[2])
            print()
            count += 1
        # EndforCount
        print()

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    CreateHeuristic.main(sys.argv)
