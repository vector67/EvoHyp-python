import multiprocessing as mp
from multiprocessing import Queue

from GeneticAlgorithm.GeneticAlgorithm import GeneticAlgorithm


class GeneticAlgorithmProcess(mp.Process):
    def __init__(self, gen_alg: GeneticAlgorithm, population_queue: Queue, best_queue: Queue):
        super().__init__()
        self.gen_alg = gen_alg
        self.best = None

        self.population_queue = population_queue
        self.best_queue = best_queue


class GeneticAlgorithmProcessCreate(GeneticAlgorithmProcess):
    def __init__(self, gen_alg: GeneticAlgorithm, population_queue: Queue, best_queue: Queue):
        super().__init__(gen_alg, population_queue, best_queue)

    def run(self) -> None:
        self.best = self.gen_alg.create_population()
        self.best_queue.put(self.best)
        self.population_queue.put(self.gen_alg.population)


class GeneticAlgorithmProcessRegenerate(GeneticAlgorithmProcess):
    def __init__(self, gen_alg: GeneticAlgorithm, population_queue: Queue, best_queue: Queue, best):
        super().__init__(gen_alg, population_queue, best_queue)
        self.best = best

    def run(self) -> None:
        self.gen_alg.population = self.population_queue.get()
        self.best_queue.put(self.gen_alg.regenerate(self.best))
        self.population_queue.put(self.gen_alg.population)
