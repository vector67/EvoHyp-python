/**
 * This class defines the evaluator to evaluate the heuristic given the values
 * of the attributes.
 * <p>
 * Nelishia Pillay
 * <p>
 * 7 October 2016
 */
package genprog;

/**
 * This class provides an interpreter for the evolved heuristic.
 */
public class Evaluator {
/******************************************************************************/
/**
 * Data elements */

    /**
     * The array stores the values of the problem attributes.
     */
    private double attributeVals[];

    /**
     * Stores the characters for each of the attributes.
     */
    private String attributes;
/******************************************************************************/

/******************************************************************************/
    /**
     * Constructor for the class which stores the attributes and attribute values.
     * @param attributes A string containing characters representing the attributes
     * for the problem
     * @param attributeVals An array of real numbers representing the attribute
     * values.
     */
    public Evaluator(String attributes, double attributeVals[]) {
        /**
         * Constructor that stores the attributes and attributes values
         * for the problem.
         */

        this.attributeVals = attributeVals;
        this.attributes = attributes;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * This method interprets a heuristic of type <tt>Node</tt> and calculates its
     * corresponding real value.
     * @param op The evolved heuristic to be interpreted.
     * @return Returns the real the heuristic evaluates to.
     */
    public double eval(Node op) {

        if (op.getArity() == 0)
            return getVal(op.getLabel());
        else if (op.getLabel().compareTo("if") == 0) {
            Node cond = op.getChild(0);
            if (eval(cond) == 1)
                return eval(op.getChild(1));
            else
                return eval(op.getChild(2));
        } else {
            double args[] = new double[op.getArity()];
            for (int count = 0; count < op.getArity(); ++count) {
                args[count] = eval(op.getChild(count));
            }//endfor_count

            return calc(op.getLabel(), args);
        }//endelse
    }
/******************************************************************************/

    /******************************************************************************/
    private double calc(String op, double args[]) {
        /**
         * This method applies each operator in the heuristic.
         */

        if (op.compareTo("+") == 0) {
            return args[0] + args[1];
        } else if (op.compareTo("-") == 0) {
            return args[0] - args[1];
        } else if (op.compareTo("*") == 0) {
            return args[0] * args[1];
        } else if (op.compareTo("/") == 0) {
            if (args[1] == 0)
                return 1;
            else
                return ((int) args[0] / args[1]);
        } else if (op.compareTo("<") == 0) {
            if (args[0] < args[1])
                return 1;
            else
                return 0;
        } else if (op.compareTo(">") == 0) {
            if (args[0] > args[1])
                return 1;
            else
                return 0;
        } else if (op.compareTo("==") == 0) {
            if (args[0] == args[1])
                return 1;
            else
                return 0;
        } else if (op.compareTo("!=") == 0) {
            if (args[0] != args[1])
                return 1;
            else
                return 0;
        } else if (op.compareTo(">=") == 0) {
            if (args[0] >= args[1])
                return 1;
            else
                return 0;
        } else if (op.compareTo("and") == 0) {
            if (args[0] == 1 && args[1] == 1)
                return 1;
            else
                return 0;
        } else {
            if (args[0] <= args[1])
                return 1;
            else
                return 0;
        }
    }
/******************************************************************************/

    /******************************************************************************/
    private double getVal(String attribute) {
        /**
         * Returns the attribute value corresponding to the terminal node.
         */
        return attributeVals[attributes.indexOf(attribute)];

    }
/******************************************************************************/
}
