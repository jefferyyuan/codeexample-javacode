package com.blogspot.illyakeeplearning.suffixtree;

public class Suffix {
    private Node originNode;
    private int beginIndex;
    private int endIndex;

    public Suffix(Node originNode, int beginIndex, int endIndex) {
        this.originNode = originNode;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
    }

    public boolean isExplicit() {
        return beginIndex > endIndex;
    }

    public boolean isImplicit() {
        return endIndex >= beginIndex;
    }

    public void canonize() {
        if (!isExplicit()) {
            Edge edge = originNode.findEdge(originNode.charAt(beginIndex));

            int edgeSpan = edge.getSpan();
            while (edgeSpan <= getSpan()) {
                beginIndex += edgeSpan + 1;
                originNode = edge.getEndNode();
                if (beginIndex <= endIndex) {
                    edge = edge.getEndNode().findEdge(originNode.charAt(beginIndex));
                    edgeSpan = edge.getSpan();
                }
            }
        }
    }

    public int getSpan() {
        return endIndex - beginIndex;
    }

    public Node getOriginNode() {
        return originNode;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void incBeginIndex() {
        beginIndex++;
    }

    public void changeOriginNode() {
        originNode = originNode.getSuffixNode();
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void incEndIndex() {
        endIndex++;
    }
}
