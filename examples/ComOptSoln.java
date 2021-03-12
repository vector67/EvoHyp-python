/*
 * This class implements the InitialSoln abstract class and is used to store
 * details of the initial solution.
 *
 * Nelishia Pillay
 *
 * 30 August 2016
 */
package solveproblem;

//Import statements
import initialsoln.*;

public class ComOptSoln extends InitialSoln
{
/******************************************************************************/
 //Data elements
    
 //Stores the heuristic combination that will be used to create an initial
 //solution.    
 private String heuristicComb; 
 
 //Stores the fitness value to be used for the initial solution created.
 private double fitness; 
 
 //Stores the initial solution created using the heuristic. In this problem
 //this is stored as an array of strings just as an example. However, the 
 //solution can be of any type, e.g. for the travelling salesman problem it 
 //could be a string representing the tour.
 String initSoln[]; 
 
 //It may be necessary to store other values that are specific to problem being
 //solved that is different from the fitness or needed to calculate the fitness.
 //For example, for the examination timetabling problem the hard and soft
 //constraint cost also needs to be stored.
 
/******************************************************************************/

/***Implementation of abstract methods needed to extend InitialSoln***/
 
/******************************************************************************/
 public double getFitness()
 {
   return fitness;  
 }
/******************************************************************************/
 
/******************************************************************************/
 public void setHeuCom(String heuristicComb)
 {
   //Implements the abstract method to store the heuristic combination used to
   //create an initial solution.
     
   this.heuristicComb=heuristicComb;   
 }
/******************************************************************************/

 /******************************************************************************/
public String getHeuCom()
{
 //Implements the abstract method to return the heuristic combination used to
 //create the solution.
    
  return heuristicComb; 
}
/******************************************************************************/

/******************************************************************************/
public String[] getSoln()
{
  //Implements the abstract method to return a solution.
    
  return initSoln;  
}
/******************************************************************************/

/******************************************************************************/
 public int fitter(InitialSoln other)
 {
   //This method is used to compare two intial solutions to determine which of 
   //the two is fitter. 
     
   if(other.getFitness() < fitness)
    return 1;
   else if (other.getFitness() > fitness)
    return -1;
   else
    return 0;
 }
/******************************************************************************/ 

/***Methods in addition to the abstract methods that need to be implemented.***/

/******************************************************************************/ 
 public void createSoln()
 {
   /*This method creates a solution using the heuristic combination.*/
   
   //Construct a solution to the problem using the heuristic combination.
     
   String temp[]={"This"," is"," a"," solution"," created"," using ",
                  heuristicComb}; 
   initSoln=temp;
   
   //Calculate the fitness of the constructed solution. This is just an example
   //so simply adds the length of the solution to a random double.
   
   fitness=temp.length+Math.random();
 }
/******************************************************************************/ 
}
