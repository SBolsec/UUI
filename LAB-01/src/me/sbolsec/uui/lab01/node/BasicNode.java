package me.sbolsec.uui.lab01.node;

public class BasicNode<S> {
    protected BasicNode<S> parent;
    protected S state;

    public BasicNode(S state, BasicNode<S> parent) {
        super();
        this.state = state;
        this.parent = parent;
    }

    public BasicNode<S> getParent() {
        return parent;
    }

    public S getState() {
        return state;
    }

    public int getDepth() {
        int depth = 1;
        BasicNode<S> current = this.getParent();
        while (current != null) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }

    @Override
    public String toString() {
        return String.format("%s ", state);
    }

    public static <X> String nodePath(BasicNode<X> node) {
        StringBuilder sb = new StringBuilder();
        nodePathRecursive(sb, node);
        return sb.toString();
    }

    private static <X> void nodePathRecursive(StringBuilder sb, BasicNode<X> node) {
        if (node.getParent() != null) {
            nodePathRecursive(sb, node.getParent());
            sb.append("=>\n");
        }
        sb.append(node);
    }
}
