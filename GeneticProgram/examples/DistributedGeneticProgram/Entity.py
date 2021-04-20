#
#  * This class is used to represent an entity which is allocated at each point in
#  * creating a solution. The class is used to illustrate the use of the GeneticProgram
#  * library.
#  *
#  * Nelishia Pillay
#  *
#  * 8 October 2016
#
# package: createheuristic
import functools


@functools.total_ordering
class Entity:
    #
    #  * Data elements
    #
    #
    #  * Stores the attributes for the entity.
    #
    attribs: float

    #
    #  * Stores the value of the evolved heuristic
    #
    heuristic: float

    def __init__(self):
        self.heuristic = 0.0

    def setAttribs(self, attribs):
        #
        #     * This method sets the attribute values for the entity.
        #
        self.attribs = attribs

    def getAttribs(self):
        #
        #     * Returns the attribute values for the entity.
        #
        return self.attribs

    def setHeuristic(self, heuristic):
        #
        #     * Sets the heuristic value for the entity.
        #
        self.heuristic = heuristic

    def getHeuristic(self):
        #
        #     * Returns the heuristic values for the entity.
        #
        return self.heuristic

    def __lt__(self, other):
        #
        #     * The class implements the Comparable class and this method compares two
        #     * entities for the sort that will be used to sort the entities in ascending
        #     * order according to the heuristic to create a solution.
        #
        if self.heuristic < other.getHeuristic():
            return -1
        elif self.heuristic > other.getHeuristic():
            return 1
        else:
            return 0
