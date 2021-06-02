#
# This package implements a genetic algorithm for selection hyper-heuristics.
# Nelishia Pillay
# 27 August 2016
#  

# Import statements
from random import Random
from typing import List

#
# This class implements a genetic algorithm selection hyper-heuristic
# hyper-heuristic for selecting low-level constructive or perturbative
# heuristics for a problem domain.
#
from GeneticAlgorithm.Solution import Solution
from GeneticAlgorithm.Problem import Problem


class GeneticAlgorithm(object):
    # Data elements
    # 
    # Stores the population size.
    #      
    population_size: int

    # 
    # Stores the tournament size.
    #      
    tournament_size: int

    # 
    # Stores the number of generations.
    #      
    no_of_generations: int

    # 
    # Stores the mutation application rate.
    #      
    mutation_rate: float

    # 
    # Stores the crossover application rate.
    #      
    crossover_rate: float

    # 
    # Stores the reproduction application rate.
    #      
    reproduction_rate: float

    # 
    # Stores the length of the string created by mutation to insert into the
    # selected parent.The length of the string is randomly selected between 1 and t
    # the this limit.
    #      
    mutation_length: int

    # 
    # Stores the maximum length for the chromosomes create in the initial
    # population.
    #      
    initial_max_length: int

    # 
    # Stores the maximum length or the chromosomes produced by the genetic
    # operators.
    #      
    offspring_max_length: int

    # 
    # Stores a Boolean value indicating whether duplicates should be allowed in the
    # initial population or not.The default value is false.
    #      
    allow_duplicates: bool

    # 
    # Stores a character for each problem specific heuristic.For example, "lse",
    # in which case there are three problem specific heuristics,represented by
    # "l", "s" and "e" respectively.
    #      
    heuristics: str

    # 
    # This variable stores a problem domain instance.
    #      
    problem: Problem

    # 
    # Stores and instances of the random number generator to be used by the genetic
    # algorithm.
    #      
    ranGen: Random

    # 
    # Stores the population.Each element is an instance of type InitialSolution.This
    # instance stores the heuristic combination, fitness and initial solution.
    #      
    population: List[Solution]

    # 
    # Is a flag used to determine whether output for each generation must be
    # printed to the screen or not.If it is set to <tt>true</tt> output is
    # printed.If it is set to false output is not printed.The default is
    # <tt>true</tt>.
    #      
    print_: bool

    #
    # This is the constructor for the class.
    #
    # @param seed       The seed for the random number generator.
    # @param heuristics A string of characters representing each of the low-level
    #                   heuristics for the problem domain.
    #      
    def __init__(self, seed=123, heuristics='', ran_gen=None):
        if heuristics == '':
            raise ValueError('Heuristics cannot be empty')
        self.heuristics = heuristics
        # Initializes the random number generator.
        if seed == 123 and ran_gen is not None:
            self.ranGen = ran_gen
        else:
            self.ranGen = Random(seed)
        # Set the flag for printing output for each generation to true.
        self.print_ = True
        self.population_size = 0
        self.allow_duplicates = False

    # Methods for setting parameter values for the genetic algorithm
    #
    # Reads the parameters from a file and stores them as data element.
    #
    # @param parameterFile The name of the file the parameter values are stored
    #                      in.
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
                self.initial_max_length = int(f.readline())
                self.offspring_max_length = int(f.readline())
                self.mutation_length = int(f.readline())
        except IOError as ioe:
            print("The file " + parameter_file + " cannot be found. " + "Please check the details provided.", ioe)

    #
    # Sets the number of generations size.
    #
    # @param noOfGenerations Parameter value for the number of generations.
    #      
    def set_no_of_generations(self, no_of_generations):
        self.no_of_generations = no_of_generations

    #
    # @return The population size.
    #      
    def get_population_size(self):
        return self.population_size

    #
    # Sets the population size.
    #
    # @param populationSize Parameter value for the population size.
    #      
    def set_population_size(self, population_size):
        self.population_size = population_size

    #
    # @return The tournament size.
    #      
    def get_tournament_size(self):
        return self.tournament_size

    #
    # Sets the tournament size.
    #
    # @param tournamentSize Parameter value for the tournament size.
    #      
    def set_tournament_size(self, tournament_size):
        self.tournament_size = tournament_size

    #
    # @return Returns the number of generations.
    #      
    def get_no_of_generations(self):
        return self.no_of_generations

    #
    # @return Returns the mutation rate.
    #      
    def get_mutation_rate(self):
        return self.mutation_rate

    #
    # Sets the mutation rate.
    #
    # @param mutationRate Parameter value for the mutation rate. The value must be
    #                     a fraction, e.g. 0.5.
    #      
    def set_mutation_rate(self, mutation_rate):
        self.mutation_rate = mutation_rate

    #
    # @return Returns the crossover rate.
    #      
    def get_crossover_rate(self):
        return self.crossover_rate

    #
    # Sets the crossover rate.
    #
    # @param crossoverRate Parameter value for the crossover rate. The value must
    #                      be a fraction, e.g. 0.5.
    #      
    def set_crossover_rate(self, crossover_rate):
        self.crossover_rate = crossover_rate

    #
    # @return Returns the reproduction rate.
    #      
    def get_reproduction_rate(self):
        return self.reproduction_rate

    #
    # @return Returns the initial maximum length permitted for heuristic
    # combinations created in the initial population.
    #      
    def get_initial_max_length(self):
        return self.initial_max_length

    #
    # Sets the maximum length of chromosome in the initial population.
    #
    # @param initialMaxLength Parameter value specifying the maximum length
    #                         permitted for heuristic combinations created in the initial population.
    #      
    def set_initial_max_length(self, initial_max_length):
        self.initial_max_length = initial_max_length

    #
    # @return Returns the maximum offspring length.
    #      
    def get_offspring_max_length(self):
        return self.offspring_max_length

    #
    # Sets the maximum length of the offspring produced by the genetic operators.If
    # the offspring size exceeds this length the substring equal to this value is
    # returned.
    #
    # @param offspringMaxLength Parameter value specifying the maximum length
    #                           permitted for offspring created by the mutation and crossover operators.
    #      
    def set_offspring_max_length(self, offspring_max_length):
        self.offspring_max_length = offspring_max_length

    #
    # @return Returns the mutation length.
    #      
    def get_mutation_length(self):
        return self.mutation_length

    #
    # Sets the maximum permitted length for the new substring created by mutation
    # to be inserted at a randomly selected point in a copy of the parent.The
    # length of the substring to be inserted is randomly selected to be between 1
    # and the this limit.
    #
    # @param mutationLength Parameter value specifying the mutation length.
    #      
    def set_mutation_length(self, mutation_length):
        self.mutation_length = mutation_length

    #
    # @return Returns the value of the Boolean flag that is used to specify if
    # duplicates are allowed or not.
    #      
    def get_allow_duplicates(self):
        return self.allow_duplicates

    #
    # This method sets the Boolean flag indicating whether duplicates are allowed
    # in the initial population of not.
    #
    # @param allowDuplicates A value of true or false which indicates whether
    #                        duplicates are allowed in the initial population or not.
    #      
    def set_allow_duplicates(self, allow_duplicates):
        self.allow_duplicates = allow_duplicates

    #
    # @return Returns the value of the flag print used to determine whether to
    # print output to the screen.
    #      
    def get_print(self):
        return self.print_

    #
    # Sets the flag for printing output for each generation to the screen.If it is
    # set to <tt>true</tt> output is printed.If it is set to <tt>false</tt> output
    # is not printed.The default is <tt>true</tt>.The output printed to the screen
    # is the best heuristic combination for each generation and its fitness as well
    # as the best fitness obtained thus far in the run.
    #
    # @param print A value of false or true indicating whether output for each
    #              generation must be printed to the screen or not.
    #      
    def set_print(self, print_):
        self.print_ = print_

    # Methods for setting problem specific information
    #
    # This method sets the string of characters, each representing a low-level
    # heuristic for the problem domain.
    #
    # @param heuristics The string of characters representing the low-level
    #                   heuristics.
    #      
    def set_heuristics(self, heuristics):
        self.heuristics = heuristics

    #
    # This method sets the string of characters, each representing a low-level
    # heuristic for the problem domain.
    #
    # @param problem Is an instance of <tt>ProblemDomain</tt> which defines the
    #                problem domain.
    #      
    def set_problem(self, problem):
        self.problem = problem

    # Methods for creating the initial population
    def exists(self, heuristic_combination, pos):
        # Checks whether the Comb already exists in the population.
        count = 0
        while count < pos:
            if heuristic_combination == self.population[count].get_heuristic_combination():
                return True
            count += 1
        return False

    def create_heuristic_combination(self):
        heuristic_combination = ''
        length = self.ranGen.randrange(self.initial_max_length) + 1
        count = 1
        while count <= length:
            heuristic_combination += self.ranGen.choice(self.heuristics)
            count += 1
        return heuristic_combination

    def create_population(self) -> Solution:
        best = None
        self.population = []
        count = 0
        while count < self.population_size:
            if not self.allow_duplicates and self.population_size <= len(self.heuristics):
                while True:
                    ind = self.create_heuristic_combination()
                    if not self.exists(ind, count):
                        break
            else:
                ind = self.create_heuristic_combination()
            self.population.append(self.evaluate(ind))
            self.population[count].set_heuristic_combination(ind)
            if count == 0:
                best = self.population[count]
            elif self.population[count].fitter(best) == 1:
                best = self.population[count]
            count += 1
        return best

    def display_population(self):
        print("Population")
        for element in self.population:
            print(element.get_heuristic_combination(), element.get_fitness())

    def evaluate(self, ind) -> Solution:
        return self.problem.evaluate(ind)

    def selection(self) -> Solution:
        winner = self.ranGen.choice(self.population)
        count = 1
        while count < self.tournament_size:
            current = self.ranGen.choice(self.population)
            if current.fitter(winner) == 1:
                winner = current
            count += 1
        if self.print_:
            print('winner', winner)
        return winner

    def crossover(self, parent1: Solution, parent2: Solution) -> Solution:
        p1 = parent1.get_heuristic_combination()
        p2 = parent2.get_heuristic_combination()
        point1 = self.ranGen.randrange(len(p1))
        point2 = self.ranGen.randrange(len(p2))
        frag11 = p1[:point1]
        frag12 = p1[point1:]
        frag21 = p2[:point2]
        frag22 = p2[point2:]
        os1 = str(frag11 + frag22)
        os2 = str(frag21 + frag12)
        if self.offspring_max_length > 0 and self.offspring_max_length > len(os1):
            os1 = os1[:self.offspring_max_length]
        if self.offspring_max_length > 0 and self.offspring_max_length > len(os2):
            os2 = os2[:self.offspring_max_length]
        offspring1 = self.evaluate(os1)
        offspring1.set_heuristic_combination(os1)
        offspring2 = self.evaluate(os2)
        offspring2.set_heuristic_combination(os2)
        if offspring1.fitter(offspring2) == 1:
            return offspring1
        else:
            return offspring2

    def create_string(self, limit):
        str_ = ''
        count = 0
        while count < limit:
            str_ += self.ranGen.choice(self.heuristics)
            count += 1
        return str_

    def mutation(self, parent: Solution):
        com = parent.get_heuristic_combination()
        if self.print_:
            print('com', com)
        mutation_point = self.ranGen.randrange(len(com))
        mutation_length = self.ranGen.randrange(self.mutation_length) + 1
        hh = self.create_string(mutation_length)
        begin = com[: mutation_point]
        end = com[mutation_point + 1:]
        tem = begin + hh + end
        if self.offspring_max_length > 0 and self.offspring_max_length > len(tem):
            tem = tem[:self.offspring_max_length]
        offspring = self.evaluate(tem)
        offspring.set_heuristic_combination(tem)
        return offspring

    def regenerate(self, best_individual) -> Solution:
        number_of_mutations = int((self.mutation_rate * self.population_size))
        number_of_crossovers = int((self.crossover_rate * self.population_size))
        self.reproduction_rate = 0
        if (self.mutation_rate + self.crossover_rate) < 1:
            self.reproduction_rate = 1 - (self.mutation_rate + self.crossover_rate)
        number_of_reproductions = int((self.reproduction_rate * self.population_size))
        if not number_of_mutations + number_of_crossovers + number_of_reproductions == len(self.population):
            if not number_of_crossovers == 0:
                number_of_crossovers += len(self.population) - (
                        number_of_mutations + number_of_crossovers + number_of_reproductions)
            elif not number_of_mutations == 0:
                number_of_mutations += len(self.population) - (
                        number_of_mutations + number_of_crossovers + number_of_reproductions)
        best = best_individual
        index = 0
        new_population: List[Solution] = []
        for i in range(number_of_reproductions):
            new_population.append(self.selection())
            if new_population[index].fitter(best) == 1:
                best = new_population[index]
            index += 1
        for i in range(number_of_mutations):
            new_population.append(self.mutation(self.selection()))
            if new_population[index].fitter(best) == 1:
                best = new_population[index]
            index += 1
        for i in range(number_of_crossovers):
            new_population.append(self.crossover(self.selection(), self.selection()))
            if new_population[index].fitter(best) == 1:
                best = new_population[index]
            index += 1
        self.population = new_population
        return best

    def evolve(self):
        if self.print_:
            print("Generation 0")
        best = self.create_population()
        if self.print_:
            print("Best Fitness:", best.get_fitness())
            print("Heuristic Combination:", best.get_heuristic_combination())
            print()
        count = 0
        while count < self.no_of_generations:
            if self.print_:
                print("Generation", count+1)
            ind = self.regenerate(best)
            if ind.fitter(best) == 1:
                best = ind
            if self.print_:
                print("Generation Best Fitness:", ind.get_fitness())
                print("Generation Best Heuristic Combination: " + ind.get_heuristic_combination())
                print("Overall Best Fitness:", best.get_fitness())
                print("Overall Best Heuristic Combination: " + best.get_heuristic_combination())
                print()
            count += 1
        print("Completed evolving heuristic combination")
        return best
