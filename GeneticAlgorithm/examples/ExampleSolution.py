#
#  * This class implements the InitialSoln abstract class and is used to store
#  * details of the initial solution.
#  *
#  * Nelishia Pillay
#  *
#  * 30 August 2016
#
from typing import List

from GeneticAlgorithm.Solution import Solution


class ExampleSolution(Solution):
    # Data elements
    # Stores the heuristic combination that will be used to create an initial
    # solution.    
    heuristic_combination: str = ''

    # Stores the fitness value to be used for the initial solution created.
    fitness: float

    # Stores the initial solution created using the heuristic. In this problem
    # this is stored as an array of strings just as an example. However, the 
    # solution can be of any type, e.g. for the travelling salesman problem it 
    # could be a string representing the tour.
    initial_solution: List[str] = []

    # It may be necessary to store other values that are specific to problem being
    # solved that is different from the fitness or needed to calculate the fitness.
    # For example, for the examination timetabling problem the hard and soft
    # constraint cost also needs to be stored.
    # Implementation of abstract methods needed to extend InitialSoln
    def get_fitness(self) -> float:
        return self.fitness

    def set_heuristic_combination(self, heuristic_combination: str):
        # Implements the abstract method to store the heuristic combination used to
        # create an initial solution.
        self.heuristic_combination = heuristic_combination

    def get_heuristic_combination(self) -> str:
        # Implements the abstract method to return the heuristic combination used to
        # create the solution.
        return self.heuristic_combination

    def get_solution(self) -> object:
        # Implements the abstract method to return a solution.
        return self.initial_solution

    def fitter(self, other: Solution):
        # This method is used to compare two intial solutions to determine which of
        # the two is fitter. 
        if other.get_fitness() < self.fitness:
            return 1
        elif other.get_fitness() > self.fitness:
            return -1
        else:
            return 0

    # Methods in addition to the abstract methods that need to be implemented.
    def create_solution(self):
        # This method creates a solution using the heuristic combination.
        # Construct a solution to the problem using the heuristic combination.
        temp = ["This", " is", " a", " solution", " created", " using ", self.heuristic_combination]
        self.initial_solution = temp
        # Calculate the fitness of the constructed solution. This is just an example
        # so simply adds the length of the solution to a random double.
        self.fitness = len(temp)
