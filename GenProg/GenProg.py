#
#  * This class implements a genetic programming generation constructive
#  * hyper-heuristic for generating new low-level construction heuristics for a
#  * problem domain.
#  * <p>
#  * Nelishia Pillay
#  * 11 September 2016
#  
# package: genprog

# 
#  *This class implements a genetic programming generation constructive 
#  *hyper-heuristic for generating new low-level construction heuristics for a 
#  *problem domain.
#
import math
from random import Random
from typing import List

from GenProg.Node import Node
from GenProg.Problem import Problem
from GenProg.Solution import Solution


class GenProg(object):
    # Data elements
    # 
    #      *Stores a string of characters with each character representing a problem
    #      *attribute that will form elements of the terminal set.
    #      
    attributes: str

    # Stores the operators.These include the arithmetic operators, "if" operator and
    #      *arithmetic logical operators:+, -, * and /. The / is protected division which
    #      *returns a value of 1 if the denominator is 0. The if operator performs the
    #      *function of the standard if-then-else operator and takes 3 arguments.
    #      *Relational operators first argument of the "if" operator: <, >, ==, //!=, >=,
    #      *<=.
    #      
    opts: List[str]

    # 
    #      * Stores the problem domain.
    #      
    problem: Problem

    # 
    #      * Stores the arithmetic operators only.
    #      
    arithOpts: List[str]

    # 
    #      * Stores the relational operators only.
    #      
    relOpts: List[str]

    # 
    #      * Stores the terminals. Each character in the string "attributes" is stored as
    #      * a terminal.
    #      
    terms: List[str]

    # Stores the terminals
    # 
    #      * Stores the maximum depth of the trees created during initial population
    #      * generation.
    #      
    maxDepth: int

    # 
    #      * Stores the maximum depth of the offspring created by the genetic operators.
    #      
    offspringDepth: int

    # 
    #      * Stores the depth of the subtree created by the mutation operator.
    #      
    mutationDepth: int

    # 
    #      * Stores the population size.
    #      
    populationSize: int

    # 
    #      * Stores the number generations.
    #      
    noOfGenerations: int

    # 
    #      * Stores the reproduction application rate.
    #      
    reproductionRate: float

    # 
    #      * Stores the mutation application rate.
    #      
    mutationRate: float

    # 
    #      * Stores the crossover application rate.
    #      
    crossoverRate: float

    # 
    #      * Stores the tournament size.
    #      
    tournamentSize: int

    # Stores the tournament size
    # 
    #      * Stores the population of heuristics.
    #      
    population: List[Solution]

    # 
    #      * Stores the seed for the random number generator.
    #      
    seed: int

    # 
    #      * Stores an instance of the random number generator.
    #      
    ranGen: Random

    # 
    #      * A temporary pointer used by the mutation operator.
    #      
    curPoint: int

    # 
    #      * A temporary pointer used by the crossover operator.
    #      
    crPoint: int

    # 
    #      * Temporary node instance used by the crossover operator.
    #      
    crChild: Node

    # 
    #      * Temporary position variable used by the crossover operator.
    #      
    crPos: int

    # 
    #      * Stores an integer value indicating whether an arithmetic function or rule
    #      * should be evolved. A value of 0 indicates and arithmetic function a value of
    #      * 1 indicates a rule.
    #      
    heuType: int

    # Is a flag used to determine whether output for each generation must be printed
    # to the screen or not. If it is set to true output is printed. If it is set to
    # false output is not printed. The default is true;
    print_: bool

    # 
    #      * Constructor to set the random number generator seed, attributes and type of
    #      * heuristic.The random number generator is initialized in the constructor.
    #      * @param seed A long value specifying the seed to be used for the random number
    #      * generator.
    #      * @param attributes A string composed of characters, each representing an
    #      * attribute for the problem.
    #      * @param heuType An integer value indicating whether an arithmetic function or
    #      * rule should be evolved. A value of 0 indicates and arithmetic function a value
    #      * of 1 indicates a rule.
    #      
    def __init__(self, seed, attributes, heuType):
        self.seed = seed
        self.ranGen = Random(seed)
        self.attributes = attributes
        self.heuType = heuType
        self.print_ = True
        self.setOpts()
        self.setTerms()

    # Methods for setting parameter values for the genetic algorithm
    #
    #      * Reads the parameters from a file and stores them as data element.
    #      * @param parameterFile The name of the file the parameter values are stored
    #      * in.
    #      
    def setParameters(self, parameterFile):
        try:
            # Initialise input stream to read from a file
            with open(parameterFile, 'r') as f:
                self.populationSize = int(f.readline())
                self.tournamentSize = int(f.readline())
                self.noOfGenerations = int(f.readline())
                self.mutationRate = float(f.readline())
                self.crossoverRate = float(f.readline())
                self.maxDepth = int(f.readline())
                self.offspringDepth = int(f.readline())
                self.mutationDepth = int(f.readline())
        except IOError as ioe:
            print("The file " + parameterFile + " cannot be found. " + "Please check the details provided.", ioe)

    # 
    #      * This method sets the string of characters, each representing a low-level
    #      * heuristic for the problem domain.
    #      * @param problem Is an instance of <tt>ProblemDomain</tt> which defines the
    #      * problem domain.
    #      
    def setProblem(self, problem):
        self.problem = problem

    # 
    #      * Sets the number of generations size.
    #      * @param noOfGenerations Parameter value for the number of generations.
    #      
    def setNoOfGenerations(self, noOfGenerations):
        self.noOfGenerations = noOfGenerations

    # 
    #      * Sets the maximum permitted depth for the new subtree created by mutation
    #      * to be inserted at a randomly selected point in a copy of the parent.
    #      * @param mutationDepth Parameter value specifying the maximum depth of the
    #      * subtree created by mutation.
    #      
    def setmutationDepth(self, mutationDepth):
        self.mutationDepth = mutationDepth

    # 
    #      *
    #      * @return The population size.
    #      
    def getPopulationSize(self):
        return self.populationSize

    # 
    #      * Sets the population size.
    #      * @param populationSize Parameter value for the population size.
    #      
    def setPopulationSize(self, populationSize):
        self.populationSize = populationSize

    # 
    #      *
    #      * @return The tournament size.
    #      
    def getTournamentSize(self):
        return self.tournamentSize

    #
    #      * Sets the tournament size.
    #      * @param tournamentSize Parameter value for the tournament size.
    #      
    def setTournamentSize(self, tournamentSize):
        #
        #          * Sets the tournament size.
        #          
        self.tournamentSize = tournamentSize

    # 
    #      *
    #      * @return Returns the number of generations.
    #      
    def getnoOfGenerations(self):
        return self.noOfGenerations

    # 
    #      *
    #      * @return Returns the mutation rate.
    #      
    def getMutationRate(self):
        return self.mutationRate

    # 
    #      * Sets the mutation rate.
    #      * @param mutationRate Parameter value for the mutation rate. The value must be
    #      * a fraction, e.g. 0.5.
    #      
    def setMutationRate(self, mutationRate):
        self.mutationRate = mutationRate

    # 
    #      *
    #      * @return Returns the crossover rate.
    #      
    def getCrossoverRate(self):
        return self.crossoverRate

    # 
    #      * Sets the crossover rate.
    #      * @param crossoverRate Parameter value for the crossover rate. The value must
    #      * be a fraction, e.g. 0.5.
    #      
    def setCrossoverRate(self, crossoverRate):
        self.crossoverRate = crossoverRate

    # 
    #      *
    #      * @return Returns the reproduction rate.
    #      
    def getReproductionRate(self):
        return self.reproductionRate

    # 
    #      *
    #      * @return Returns the  maximum depth permitted for parse trees representing
    #      * heuristics created in the initial population.
    #      
    def getMaxDepth(self):
        return self.maxDepth

    #
    #      * Sets the maximum depth of a parse tree in the initial population.
    #      * @param maxDepth Parameter value specifying the maximum depth
    #      * permitted for heuristics created in the initial population.
    #      
    def setMaxDepth(self, maxDepth):
        self.maxDepth = maxDepth

    #
    #      *
    #      * @return Returns the maximum offspring depth.
    #      
    def getOffspringDepth(self):
        return self.offspringDepth

    #
    #      * Sets the maximum depth of the offspring produced by the genetic operators.If
    #      * the offspring size exceeds this depth the function nodes at the maximum depth
    #      * are replaced by randomly selected terminals.
    #      * @param offspringDepth Parameter value specifying the maximum depth
    #      * permitted for offspring created by the mutation and crossover operators.
    #      
    def setOffspringDepth(self, offspringDepth):
        self.offspringDepth = offspringDepth

    #
    #      *
    #      * @return Returns the mutation depth.
    #      
    def getMutationDepth(self):
        return self.mutationDepth

    #
    #      *
    #      * @return Returns the value of the flag print used to determine whether to
    #      * print output to the screen.
    #      
    def getPrint(self):
        return self.print_

    #
    #      * Sets the flag for printing output for each generation to the screen.If it is
    #      * set to <tt>true</tt> output is printed.If it is set to <tt>false</tt> output
    #      * is not printed.The default is <tt>true</tt>.The output printed to the screen
    #      * is the best heuristic combination for each generation and its fitness as well
    #      * as the best fitness obtained thus far in the run.
    #      * @param print A value of false or true indicating whether output for each
    #      * generation must be printed to the screen or not.
    #      
    def setPrint(self, print_):
        self.print_ = print_

    def setOpts(self):
        #
        #          * Sets the three function arrays opts, arithOpts and relOpts.
        #          
        # Store all the operators.
        self.opts = ['+', '-', '*', '/']
        if self.heuType == 1:
            self.opts.append('if')
        # Stores the arithmetic operators.
        self.arithOpts = ['+', '-', '*', '/']
        # Stores the relationaloperators.

        self.relOpts = ['<', '>', '==', '!=', '<=', '>=']

    def setTerms(self):
        #
        #          * Stores the terminals from the attributes string. Each character in
        #          * "attributes" is an element of the terminal set. 
        # Initialize array.
        # Store attributes as terminals.
        count = 0
        self.terms = []
        while count < len(self.attributes):
            self.terms.append(self.attributes[count])
            count += 1
        # EndForCount

    def displayFunctionSet(self):
        # Displays the function set.
        print(' '.join(self.opts))

    def displayTerminalSet(self):
        # Displays the function set.
        print(' '.join(self.terms))

    def prune(self, root: Node, currDepth):
        if currDepth == self.offspringDepth or (root.getLabel() == 'if' and currDepth == (self.offspringDepth - 1)):
            if root.getArity() > 0:
                root.setLabel(self.ranGen.choice(self.terms))
                root.setArity(0)
                root.clearChildren()
        else:
            currDepth += 1
            i = 0
            while i < root.getArity():
                self.prune(root.getChild(i), currDepth)
                i += 1
        # return root;

    def printInd(self, root):
        print(root.getLabel())
        count = 0
        while count < root.getArity():
            # if(root.get_child(count)!=null)
            self.printInd(root.getChild(count))
            count += 1
        # endfor_count

    def displayPop(self):
        count = 0
        while len(self.population):
            self.population[count].getHeuristic().prefix()
            print(self.population[count].getFitness())
            count += 1

    def createPop(self):
        self.population = [None] * self.populationSize
        best = None
        count = 0
        while count < self.populationSize:
            prog = self.create()
            self.population[count] = self.problem.evaluate(prog)
            self.population[count].setHeuristic(prog)
            if count == 0:
                best = self.population[count]
            elif self.population[count].fitter(best) == 1:
                best = self.population[count]
            count += 1
        return best

    def create(self):
        root = Node()

        root.setLabel(self.ranGen.choice(self.opts))
        if root.getLabel() == "if":
            root.setArity(3)
        else:
            root.setArity(2)
        return self.createInd(root, 1)

    def createInd(self, parent: Node, curDep: int):
        curDep += 1
        count = 0
        while count < parent.getArity():
            child = Node()
            if curDep == self.maxDepth:
                child.setLabel(self.ranGen.choice(self.terms))
                child.setArity(0)
            else:
                if parent.getLabel() == "if" and count == 0:
                    child.setLabel(self.ranGen.choice(self.relOpts))
                    child.setArity(2)
                else:
                    if self.ranGen.random() > 0.5:
                        child.setLabel(self.ranGen.choice(self.terms))
                        child.setArity(0)
                    else:
                        if (self.maxDepth - curDep) < 2:
                            child.setLabel(self.ranGen.choice(self.arithOpts))
                        else:
                            child.setLabel(self.ranGen.choice(self.opts))
                        if child.getLabel() == "if":
                            child.setArity(3)
                        else:
                            child.setArity(2)
            # print('    '*(curDep-2) + 'parent', parent.getLabel(), parent.getArity(), 'count', count, 'child', child.getLabel(), child.getArity())
            parent.addChild(self.createInd(child, curDep))
            count += 1
        return parent

    def mutation(self, parent):
        par = parent.getHeuristic()
        mpoint = self.ranGen.randrange(self.getSize(par))
        if mpoint == 0:
            prog = self.create()
        else:
            prog = self.mprog(parent.getHeuristic(), mpoint)
        if self.offspringDepth > 0:
            self.prune(prog, 0)
        offspring = self.problem.evaluate(prog)
        offspring.setHeuristic(prog)
        return offspring

    def mprog(self, parent, point):
        new_prog = Node()
        self.curPoint = 0
        new_prog.setLabel(parent.getLabel())
        new_prog.setArity(parent.getArity())
        offspring = self.mutate(new_prog, parent, point)
        return offspring

    def mutate(self, oprog, pprog, mpoint):
        count = 0
        while count < oprog.getArity():
            self.curPoint += 1
            if mpoint == self.curPoint:
                if self.ranGen.random() > 0.5:
                    child = Node()
                    child.setLabel(self.ranGen.choice(self.terms))
                    child.setArity(0)
                else:
                    if oprog.getLabel() is "if" and count == 0:
                        child = self.mcreate(True)
                    else:
                        child = self.mcreate(False)
                oprog.addChild(child)
            else:
                child = Node()
                child.setLabel(pprog.getChild(count).getLabel())
                child.setArity(pprog.getChild(count).getArity())
                oprog.addChild(self.mutate(child, pprog.getChild(count), mpoint))
            count += 1
        return oprog

    def mcreate(self, isif):
        root = Node()
        if isif:
            isif = False
            root.setLabel(self.ranGen.choice(self.relOpts))
        elif self.mutationDepth <= 2:
            root.setLabel(self.ranGen.choice(self.arithOpts))
        else:
            root.setLabel(self.ranGen.choice(self.opts))
        if root.getLabel() == "if":
            root.setArity(3)
        else:
            root.setArity(2)
        return self.mcreateInd(root, 1, isif)

    def mcreateInd(self, parent, cur_dep, isif):
        cur_dep += 1
        count = 0
        while count < parent.getArity():
            child = Node()
            if cur_dep == self.mutationDepth:
                child.setLabel(self.ranGen.choice(self.terms))
                child.setArity(0)
            else:
                if isif or (parent.getLabel() == "if" and count == 0):
                    child.setLabel(self.ranGen.choice(self.relOpts))
                    child.setArity(2)
                    if isif:
                        isif = False
                else:
                    if self.ranGen.random():
                        child.setLabel(self.ranGen.choice(self.terms))
                        child.setArity(0)
                    else:
                        if (self.mutationDepth - cur_dep) < 2:
                            child.setLabel(self.ranGen.choice(self.arithOpts))
                        else:
                            child.setLabel(self.ranGen.choice(self.opts))
                        if child.getLabel() == "if":
                            child.setArity(3)
                        else:
                            child.setArity(2)
            parent.addChild(self.mcreateInd(child, cur_dep, isif))
            count += 1
        return parent

    def getSize(self, root):
        size = 1
        count = 0
        while count < root.getArity():
            size += self.getSize(root.getChild(count))
            count += 1
        return size

    def crossover(self, parent1, parent2):
        p1 = parent1.getHeuristic()
        p2 = parent2.getHeuristic()
        c1 = self.getSize(p1)
        c2 = self.getSize(p2)
        cp1 = self.ranGen.randrange(c1)
        cp2 = self.ranGen.randrange(c2)
        o1 = Node()
        o1.setLabel(p1.getLabel())
        o1.setArity(p1.getArity())
        if cp1 == 0:
            sp1 = self.copy(p1, o1)
        else:
            self.crPoint = 0
            self.getChild(cp1, p1)
            sp1 = self.crChild
        o2 = Node()
        o2.setLabel(p2.getLabel())
        o2.setArity(p2.getArity())
        if cp2 == 0:
            sp2 = self.copy(p2, o2)
        else:
            self.crPoint = 0
            self.getChild(cp2, p2)
            self.crPoint = 0
            sp2 = self.crChild

        if cp1 == 0:
            o1 = sp2
        else:
            self.crPoint = 0
            o1 = self.subprog(cp1, p1, o1, sp2)

        if self.offspringDepth > 0:
            self.prune(o1, 0)

        if cp2 == 0:
            o2 = sp1
        else:
            self.crPoint = 0
            o2 = self.subprog(cp2, p2, o2, sp1)

        if self.offspringDepth > 0:
            self.prune(o2, 0)

        offspring: List[Solution] = [self.problem.evaluate(o1), self.problem.evaluate(o2)]
        offspring[0].setHeuristic(o1)
        offspring[1].setHeuristic(o2)
        return offspring

    def copy(self, parent, cparent):
        count = 0
        while count < parent.getArity():
            child = parent.getChild(count)
            c = Node()
            c.setLabel(child.getLabel())
            c.setArity(child.getArity())
            c = self.copy(child, c)
            cparent.addChild(c, count)
            count += 1
        return cparent

    def getChild(self, cp, parent):
        fin = False
        count = 0
        while not fin and count < parent.getArity():
            self.crPoint += 1
            child = parent.getChild(count)
            if self.crPoint == cp:
                self.crChild = child
                fin = True
            else:
                self.getChild(cp, child)
            count += 1

    def subprog(self, cp, parent, cparent, sp):
        count = 0
        while count < parent.getArity():
            self.crPoint += 1
            child = parent.getChild(count)
            if self.crPoint != cp:
                c = Node()
                c.setLabel(child.getLabel())
                c.setArity(child.getArity())
                c = self.subprog(cp, child, c, sp)
                cparent.addChild(c, count)
            else:
                cparent.addChild(sp, count)
            count += 1
        return cparent

    def selection(self):
        best = self.ranGen.choice(self.population)
        count = 2
        while count <= self.tournamentSize:
            current = self.ranGen.choice(self.population)
            if current.fitter(best) == 1:
                best = current
            count += 1
        return best

    def regenerate(self, best):
        mtimes = int(math.floor(self.populationSize * self.mutationRate))
        ctimes = int(math.floor(self.populationSize * self.crossoverRate)) / 2
        if (mtimes + ctimes * 2) < self.populationSize:
            mtimes += self.populationSize - (mtimes + ctimes * 2)
        npop: List[Solution] = [None] * self.populationSize
        index = 0
        mc = 1
        while mc <= mtimes:
            npop[index] = self.mutation(self.selection())
            if npop[index].fitter(best) == 1:
                best = npop[index]
            index += 1
            mc += 1
        cc = 1
        while cc <= ctimes:
            ctemp = self.crossover(self.selection(), self.selection())
            npop[index] = ctemp[0]
            index += 1
            if ctemp[0].fitter(best) == 1:
                best = ctemp[0]
            npop[index] = ctemp[1]
            index += 1
            if ctemp[1].fitter(best) == 1:
                best = ctemp[1]
            cc += 1
        self.population = npop
        return best

    def evolve(self):
        if self.print_:
            print("Generation 0")
        best = self.createPop()
        if self.print_:
            self.printInd(best.getHeuristic())
            print("", best.getFitness())
            print()
        count = 1
        while count <= self.noOfGenerations:
            if self.print_:
                print("Generation", count)
            prog = self.regenerate(best)
            if prog.fitter(best) == 1:
                best = prog
            if self.print_:
                self.printInd(best.getHeuristic())
                print("", best.getFitness())
                print()
            count += 1
        print("Completed evolving heuristic")
        return best
