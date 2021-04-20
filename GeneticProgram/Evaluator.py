#
#  * This class defines the evaluator to evaluate the heuristic given the values
#  * of the attributes.
#  * <p>
#  * Nelishia Pillay
#  * <p>
#  * 7 October 2016
#  

#
#  * This class provides an interpreter for the evolved heuristic.
#
from typing import List

from GeneticProgram.Node import Node


class Evaluator(object):
    #
    #  * Data elements 
    # 
    #      * The array stores the values of the problem attributes.
    #      
    attribute_values: List[float]

    # 
    #      * Stores the characters for each of the attributes.
    #      
    attributes: str

    # 
    #      * Constructor for the class which stores the attributes and attribute values.
    #      * @param attributes A string containing characters representing the attributes
    #      * for the problem
    #      * @param attribute_values An array of real numbers representing the attribute
    #      * values.
    #      
    def __init__(self, attributes, attribute_values):
        #
        #          * Constructor that stores the attributes and attributes values
        #          * for the problem.
        #          
        self.attribute_values = attribute_values
        self.attributes = attributes

    # 
    #      * This method interprets a heuristic of type <tt>Node</tt> and calculates its
    #      * corresponding real value.
    #      * @param op The evolved heuristic to be interpreted.
    #      * @return Returns the real the heuristic evaluates to.
    #      
    def eval(self, op: Node):
        if op.get_arity() == 0:
            return self.get_attribute_value(op.get_label())
        elif op.get_label() == "if":
            cond = op.get_child(0)
            if self.eval(cond) == 1:
                return self.eval(op.get_child(1))
            else:
                return self.eval(op.get_child(2))
        else:
            args = [None] * op.get_arity()
            count = 0
            while count < op.get_arity():
                args[count] = self.eval(op.get_child(count))
                count += 1
            return self.calculate(op.get_label(), args)

    #
    #          * This method applies each operator in the heuristic.
    #
    @staticmethod
    def calculate(op, args):
        if op == "+":
            return args[0] + args[1]
        elif op == "-":
            return args[0] - args[1]
        elif op == "*":
            return args[0] * args[1]
        elif op == "/":
            if args[1] == 0:
                return 1
            else:
                return int(args[0]) / args[1]
        elif op == "<":
            if args[0] < args[1]:
                return 1
            else:
                return 0
        elif op == ">":
            if args[0] > args[1]:
                return 1
            else:
                return 0
        elif op == "==":
            if args[0] == args[1]:
                return 1
            else:
                return 0
        elif op == "!=":
            if args[0] != args[1]:
                return 1
            else:
                return 0
        elif op == ">=":
            if args[0] >= args[1]:
                return 1
            else:
                return 0
        elif op == "and":
            if args[0] == 1 and args[1] == 1:
                return 1
            else:
                return 0
        else:
            if args[0] <= args[1]:
                return 1
            else:
                return 0

    def get_attribute_value(self, attribute):
        #
        #          * Returns the attribute value corresponding to the terminal node.
        #          
        return self.attribute_values[self.attributes.index(attribute)]
