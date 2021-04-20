#
#  * This class contains the driver program for the class illustrate the use of
#  * the GeneticProgram library.
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#
import time

from GeneticProgram.DistributedGeneticProgram import DistributedGeneticProgram
from GeneticProgram.examples.ExampleProblem import ExampleProblem
from GeneticProgram.examples.ExampleSolution import ExampleSolution


class DistributedGeneticProgramExample(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GeneticAlgorithm library can be used to solve a combinatorial optimization problem.
        problem = ExampleProblem()
        seed = round(time.time() * 1000)
        attribs = str("abc")
        problem.set_attributes(attribs)
        distributed_genetic_program = DistributedGeneticProgram(seed, attribs, 1, 4)
        distributed_genetic_program.set_parameters("Parameters.txt")
        distributed_genetic_program.set_problem(problem)
        solution = distributed_genetic_program.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness:", solution.get_fitness())
        print("Heuristic: ")
        print((solution.get_heuristic()).__str__())
        print("Solution: ")
        DistributedGeneticProgramExample.display_solution(solution)

    @classmethod
    def display_solution(cls, solution: ExampleSolution):
        # Displays the solution.
        print(' '.join(
            [
                ' '.join(
                    list(map(str, [attribs.get_attributes()[i] for i in range(3)]))
                ) for attribs in solution.get_solution()
            ]
        ))

    @classmethod
    def main(cls):
        cls.solve()


if __name__ == '__main__':
    DistributedGeneticProgramExample.main()
