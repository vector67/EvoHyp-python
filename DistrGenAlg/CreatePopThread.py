#
#  Thread for creating part of the initial population.
#
#  Nelishia Pillay
#  10 December 2016
#  
# package: distrgenalg
import random
from typing import List

from DistrGenAlg.Output import Output
from InitialSolution import InitialSolution
from ProblemDomain import ProblemDomain


#
#  Implements the thread for initial population generation.
#
class CreatePopThread:
    #
    # Data elements
    # 
    #   This variable stores the size of the subpopulation.
    #      
    pop_size: int

    # 
    # Stores the subpopulation. Each element is an instance of type InitialSolution.
    # This instance stores the heuristic combination, fitness and initial solution.
    #      
    sub_pop: List[InitialSolution]

    # 
    # This variable stores a problem domain instance.
    #      
    problem: ProblemDomain

    # 
    # Stores the maximum length for the chromosomes create in the initial population.
    #      
    initial_max_length: int

    # 
    # Stores a character for each problem specific heuristic. For example, "lse",
    # in which case there are three problem specific heuristics,represented by
    # "l", "s" and "e" respectively.
    #      
    heuristics: str

    # 
    # Stores and instances of the random number generator to be used by the genetic algorithm.
    #      
    ran_gen: random.Random

    #
    # Constructor for the thread for initial population generation.
    #
    # @param popSize          Specifies the population size.
    # @param heuristics       String comprised of characters representing each of the
    #                         heuristics.
    # @param initialMaxLength Specifies the maximum length permitted for heuristic
    #                         combinations created in the initial population.
    # @param problem          Specifies the problem domain.
    # @param ranGen           Random number generator.
    #      
    def __init__(self, pop_size, heuristics, initial_max_length, problem, ran_gen):
        super(CreatePopThread, self).__init__()
        self.pop_size = pop_size
        self.heuristics = heuristics
        self.initial_max_length = initial_max_length
        self.problem = problem
        self.ran_gen = ran_gen

    def create_combination(self) -> str:
        # This method creates the heuristic combination to construct or perturb a solution.
        heuristic_combination = str()
        length = self.ran_gen.randrange(self.initial_max_length) + 1
        for i in range(1, length):
            index = self.ran_gen.randrange(len(self.heuristics))
            heuristic_combination += self.heuristics[index: index + 1]
        return heuristic_combination

    # 
    # The call method for the thread for initial population generation.
    #      
    def call(self):
        # Creates the initial population. Duplicates are not allowed.
        best = None
        # self.sub_pop = [None] * self.pop_size
        count = 0
        while count < self.pop_size:
            while True:
                ind = self.create_combination()
                if not self.exists(ind, count):
                    break
            self.sub_pop[count] = self.evaluate(ind)
            self.sub_pop[count].set_heuristic_combination(ind)
            if count == 0:
                best = self.sub_pop[count]
            elif self.sub_pop[count].fitter(best) == 1:
                best = self.sub_pop[count]
            count += 1
        output = Output()
        output.setBest(best)
        output.setSubPop(self.sub_pop)
        return output

    def display_population(self):
        count = 0
        while len(self.sub_pop):
            print(self.sub_pop[count].get_heuristic_combination() + " " + str(self.sub_pop[count].get_fitness()))
            count += 1

    def evaluate(self, ind):
        return self.problem.evaluate(ind)

    def exists(self, heuristic_combination, pos):
        count = 0
        while count < pos:
            if heuristic_combination == self.sub_pop[count].get_heuristic_combination():
                return True
            count += 1
        return False
