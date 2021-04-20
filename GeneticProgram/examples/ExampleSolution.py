#
#  * This class extends the abstract class Solution to define a solution for the
#  * problem domain.
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#  

from random import Random
from typing import List

from GeneticProgram.Evaluator import Evaluator
from GeneticProgram.Node import Node
from GeneticProgram.Solution import Solution
from GeneticProgram.examples.Entity import Entity
import sys


class ExampleSolution(Solution):
    #  * Data elements
    #  
    # 
    #   * Stores the entities.
    #   
    entities: List[Entity]

    # 
    #   * Stores the fitness value to be used for the initial solution created.
    #   
    fitness: float

    # 
    #   * Stores the evolved heuristic.
    #   
    heuristic: Node

    # 
    #   * Stores the initial solution created using the heuristic. In this problem
    #   * this is stored as an array of strings just as an example. However, the 
    #   * solution can be of any type, e.g. for the traveling salesman problem it 
    #   * could be a string representing the tour.
    #   
    solution: List[Entity]

    # It may be necessary to store other values that are specific to problem being
    # solved that is different from the fitness or needed to calculate the fitness.
    # For example, for the examination timetabling problem the hard and soft
    # constraint cost also needs to be stored. 
    # 
    #   * Stores the attributes. Each character in the string represents a different
    #   * attribute.
    #   
    attributes: str

    def set_heuristic(self, heuristic):
        #
        #     * Sets the evolved heuristic.
        #     
        self.heuristic = heuristic

    def get_heuristic(self):
        #
        #     * Returns the evolved heuristic.
        #     
        return self.heuristic

    def get_solution(self) -> List[Entity]:
        #
        #     * Returns the solution created using the evolved heuristic.
        #     
        return self.solution

    def get_fitness(self):
        #
        #     * Returns the fitness calculated using the solution constructed using the
        #     * evolved heuristic.
        #     
        return self.fitness

    def fitter(self, other):
        #
        #     * This method is used to compare two solutions to determine which of 
        #     * the two is fitter. 
        #     
        if other.get_fitness() < self.fitness:
            return -1
        elif other.get_fitness() > self.fitness:
            return 1
        else:
            return 0

    # OTHER METHODS
    def __init__(self):
        super(ExampleSolution, self).__init__()
        self.solution = []
        # Initialize the problem domain  
        self.initialize_entities()

    def initialize_entities(self):
        #
        #     * Initializes the entities to illustrate how a solution can be created
        #     * used the evolved heuristic. Creates and stores three entities. The
        #     * attribute values for the entities are randomly selected.
        #     
        # Initialize random number generator.  
        random_generator = Random()
        self.entities = []
        # Loop to create entities.
        for i in range(3):
            attribs = [random_generator.randrange(10) + 1 for _ in range(3)]
            new_entity = Entity()
            new_entity.set_attributes(attribs)
            self.entities.append(new_entity)

    def calculate_heuristics(self):
        for entity in self.entities:
            if entity.get_heuristic() != sys.float_info.max:
                evaluator = Evaluator(self.attributes, entity.get_attributes())
                entity.set_heuristic(evaluator.eval(self.heuristic))

    def create_solution(self, attributes):
        self.attributes = attributes
        self.calculate_heuristics()
        self.entities.sort()
        while self.entities[0].get_heuristic() != sys.float_info.max:
            if self.solution is None:
                self.solution = []
            self.solution.append(self.entities[0])
            self.update_entities()
            self.calculate_heuristics()
            self.entities.sort()
        entity = self.solution[0]
        a = entity.get_attributes()
        self.fitness = a[0] + a[1] + a[2]

    def update_entities(self):
        self.entities[0].set_heuristic(sys.float_info.max)
        count = 1
        while count < len(self.entities):
            attribs = self.entities[count].get_attributes()
            attribs[1] -= 1
            self.entities[count].set_attributes(attribs)
            count += 1
