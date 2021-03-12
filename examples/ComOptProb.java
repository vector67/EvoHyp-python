/*
 * This class implements a problem domain and implements ProblemDomain abstract
 * class.
 *
 * N. Pillay
 *
 * 30 August 2016 
 */
package solveproblem;

//Import statements
import problemdomain.*;
import initialsoln.*;

public class ComOptProb extends ProblemDomain
{
/***Methods that are abstract in ProblemDomain that need to be implemented***/
    
/******************************************************************************/ 
 public ComOptSoln evaluate(String heuristicComb)
 {
   //Implements the abstract method to create a solution using heuristicComb
   //using an instane of the InitialSoln class which is also used to calculate
   //the fitness using the objective value of the created solution.
     
   ComOptSoln soln = new ComOptSoln();
   soln.setHeuCom(heuristicComb);
   soln.createSoln();
   
   return soln;
 }
/******************************************************************************/
}
