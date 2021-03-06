#
#  * This class implements a genetic programming generation constructive
#  * hyper-heuristic for generating new low-level construction heuristics for a
#  * problem domain.
#  * <p>
#  * Nelishia Pillay
#  * 11 September 2016
#  

# 
#  *This class implements a genetic programming generation constructive 
#  *hyper-heuristic for generating new low-level construction heuristics for a 
#  *problem domain.
#
import math
from random import Random
from typing import List

from GeneticProgram.Node import Node
from GeneticProgram.Problem import Problem
from GeneticProgram.Solution import Solution


class GeneticProgram(object):
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
    operators: List[str]

    # 
    #      * Stores the problem domain.
    #      
    problem: Problem

    # 
    #      * Stores the arithmetic operators only.
    #      
    arithmetic_operators: List[str]

    # 
    #      * Stores the relational operators only.
    #      
    relational_operators: List[str]

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
    max_depth: int

    # 
    #      * Stores the maximum depth of the offspring created by the genetic operators.
    #      
    offspring_depth: int

    # 
    #      * Stores the depth of the subtree created by the mutation operator.
    #      
    mutation_depth: int

    # 
    #      * Stores the population size.
    #      
    population_size: int

    # 
    #      * Stores the number generations.
    #      
    no_of_generations: int

    # 
    #      * Stores the reproduction application rate.
    #      
    reproduction_rate: float

    # 
    #      * Stores the mutation application rate.
    #      
    mutation_rate: float

    # 
    #      * Stores the crossover application rate.
    #      
    crossover_rate: float

    # 
    #      * Stores the tournament size.
    #      
    tournament_size: int

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
    random_generator: Random

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
    heuristic_type: int

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
    def __init__(self, seed, attributes, heuristic_type):
        self.seed = seed
        self.random_generator = Random(seed)
        self.attributes = attributes
        self.heuristic_type = heuristic_type
        self.print_ = True
        self.set_operators()
        self.set_terms()

    def clone(self) -> 'GeneticProgram':
        gen_program = GeneticProgram(self.seed, self.attributes, self.heuristic_type)
        gen_program.population_size = self.population_size
        gen_program.tournament_size = self.tournament_size
        gen_program.no_of_generations = self.no_of_generations
        gen_program.mutation_rate = self.mutation_rate
        gen_program.crossover_rate = self.crossover_rate
        gen_program.max_depth = self.max_depth
        gen_program.offspring_depth = self.offspring_depth
        gen_program.mutation_depth = self.mutation_depth
        gen_program.problem = self.problem
        return gen_program

    # Methods for setting parameter values for the genetic algorithm
    #
    #      * Reads the parameters from a file and stores them as data element.
    #      * @param parameterFile The name of the file the parameter values are stored
    #      * in.
    #      
    def set_parameters(self, parameter_file):
        try:
            # Initialise input stream to read from a file
            with open(parameter_file, 'r') as f:
                self.population_size = int(f.readline())
                self.tournament_size = int(f.readline())
                self.no_of_generations = int(f.readline())
                self.mutation_rate = float(f.readline())
                self.crossover_rate = float(f.readline())
                self.max_depth = int(f.readline())
                self.offspring_depth = int(f.readline())
                self.mutation_depth = int(f.readline())
        except IOError as ioe:
            print("The file " + parameter_file + " cannot be found. " + "Please check the details provided.", ioe)

    # 
    #      * This method sets the string of characters, each representing a low-level
    #      * heuristic for the problem domain.
    #      * @param problem Is an instance of <tt>ProblemDomain</tt> which defines the
    #      * problem domain.
    #      
    def set_problem(self, problem):
        self.problem = problem

    # 
    #      * Sets the number of generations size.
    #      * @param noOfGenerations Parameter value for the number of generations.
    #      
    def set_no_of_generations(self, no_of_generations):
        self.no_of_generations = no_of_generations

    # 
    #      * Sets the maximum permitted depth for the new subtree created by mutation
    #      * to be inserted at a randomly selected point in a copy of the parent.
    #      * @param mutationDepth Parameter value specifying the maximum depth of the
    #      * subtree created by mutation.
    #      
    def set_mutation_depth(self, mutation_depth):
        self.mutation_depth = mutation_depth

    # 
    #      *
    #      * @return The population size.
    #      
    def get_population_size(self):
        return self.population_size

    # 
    #      * Sets the population size.
    #      * @param populationSize Parameter value for the population size.
    #      
    def set_population_size(self, population_size):
        self.population_size = population_size

    # 
    #      *
    #      * @return The tournament size.
    #      
    def get_tournament_size(self):
        return self.tournament_size

    #
    #      * Sets the tournament size.
    #      * @param tournamentSize Parameter value for the tournament size.
    #      
    def set_tournament_size(self, tournament_size):
        #
        #          * Sets the tournament size.
        #          
        self.tournament_size = tournament_size

    # 
    #      *
    #      * @return Returns the number of generations.
    #      
    def get_no_of_generations(self):
        return self.no_of_generations

    # 
    #      *
    #      * @return Returns the mutation rate.
    #      
    def get_mutation_rate(self):
        return self.mutation_rate

    # 
    #      * Sets the mutation rate.
    #      * @param mutationRate Parameter value for the mutation rate. The value must be
    #      * a fraction, e.g. 0.5.
    #      
    def set_mutation_rate(self, mutation_rate):
        self.mutation_rate = mutation_rate

    # 
    #      *
    #      * @return Returns the crossover rate.
    #      
    def get_crossover_rate(self):
        return self.crossover_rate

    # 
    #      * Sets the crossover rate.
    #      * @param crossoverRate Parameter value for the crossover rate. The value must
    #      * be a fraction, e.g. 0.5.
    #      
    def set_crossover_rate(self, crossover_rate):
        self.crossover_rate = crossover_rate

    # 
    #      *
    #      * @return Returns the reproduction rate.
    #      
    def get_reproduction_rate(self):
        return self.reproduction_rate

    # 
    #      *
    #      * @return Returns the  maximum depth permitted for parse trees representing
    #      * heuristics created in the initial population.
    #      
    def get_max_depth(self):
        return self.max_depth

    #
    #      * Sets the maximum depth of a parse tree in the initial population.
    #      * @param maxDepth Parameter value specifying the maximum depth
    #      * permitted for heuristics created in the initial population.
    #      
    def set_max_depth(self, max_depth):
        self.max_depth = max_depth

    #
    #      *
    #      * @return Returns the maximum offspring depth.
    #      
    def get_offspring_depth(self):
        return self.offspring_depth

    #
    #      * Sets the maximum depth of the offspring produced by the genetic operators.If
    #      * the offspring size exceeds this depth the function nodes at the maximum depth
    #      * are replaced by randomly selected terminals.
    #      * @param offspringDepth Parameter value specifying the maximum depth
    #      * permitted for offspring created by the mutation and crossover operators.
    #      
    def set_offspring_depth(self, offspring_depth):
        self.offspring_depth = offspring_depth

    #
    #      *
    #      * @return Returns the mutation depth.
    #      
    def get_mutation_depth(self):
        return self.mutation_depth

    #
    #      *
    #      * @return Returns the value of the flag print used to determine whether to
    #      * print output to the screen.
    #      
    def get_print(self):
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
    def set_print(self, print_):
        self.print_ = print_

    def set_operators(self):
        #
        #          * Sets the three function arrays opts, arithOpts and relOpts.
        #          
        # Store all the operators.
        self.operators = ['+', '-', '*', '/']
        if self.heuristic_type == 1:
            self.operators.append('if')
        # Stores the arithmetic operators.
        self.arithmetic_operators = ['+', '-', '*', '/']
        # Stores the relational operators.

        self.relational_operators = ['<', '>', '==', '!=', '<=', '>=']

    def set_terms(self):
        #
        #          * Stores the terminals from the attributes string. Each character in
        #          * "attributes" is an element of the terminal set. 
        # Initialize array.
        # Store attributes as terminals.
        self.terms = []
        for i in range(len(self.attributes)):
            self.terms.append(self.attributes[i])

    def display_function_set(self):
        # Displays the function set.
        print(' '.join(self.operators))

    def display_terminal_set(self):
        # Displays the function set.
        print(' '.join(self.terms))

    def prune(self, root: Node, current_depth):
        if current_depth == self.offspring_depth or (
                root.get_label() == 'if' and current_depth == (self.offspring_depth - 1)):
            if root.get_arity() > 0:
                root.set_label(self.random_generator.choice(self.terms))
                root.set_arity(0)
                root.clear_children()
        else:
            current_depth += 1
            for i in range(root.get_arity()):
                self.prune(root.get_child(i), current_depth)
        # return root;

    def print_labels(self, root):
        print(root.get_label())
        for i in range(root.get_arity()):
            self.print_labels(root.get_child(i))

    def display_population(self):
        for solution in self.population:
            solution.get_heuristic().prefix()
            print(solution.get_fitness())

    def create_population(self):
        self.population = []
        best = None
        for i in range(self.population_size):
            program = self.create()
            new_solution = self.problem.evaluate(program)
            self.population.append(new_solution)
            new_solution.set_heuristic(program)
            if i == 0:
                best = new_solution
            elif new_solution.fitter(best) == 1:
                best = new_solution
        return best

    def create(self) -> Node:
        root = Node()

        root.set_label(self.random_generator.choice(self.operators))
        if root.get_label() == "if":
            root.set_arity(3)
        else:
            root.set_arity(2)
        return self.create_node_with_children(root, 1)

    def create_node_with_children(self, parent: Node, current_depth: int) -> Node:
        current_depth += 1
        for i in range(parent.get_arity()):
            child = Node()
            if current_depth == self.max_depth:
                child.set_label(self.random_generator.choice(self.terms))
                child.set_arity(0)
            else:
                if parent.get_label() == "if" and i == 0:
                    child.set_label(self.random_generator.choice(self.relational_operators))
                    child.set_arity(2)
                else:
                    if self.random_generator.random() > 0.5:
                        child.set_label(self.random_generator.choice(self.terms))
                        child.set_arity(0)
                    else:
                        self.set_node_label_and_arity(child, current_depth, self.max_depth)
            parent.add_child(self.create_node_with_children(child, current_depth))
        return parent

    def mutation(self, parent: Solution) -> Solution:
        parent_heuristic: Node = parent.get_heuristic()
        mutation_point = self.random_generator.randrange(self.get_size_of_node(parent_heuristic))
        if mutation_point == 0:
            program = self.create()
        else:
            program = self.mutate_parent_at_point(parent_heuristic, mutation_point)
        if self.offspring_depth > 0:
            self.prune(program, 0)
        offspring: Solution = self.problem.evaluate(program)
        offspring.set_heuristic(program)
        return offspring

    def mutate_parent_at_point(self, parent: Node, mutation_point: int) -> Node:
        new_node: Node = Node()
        self.curPoint = 0
        new_node.set_label(parent.get_label())
        new_node.set_arity(parent.get_arity())
        offspring = self.mutate(new_node, parent, mutation_point)
        return offspring

    def mutate(self, offspring_node: Node, parent_node: Node, mutation_point: int) -> Node:
        for i in range(offspring_node.get_arity()):
            self.curPoint += 1
            if mutation_point == self.curPoint:
                if self.random_generator.random() > 0.5:
                    child = Node()
                    child.set_label(self.random_generator.choice(self.terms))
                    child.set_arity(0)
                else:
                    if offspring_node.get_label() == "if" and i == 0:
                        child = self.create_new_node_for_mutation(True)
                    else:
                        child = self.create_new_node_for_mutation(False)
                offspring_node.add_child(child)
            else:
                child = Node()
                child.set_label(parent_node.get_child(i).get_label())
                child.set_arity(parent_node.get_child(i).get_arity())
                offspring_node.add_child(self.mutate(child, parent_node.get_child(i), mutation_point))
        return offspring_node

    def create_new_node_for_mutation(self, is_if):
        root = Node()
        if is_if:
            is_if = False
            root.set_label(self.random_generator.choice(self.relational_operators))
        elif self.mutation_depth <= 2:
            root.set_label(self.random_generator.choice(self.arithmetic_operators))
        else:
            root.set_label(self.random_generator.choice(self.operators))
        if root.get_label() == "if":
            root.set_arity(3)
        else:
            root.set_arity(2)
        return self.create_new_node_for_mutation_recursive(root, 1, is_if)

    def create_new_node_for_mutation_recursive(self, parent, current_depth, is_if):
        current_depth += 1
        for i in range(parent.get_arity()):
            child = Node()
            if current_depth == self.mutation_depth:
                child.set_label(self.random_generator.choice(self.terms))
                child.set_arity(0)
            else:
                if is_if or (parent.get_label() == "if" and i == 0):
                    child.set_label(self.random_generator.choice(self.relational_operators))
                    child.set_arity(2)
                    if is_if:
                        is_if = False
                else:
                    if self.random_generator.random():
                        child.set_label(self.random_generator.choice(self.terms))
                        child.set_arity(0)
                    else:
                        self.set_node_label_and_arity(child, current_depth, self.mutation_depth)
            parent.add_child(self.create_new_node_for_mutation_recursive(child, current_depth, is_if))
        return parent

    def set_node_label_and_arity(self, child, current_depth, depth):
        if (depth - current_depth) < 2:
            child.set_label(self.random_generator.choice(self.arithmetic_operators))
        else:
            child.set_label(self.random_generator.choice(self.operators))
        if child.get_label() == "if":
            child.set_arity(3)
        else:
            child.set_arity(2)

    def get_size_of_node(self, root):
        size = 1
        for i in range(root.get_arity()):
            size += self.get_size_of_node(root.get_child(i))
        return size

    def crossover(self, parent1, parent2):
        heuristic1: Node = parent1.get_heuristic()
        heuristic2: Node = parent2.get_heuristic()
        heuristic_length1 = self.get_size_of_node(heuristic1)
        heuristic_length2 = self.get_size_of_node(heuristic2)
        crossover_point1 = self.random_generator.randrange(heuristic_length1)
        crossover_point2 = self.random_generator.randrange(heuristic_length2)
        offspring1 = Node()
        offspring1.set_label(heuristic1.get_label())
        offspring1.set_arity(heuristic1.get_arity())
        if crossover_point1 == 0:
            sp1 = self.copy(heuristic1, offspring1)
        else:
            self.crPoint = 0
            self.get_child(crossover_point1, heuristic1)
            sp1 = self.crChild
        offspring2 = Node()
        offspring2.set_label(heuristic2.get_label())
        offspring2.set_arity(heuristic2.get_arity())
        if crossover_point2 == 0:
            sp2 = self.copy(heuristic2, offspring2)
        else:
            self.crPoint = 0
            self.get_child(crossover_point2, heuristic2)
            self.crPoint = 0
            sp2 = self.crChild

        if crossover_point1 == 0:
            offspring1 = sp2
        else:
            self.crPoint = 0
            offspring1 = self.add_sub_program(crossover_point1, heuristic1, offspring1, sp2)

        if self.offspring_depth > 0:
            self.prune(offspring1, 0)

        if crossover_point2 == 0:
            offspring2 = sp1
        else:
            self.crPoint = 0
            offspring2 = self.add_sub_program(crossover_point2, heuristic2, offspring2, sp1)

        if self.offspring_depth > 0:
            self.prune(offspring2, 0)

        offspring = (self.problem.evaluate(offspring1), self.problem.evaluate(offspring2))
        offspring[0].set_heuristic(offspring1)
        offspring[1].set_heuristic(offspring2)
        return offspring

    def copy(self, parent, crossover_parent):
        for i in range(parent.get_arity()):
            child = parent.get_child(i)
            c = Node()
            c.set_label(child.get_label())
            c.set_arity(child.get_arity())
            c = self.copy(child, c)
            crossover_parent.add_child(c, i)
        return crossover_parent

    def get_child(self, crossover_point, parent):
        for i in range(parent.get_arity()):
            self.crPoint += 1
            child = parent.get_child(i)
            if self.crPoint == crossover_point:
                self.crChild = child
                break
            else:
                self.get_child(crossover_point, child)

    def add_sub_program(self, crossover_point: int, parent: Node, crossover_parent: Node, sub_program: Node) -> Node:
        for i in range(parent.get_arity()):
            self.crPoint += 1
            child = parent.get_child(i)
            if self.crPoint != crossover_point:
                new_node = Node()
                new_node.set_label(child.get_label())
                new_node.set_arity(child.get_arity())
                new_node = self.add_sub_program(crossover_point, child, new_node, sub_program)
                crossover_parent.add_child(new_node, i)
            else:
                crossover_parent.add_child(sub_program, i)
        return crossover_parent

    def selection(self) -> Solution:
        best: Solution = self.random_generator.choice(self.population)
        for i in range(self.tournament_size - 1):
            current = self.random_generator.choice(self.population)
            if current.fitter(best) == 1:
                best = current
        return best

    def regenerate(self, best):
        number_of_mutations = int(math.floor(self.population_size * self.mutation_rate))
        number_of_crossovers = int(int(math.floor(self.population_size * self.crossover_rate)) / 2)
        if (number_of_mutations + number_of_crossovers * 2) < self.population_size:
            number_of_mutations += self.population_size - (number_of_mutations + number_of_crossovers * 2)
        new_population: List[Solution] = []
        for i in range(number_of_mutations):
            new_solution = self.mutation(self.selection())
            new_population.append(new_solution)
            if new_solution.fitter(best) == 1:
                best = new_solution
        for i in range(number_of_crossovers):
            new_solution1, new_solution2 = self.crossover(self.selection(), self.selection())
            new_population.append(new_solution1)
            if new_solution1.fitter(best) == 1:
                best = new_solution1
            new_population.append(new_solution2)
            if new_solution2.fitter(best) == 1:
                best = new_solution2
        self.population = new_population
        return best

    def evolve(self):
        if self.print_:
            print("Generation 0")
        best: Solution = self.create_population()
        if self.print_:
            self.print_labels(best.get_heuristic())
            print("", best.get_fitness())
            print()
        for i in range(self.no_of_generations):
            if self.print_:
                print("Generation", i + 1)
            new_program = self.regenerate(best)
            if new_program.fitter(best) == 1:
                best = new_program
            if self.print_:
                self.print_labels(best.get_heuristic())
                print("", best.get_fitness())
                print()
        print("Completed evolving heuristic")
        return best
