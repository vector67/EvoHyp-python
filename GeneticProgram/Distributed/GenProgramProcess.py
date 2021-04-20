import multiprocessing as mp
from multiprocessing import Queue

from GeneticProgram.GeneticProgram import GeneticProgram


class GenProgramProcess(mp.Process):
    def __init__(self, gen_program: GeneticProgram, population_queue: Queue, best_queue: Queue):
        super().__init__()
        self.gen_program = gen_program
        self.best = None

        self.population_queue = population_queue
        self.best_queue = best_queue


class GenProgramProcessCreate(GenProgramProcess):
    def __init__(self, gen_program: GeneticProgram, population_queue: Queue, best_queue: Queue):
        super().__init__(gen_program, population_queue, best_queue)

    def run(self) -> None:
        self.best = self.gen_program.create_population()
        self.best_queue.put(self.best)
        self.population_queue.put(self.gen_program.population)


class GenProgramProcessRegenerate(GenProgramProcess):
    def __init__(self, gen_program: GeneticProgram, population_queue: Queue, best_queue: Queue, best):
        super().__init__(gen_program, population_queue, best_queue)
        self.best = best

    def run(self) -> None:
        self.gen_program.population = self.population_queue.get()
        self.best_queue.put(self.gen_program.regenerate(self.best))
        self.population_queue.put(self.gen_program.population)
