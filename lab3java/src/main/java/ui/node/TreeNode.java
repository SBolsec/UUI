package ui.node;

import java.util.*;

/**
 * Represents a node in the decision tree.
 */
public class TreeNode {
    /** Attribute that this node represents, null if this is a leaf node. */
    protected String attribute;
    /** Value that this node represents, null if inner node. */
    protected String value;
    /** Children nodes of this node. Key is value that has to be used to get to a child. */
    protected Map<String, TreeNode> children;

    /**
     * Default constructor.
     */
    public TreeNode() {
    }

    /**
     * Constructor that initializes a inner node (or root node).
     * It sets the given attribute and prepares a map for children.
     * @param attribute attribute
     */
    public TreeNode(String attribute) {
        this.attribute = attribute;
        this.value = null;
        this.children = new HashMap<>();
    }

    /**
     * Returns attribute.
     * @return attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Returns value.
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns unmodifiable map of children.
     * @return unmodifiable map of children
     */
    public Map<String, TreeNode> getChildren() {
        return Collections.unmodifiableMap(children);
    }

    /**
     * Adds a child node using the given value as the key in the map.
     * @param value value used to traverse to child node
     * @param node child node
     */
    public void addChild(String value, TreeNode node) {
        children.put(value, node);
    }

    /**
     * Prints all the branches starting from this node.
     */
    public void printBranches() {
        printBranchesRecursive(this,"", 1);
    }

    /**
     * Recursively prints all the branches
     * @param node node in the tree
     * @param above string that is used to create required format
     * @param level depth of the node
     */
    private void printBranchesRecursive(TreeNode node, String above, int level) {
        if (node.attribute == null) { // LeafNode
            System.out.println(above + node.value);
            return;
        }
        for (Map.Entry<String, TreeNode> e : node.children.entrySet()) {
             String newAbove = above + level + ":" + node.attribute + "=" + e.getKey() + " ";
             printBranchesRecursive(e.getValue(), newAbove, level+1);
        }
    }
}
