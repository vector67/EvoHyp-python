#
#  * This class extends the abstract class Solution to define a solution for the
#  * problem domain.
#  *
#  * Nelishia Pillay
#  *
#  * 8 October 2016
#
# package: createheuristic

from random import Random
from typing import List

from GenProg.Evaluator import Evaluator
from GenProg.Node import Node
from GenProg.Solution import Solution
import sys

from examples.DistributedGeneticProgram.Entity import Entity


class ComOptSoln(Solution):
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
    soln: List[Entity]

    # It may be necessary to store other values that are specific to problem being
    # solved that is different from the fitness or needed to calculate the fitness.
    # For example, for the examination timetabling problem the hard and soft
    # constraint cost also needs to be stored.
    #
    #   * Stores the attributes. Each character in the string represents a different
    #   * attribute.
    #
    attributes: str

    # ABSTRACT METHODS
    def setHeuristic(self, heuristic):
        #
        #     * Sets the evolved heuristic.
        #
        self.heuristic = heuristic

    def getHeuristic(self):
        #
        #     * Returns the evolved heuristic.
        #
        return self.heuristic

    def getSoln(self):
        #
        #     * Returns the solution created using the evolved heuristic.
        #
        return self.soln

    def getFitness(self):
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
        if other.getFitness() < self.fitness:
            return -1
        elif other.getFitness() > self.fitness:
            return 1
        else:
            return 0

    # OTHER METHODS
    def __init__(self):
        super(ComOptSoln, self).__init__()
        self.soln = None
        # Initialize the problem domain
        self.initEntities()

    def initEntities(self):
        #
        #     * Initializes the entities to illustrate how a solution can be created
        #     * used the evolved heuristic. Creates and stores three entities. The
        #     * attribute values for the entities are randomly selected.
        #
        # Initialize random number generator.
        ranGen = Random()
        # Initialize the entities array.
        self.entities = [None] * 3
        # Loop to create entities.
        count = 0
        while count < 3:
            attribs = [ranGen.randrange(10) + 1, ranGen.randrange(10) + 1, ranGen.randrange(10) + 1]
            self.entities[count] = Entity()
            self.entities[count].setAttribs(attribs)
            count += 1

    def calcHeuristics(self):
        count = 0
        while count < len(self.entities):
            if self.entities[count].getHeuristic() != sys.float_info.max:
                evaluator = Evaluator(self.attributes, self.entities[count].getAttribs())
                self.entities[count].setHeuristic(evaluator.eval(self.heuristic))
            count += 1

    def createSoln(self, attributes):
        self.attributes = attributes
        self.calcHeuristics()
        self.entities.sort()
        while self.entities[0].getHeuristic() != sys.float_info.max:
            if self.soln is None:
                self.soln = []
            self.soln.append(self.entities[0])
            self.updateEntities()
            self.calcHeuristics()
            self.entities.sort()
        entity = self.soln[0]
        a = entity.getAttribs()
        self.fitness = a[0] + a[1] + a[2]

    def updateEntities(self):
        self.entities[0].setHeuristic(sys.float_info.max)
        count = 1
        while count < len(self.entities):
            attribs = self.entities[count].getAttribs()
            attribs[1] -= 1
            self.entities[count].setAttribs(attribs)
            count += 1
