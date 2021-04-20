#
#  * This class contains the driver program for the class illustrate the use of
#  * the GeneticProgram library.
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#  

import time

from GeneticProgram.GeneticProgram import GeneticProgram
from GeneticProgram.examples.ExampleProblem import ExampleProblem


class GeneticProgramExample(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GeneticAlgorithm library can be used to solve a combinatorial optimization problem.
        problem = ExampleProblem()
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
        cls.display_solution(sol.getSoln())

    @classmethod
    def display_solution(cls, solution):
        # Displays the solution.
        for element in solution:
            attribs = element.getAttribs()
            print(attribs[0], attribs[1], attribs[2])
        print()

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    GeneticProgramExample.main(sys.argv)
