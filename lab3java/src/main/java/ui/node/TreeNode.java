package ui.node;

import java.util.*;

public class TreeNode {
    protected String attribute;
    protected String value;
    protected Map<String, TreeNode> children;

    public TreeNode() {
    }

    public TreeNode(String attribute) {
        this.attribute = attribute;
        this.value = null;
        this.children = new HashMap<>();
    }

    public TreeNode(String attribute, String value, TreeNode parent, Map<String, TreeNode> children) {
        this.attribute = attribute;
        this.value = value;
        this.children = children;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, TreeNode> children) {
        this.children = children;
    }

    public void addChild(String value, TreeNode node) {
        children.put(value, node);
    }

    public void printBranches() {
        printBranchesRecursive(this,"", 1);
    }

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
