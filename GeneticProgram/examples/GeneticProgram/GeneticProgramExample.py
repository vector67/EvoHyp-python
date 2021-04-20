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
        problem.set_attributes(attribs)
        genetic_program = GeneticProgram(seed, attribs, 1)
        genetic_program.set_parameters("Parameters.txt")
        genetic_program.set_problem(problem)
        sol = genetic_program.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness:", sol.get_fitness())
        print("Heuristic: ")
        print((sol.get_heuristic()).__str__())
        print("Solution: ")
        cls.display_solution(sol.get_solution())

    @classmethod
    def display_solution(cls, solution):
        # Displays the solution.
        for element in solution:
            attribs = element.get_attributes()
            print(attribs[0], attribs[1], attribs[2])
        print()

    @classmethod
    def main(cls):
        cls.solve()


if __name__ == '__main__':
    GeneticProgramExample.main()
