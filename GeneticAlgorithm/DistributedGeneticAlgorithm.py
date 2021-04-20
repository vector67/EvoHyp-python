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

from GeneticAlgorithm.GeneticAlgorithmProcess import GeneticAlgorithmProcessCreate, GeneticAlgorithmProcessRegenerate, \
    GeneticAlgorithmProcess
from GeneticAlgorithm.GeneticAlgorithm import GeneticAlgorithm
from GeneticAlgorithm.Solution import Solution


class DistributedGeneticAlgorithm(GeneticAlgorithm):
    #
    #      * Stores the number of cores.
    #      
    no_of_cores: int

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
    def __init__(self, seed, heuristics, no_of_cores):
        super().__init__(seed, heuristics)
        self.no_of_cores = no_of_cores
        self.processes = []
        self.manager = multiprocessing.Manager()
        self.population_queue = self.manager.Queue()
        self.best_queue = self.manager.Queue()

    #
    #      *
    #      * @return Returns the mutation length.
    #      
    def get_no_of_cores(self):
        return self.no_of_cores

    #
    #      * Sets the number of cores that the genetic algorithm hyper-heuristics will be
    #      * distributed over.
    #      * @param noOfCores Number of cores available.
    #      
    def set_no_of_cores(self, no_of_cores):
        self.no_of_cores = no_of_cores

    def create_genetic_algorithm(self):
        genetic_algorithm = GeneticAlgorithm(heuristics=self.heuristics, ran_gen=self.ranGen)
        genetic_algorithm.set_population_size(int(self.population_size / self.no_of_cores))
        genetic_algorithm.set_initial_max_length(self.initial_max_length)
        genetic_algorithm.set_problem(self.problem)
        genetic_algorithm.tournament_size = self.tournament_size
        genetic_algorithm.no_of_generations = self.no_of_generations
        genetic_algorithm.mutation_rate = self.mutation_rate
        genetic_algorithm.crossover_rate = self.crossover_rate
        genetic_algorithm.offspring_max_length = self.offspring_max_length
        genetic_algorithm.mutation_length = self.mutation_length
        return genetic_algorithm

    def create_population(self) -> Solution:
        self.population = []
        count = 0

        while count < self.no_of_cores:
            gen_alg = self.create_genetic_algorithm()
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

    def evaluate(self, ind):
        return self.problem.evaluate(ind)

    def regenerate(self, best_individual: Solution):
        core_population_size = int(self.population_size / self.no_of_cores)
        for i in range(len(self.processes)):
            gen_alg = self.create_genetic_algorithm()
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