#
#  *This class defines the structure for the expression tree which will be used
#  *to represent each element of the genetic programming population.
#  *
#  *Nelishia Pillay
#  *11 September 2016
#  

#
#  * This class defines the structure to store the heuristic.
#
from typing import List


class Node(object):
    # Data elements
    # Stores the label for the node, e.g.+, if.     
    label: str

    # Stores the node arity,i.e. the number of arguments, e.g. the + operator takes
    # two arguments and so has an arity of 2. 
    arity: int

    # Stores the children for node, forming the next layer of the expression tree.
    children: List["Node"]

    # Stores the index for the children array. Is incremented each time a child is
    # added.
    index: int

    # Strong typing is used and the type of the node is stored.
    type_: str

    # Stores the heuristic in prefix notation
    heu: str

    def __init__(self):
        self.children = None

    #
    #      * Adds a child to the node at a particular position, e.g.second child.
    #      *
    #      * @param child Child node to be added to the node.
    #      * @param pos   The position at which to add the child, e.g. first child.
    #      
    def add_child(self, child, pos=-1):
        if pos == -1:
            if self.children is None:
                self.children = [None] * self.arity
                self.index = 0
                self.children[self.index] = child
                self.index += 1
            else:
                self.children[self.index] = child
                self.index += 1
        else:
            if self.children is None:
                self.children = [None] * self.arity
            self.children[pos] = child

    #
    #      * @return Returns the type of the node.
    #      
    def get_type(self):
        return self.type_

    #
    #      * Sets the type of the node.
    #      *
    #      * @param type Specifies the node type e.g. integer.
    #      
    def set_type(self, type_):
        self.type_ = type_

    #      * @return Returns the label of the node.
    #      
    def get_label(self):
        return self.label

    # Get Methods
    #
    #      * Specifies the label of the node.
    #      *
    #      * @param label Specifies the label of the node, e.g. +.
    #      
    def set_label(self, label):
        self.label = label

    #
    #      * @return Returns the arity of the node.
    #      
    def get_arity(self):
        return self.arity

    #
    #      * Sets the arity of the node.
    #      *
    #      * @param arity An integer value specifying the arity of the node.
    #      
    def set_arity(self, arity):
        self.arity = arity

    #
    #      * @param pos The position of the child, e.g. first child.
    #      * @return Returns the child, i.e. node, at the specified position.
    #      
    def get_child(self, pos):
        # Returns the child at the position in the array specified by "pos".
        if self.children is not None and len(self.children):
            return self.children[pos]
        else:
            return None

    #
    #      * @return Returns the children of the node in an array of type <tt>Node</tt>.
    #      
    def get_children(self):
        return self.children

    #
    #      * Deletes the children of the node.Is used to prune the tree when the offspring
    #      * exceeds the maximum offspring depth.
    #      
    def clear_children(self):
        self.children = None

    #
    #      * Displays the heuristic in prefix notation.
    #      
    def prefix(self):
        print(self.label)
        count = 0
        while count < self.arity:
            # if(root.get_child(count)!=null)
            self.children[count].prefix()
            count += 1

    def __str__(self):
        self.heu = ''
        self.get_str_with_children(self)
        return self.heu

    def get_str_with_children(self, root):
        if root.get_label() is not None:
            self.heu += root.get_label()
        count = 0
        while count < root.arity:
            self.get_str_with_children(root.get_child(count))
            count += 1
        return self.heu
