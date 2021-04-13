#
#  * This class defines the evaluator to evaluate the heuristic given the values
#  * of the attributes.
#  * <p>
#  * Nelishia Pillay
#  * <p>
#  * 7 October 2016
#  
# package: genprog

#
#  * This class provides an interpreter for the evolved heuristic.
#
from typing import List

from GenProg.Node import Node


class Evaluator(object):
    #
    #  * Data elements 
    # 
    #      * The array stores the values of the problem attributes.
    #      
    attributeVals: List[float]

    # 
    #      * Stores the characters for each of the attributes.
    #      
    attributes: str

    # 
    #      * Constructor for the class which stores the attributes and attribute values.
    #      * @param attributes A string containing characters representing the attributes
    #      * for the problem
    #      * @param attributeVals An array of real numbers representing the attribute
    #      * values.
    #      
    def __init__(self, attributes, attributeVals):
        #
        #          * Constructor that stores the attributes and attributes values
        #          * for the problem.
        #          
        self.attributeVals = attributeVals
        self.attributes = attributes

    # 
    #      * This method interprets a heuristic of type <tt>Node</tt> and calculates its
    #      * corresponding real value.
    #      * @param op The evolved heuristic to be interpreted.
    #      * @return Returns the real the heuristic evaluates to.
    #      
    def eval(self, op: Node):
        if op.getArity() == 0:
            return self.getVal(op.getLabel())
        elif op.getLabel() == "if":
            cond = op.getChild(0)
            if self.eval(cond) == 1:
                return self.eval(op.getChild(1))
            else:
                return self.eval(op.getChild(2))
        else:
            args = [None] * op.getArity()
            count = 0
            while count < op.getArity():
                # print('op', op.getArity(), op.getLabel(), len(op.getChildren()), count)
                args[count] = self.eval(op.getChild(count))
                count += 1
            # endfor_count
            return self.calc(op.getLabel(), args)
        # endelse

    def calc(self, op, args):
        #
        #          * This method applies each operator in the heuristic.
        #          
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

    def getVal(self, attribute):
        #
        #          * Returns the attribute value corresponding to the terminal node.
        #          
        return self.attributeVals[self.attributes.index(attribute)]
