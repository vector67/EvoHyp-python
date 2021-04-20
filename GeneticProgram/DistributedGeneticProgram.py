#
#  * This class implements a genetic programming generation constructive
#  * hyper-heuristic for generating new low-level construction heuristics for a
#  * problem domain. The running of the algorithm is distributed over a multicore
#  * architecture.
#  * <p>
#  * Nelishia Pillay
#  * 16 January 2017
#  

import multiprocessing
import queue
from typing import List, Any

from GeneticProgram.GeneticProgramProcess import GeneticProgramProcess, GeneticProgramProcessCreate, \
    GeneticProgramProcessRegenerate
from GeneticProgram.GeneticProgram import GeneticProgram
from GeneticProgram.Solution import Solution


#
#  * This class implements a genetic programming generation constructive 
#  * hyper-heuristic for generating new low-level construction heuristics for a 
#  * problem domain.
#
class DistributedGeneticProgram(GeneticProgram):
    # 
    #      * Stores the number of cores.
    #      
    no_of_cores = int()

    processes: List[GeneticProgramProcess]

    population_queue: queue.Queue[Any]
    best_queue: queue.Queue[Any]

    #
    #      * Constructor to set the random number generator seed, attributes and type of
    #      * heuristic.The random number generator is initialized in the constructor.
    #      * @param seed A long value specifying the seed to be used for the random number
    #      * generator.
    #      * @param attributes A string composed of characters, each representing an
    #      * attribute for the problem.
    #      * @param heuristic_type An integer value indicating whether an arithmetic function or
    #      * rule should be evolved. A value of 0 indicates and arithmetic function a value
    #      * of 1 indicates a rule.
    #      * @param noOfCores Specifies the number of available cores that implementation of the
    #      * genetic programming hyper-heuristic will be distributed over.
    #      
    def __init__(self, seed, attributes, heuristic_type, no_of_cores):
        super().__init__(seed, attributes, heuristic_type)
        self.no_of_cores = no_of_cores
        self.processes = []
        self.manager = multiprocessing.Manager()
        self.population_queue = self.manager.Queue()
        self.best_queue = self.manager.Queue()

    #
    #      * Sets the number of cores that the genetic algorithm hyper-heuristics will be
    #      * distributed over.
    #      * @param noOfCores Number of cores available.
    #      
    def set_no_of_cores(self, no_of_cores):
        self.no_of_cores = no_of_cores

    def create_population(self):
        self.population = []
        core_population_size = int(self.population_size / self.no_of_cores)
        for i in range(self.no_of_cores):
            gen_program = self.clone()
            gen_program.set_population_size(core_population_size)
            process = GeneticProgramProcessCreate(gen_program, self.population_queue, self.best_queue)
            process.start()
            self.processes.append(process)
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

    def regenerate(self, best):
        core_population_size = int(self.population_size / self.no_of_cores)
        for i in range(len(self.processes)):
            gen_program = self.clone()
            gen_program.set_population_size(core_population_size)
            self.processes[i] = GeneticProgramProcessRegenerate(gen_program,
                                                                self.population_queue,
                                                                self.best_queue,
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
