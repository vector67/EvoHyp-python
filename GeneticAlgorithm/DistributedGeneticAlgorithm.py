#
#  * This package implements a genetic algorithm hyper-heuristic using a
#  * distributed multi-core architecture.
#  * <p>
#  * Nelishia Pillay
#  * 10 December 2016
#  
# package: distrgenalg

# 
#  * This package implements a genetic algorithm selection hyper-heuristic using a 
#  * distributed multi-core architecture.
#
import multiprocessing
import queue
from typing import List, Any

from GeneticAlgorithm.GeneticAlgorithmProcess import GeneticAlgorithmProcessCreate, GeneticAlgorithmProcessRegenerate, GeneticAlgorithmProcess
from GeneticAlgorithm.GeneticAlgorithm import GeneticAlgorithm
from GeneticAlgorithm.Solution import Solution


class DistributedGeneticAlgorithm(GeneticAlgorithm):
    #
    #      * Stores the number of cores.
    #      
    noOfCores: int

    processes: List[GeneticAlgorithmProcess]

    population_queue: queue.Queue[Any]
    best_queue: queue.Queue[Any]

    #
    #      * This is the constructor for the class.
    #      * @param seed The seed for the random number generator.
    #      * @param heuristics A string of characters representing each of the low-level
    #      * @param noOfCores Specifies the number of cores that the genetic algorithm
    #      * will be distributed over.
    #
    def __init__(self, seed, heuristics, noOfCores):
        super().__init__(seed, heuristics)
        self.noOfCores = noOfCores
        self.processes = []
        self.manager = multiprocessing.Manager()
        self.population_queue = self.manager.Queue()
        self.best_queue = self.manager.Queue()

    #
    #      *
    #      * @return Returns the mutation length.
    #      
    def getNoOfCores(self):
        return self.noOfCores

    #
    #      * Sets the number of cores that the genetic algorithm hyper-heuristics will be
    #      * distributed over.
    #      * @param noOfCores Number of cores available.
    #      
    def setNoOfCores(self, noOfCores):
        self.noOfCores = noOfCores

    def create_gen_alg(self):
        gen_alg = GeneticAlgorithm(heuristics=self.heuristics, ran_gen=self.ranGen)
        gen_alg.set_population_size(int(self.population_size / self.noOfCores))
        gen_alg.set_initial_max_length(self.initial_max_length)
        gen_alg.set_problem(self.problem)
        gen_alg.tournament_size = self.tournament_size
        gen_alg.no_of_generations = self.no_of_generations
        gen_alg.mutation_rate = self.mutation_rate
        gen_alg.crossover_rate = self.crossover_rate
        gen_alg.offspring_max_length = self.offspring_max_length
        gen_alg.mutation_length = self.mutation_length
        return gen_alg

    def create_population(self) -> Solution:
        self.population = []
        count = 0

        while count < self.noOfCores:
            gen_alg = self.create_gen_alg()
            process = GeneticAlgorithmProcessCreate(gen_alg, self.population_queue, self.best_queue)
            process.start()
            self.processes.append(process)
            count += 1

        best: Solution = None
        for process in self.processes:
            process.join()
            possible_best = self.best_queue.get()
            if best is None:
                best = possible_best
            elif possible_best.fitter(best) == 1:
                best = possible_best
            self.population.extend(self.population_queue.get())
        return best

    def displayPopulation(self):
        count = 0
        while len(self.population):
            print(self.population[count].get_heuristic_combination(), self.population[count].get_fitness())
            count += 1

    def evaluate(self, ind):
        return self.problem.evaluate(ind)

    def regenerate(self, best_individual: Solution):
        core_population_size = int(self.population_size / self.noOfCores)
        for i in range(len(self.processes)):
            gen_alg = self.create_gen_alg()
            self.processes[i] = GeneticAlgorithmProcessRegenerate(gen_alg, self.population_queue, self.best_queue,
                                                                  self.population[i * core_population_size])
            self.population_queue.put(self.population[i * core_population_size: (i + 1) * core_population_size])

        for process in self.processes:
            process.start()
        best = None
        self.population = []
        for process in self.processes:
            process.join()
            possible_best = self.best_queue.get()
            if best is None:
                best = possible_best
            elif possible_best.fitter(best) == 1:
                best = possible_best
            self.population.extend(self.population_queue.get())
        return best
    #
    # def evolve(self):
    #     """ generated source for method evolve """
    #     if self.print_:
    #         print("Generation 0")
    #     best = self.createPop()
    #     if self.print_:
    #         print(best.get_fitness(), best.get_heuristic_combination())
    #     count = 1
    #     while count <= self.no_of_generations:
    #         if self.print_:
    #             print("Generation", count)
    #         ind = self.regen(best)
    #         if ind.fitter(best) == 1:
    #             best = ind
    #         if self.print_:
    #             print(best.getFitness() + " " + best.getHeuCom())
    #         count += 1
    #     print("Completed evolving heuristic combination.")
    #     return best
