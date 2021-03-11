/*
 *This class defines the structure for the expression tree which will be used
 *to represent each element of the genetic programming population.
 *
 *Nelishia Pillay
 *11 September 2016
 */
package genprog;

/**
 * This class defines the structure to store the heuristic.
 */
public class Node {
    /******************************************************************************/
    /*Data elements*/

//Stores the label for the node, e.g.+, if.     
    private String label;

    //Stores the node arity,i.e. the number of arguments, e.g. the + operator takes
//two arguments and so has an arity of 2. 
    private int arity;

    //Stores the children for node, forming the next layer of the expression tree.
    private Node children[];

    //Stores the index for the children array. Is incremented each time a child is
//added.
    private int index;

    //Strong typing is used and the type of the node is stored.
    private String type;

    //Stores the heuristic in prefix notation
    private String heu;
/******************************************************************************/

/************************Set and Add Methods***********************************/

/******************************************************************************/

    /**
     * Adds a child to the node.
     *
     * @param child Child node to be added to the node.
     */
    public void addChild(Node child) {
        if (children == null) {
            children = new Node[arity];
            index = 0;
            children[index] = child;
        } else
            children[++index] = child;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Adds a child to the node at a particular position, e.g.second child.
     *
     * @param child Child node to be added to the node.
     * @param pos   The position at which to add the child, e.g. first child.
     */
    public void addChild(Node child, int pos) {
        if (children == null) {
            children = new Node[arity];
        }
        children[pos] = child;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the type of the node.
     */
    public String getType() {
        return type;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the type of the node.
     *
     * @param type Specifies the node type e.g. integer.
     */
    public void setType(String type) {
        this.type = type;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the label of the node.
     */
    public String getLabel() {
        return label;
    }
/******************************************************************************/

/*****************************Get Methods**************************************/

/******************************************************************************/

    /**
     * Specifies the label of the node.
     *
     * @param label Specifies the label of the node, e.g. +.
     */
    public void setLabel(String label) {
        this.label = label;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @return Returns the arity of the node.
     */
    public int getArity() {
        return arity;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * Sets the arity of the node.
     *
     * @param arity An integer value specifying the arity of the node.
     */
    public void setArity(int arity) {
        this.arity = arity;
    }
/******************************************************************************/

/******************************************************************************/

    /**
     * @param pos The position of the child, e.g. first child.
     * @return Returns the child, i.e. node, at the specified position.
     */
    public Node getChild(int pos) {
        //Returns the child at the postion in the array specified by "pos".

        if (children != null && pos < children.length)
            return children[pos];
        else
            return null;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * @return Returns the children of the node in an array of type <tt>Node</tt>.
     */
    public Node[] getChildren() {
        return children;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * Deletes the children of the node.Is used to prune the tree when the offspring
     * exceeds the maximum offspring depth.
     */
    public void clearChildren() {
        children = null;
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * Displays the heuristic in prefix notation.
     */
    public void prefix() {
        System.out.print(label);

        for (int count = 0; count < arity; ++count) {
            //if(root.get_child(count)!=null)
            children[count].prefix();

        }//endfor_count
    }
/******************************************************************************/

/******************************************************************************/
    /**
     * @return Returns the heuristic as a string in prefix notation.
     */
    public String toString() {
        heu = new String();
        printInd(this);

        return heu;

    }
/******************************************************************************/

    /******************************************************************************/
    private String printInd(Node root) {
        if (root.getLabel() != null)
            heu += root.getLabel();

        for (int count = 0; count < root.arity; ++count) {
            printInd(root.getChild(count));
        }//endfor_count

        return heu;
    }
/******************************************************************************/
}
