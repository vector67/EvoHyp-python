/*
 * This is an example program used to illustrate how to use the EvoHyp library
 * to create an initial solution to a problem using the genetic algorithm
 * hyper-heuristic provided by the library.
 *
 * N. Pillay
 *
 * 30 August 2016
 */
package solveproblem;

//Import statements
import distrgenalg.*;
import initialsoln.*;


public class SolveProblem 
{
/******************************************************************************/
 static public void solve()
 {
  //This method illustrates how the selection construction hyper-heuristic in
  //the GenAlg library can be used to solve a combinatorial optimization problem.
  
  ComOptProb problem = new ComOptProb();
  long seed = System.currentTimeMillis();
  String heuristics=new String("abc");
  DistrGenAlg schh = new DistrGenAlg(seed,heuristics,4);
  schh.setParameters("Parameters.txt");
  schh.setProblem(problem);
  InitialSoln solution= schh.evolve();
  
  System.out.println("Best Solution");
  System.out.println("--------------");
  System.out.println("Fitness: "+solution.getFitness());
  System.out.println("Heuristic combination: "+solution.getHeuCom());
  System.out.println("Solution: ");
  displaySolution(solution.getSoln());
 
 }
/******************************************************************************/    

/******************************************************************************/
static private void displaySolution(Object solution)
{
  //Displays the solution. 
    
  String soln[] = (String[])solution;
  for(int count=0;count < soln.length;++count)
  {
    System.out.print(soln[count]+" ");  
  }//EndforCount
  System.out.println();     
}
/******************************************************************************/
 
/******************************************************************************/
 public static void main(String[] args) 
 {
   solve();
 }
/******************************************************************************/    
}
