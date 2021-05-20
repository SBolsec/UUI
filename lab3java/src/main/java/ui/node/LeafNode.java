package ui.node;

/**
 * Same as TreeNode.
 * Just needed a constructor with the same signature.
 */
public class LeafNode extends TreeNode {
    /**
     * Constructor that initializes a leaf node.
     * @param value value of leaf node
     */
    public LeafNode(String value) {
        this.value = value;
        this.attribute = null;
        this.children = null;
    }
}
