#
#  * This class contains the driver program for the class illustrate the use of
#  * the GenProg library. 
#  * 
#  * Nelishia Pillay
#  * 
#  * 8 October 2016
#  
# package: createheuristic

from GenProg.GenProg import GenProg
from examples.GeneticProgram.ComOptProb import ComOptProb


class CreateHeuristic(object):
    @classmethod
    def solve(cls):
        # This method illustrates how the selection construction hyper-heuristic in
        # the GenAlg library can be used to solve a combinatorial optimization problem.
        problem = ComOptProb()
        seed = 12512312352342165867854
        attribs = str("abc")
        problem.setAttribs(attribs)
        gchh = GenProg(seed, attribs, 1)
        gchh.setParameters("Parameters.txt")
        gchh.setProblem(problem)
        sol = gchh.evolve()
        print("Best Solution")
        print("--------------")
        print("Fitness:", sol.getFitness())
        print("Heuristic: ")
        print((sol.getHeuristic()).__str__())
        print("Solution: ")
        cls.displaySolution(sol.getSoln())

    @classmethod
    def displaySolution(cls, soln):
        # Displays the solution.
        count = 0
        while count < len(soln):
            attribs = soln[count].getAttribs()
            print(attribs[0], attribs[1], attribs[2])
            print()
            count += 1
        # EndforCount
        print()

    @classmethod
    def main(cls, args):
        cls.solve()


if __name__ == '__main__':
    import sys

    CreateHeuristic.main(sys.argv)
