package org.twig.syntax.parser.node;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.Compilable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents something in the AST.
 */
public class Node implements Compilable {
    // The nodes belonging to this node
    protected ArrayList<Node> nodes;
    // The attributes belonging to this node
    protected HashMap<String, String> attributes;
    // The line the node begins on
    protected Integer line;
    // TODO Find out what this thing does
    private String tag;

    public Node(ArrayList<Node> nodes, HashMap<String, String> attributes, Integer line, String tag) {
        this.nodes = nodes;
        this.attributes = attributes;
        this.line = line;
        this.tag = tag;
    }

    public Node(Integer line) {
        this.nodes = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.line = line;
    }

    /**
     * Compiles the node to Java code
     *
     * @param compiler The compiler
     */
    public void compile(ClassCompiler compiler) {
        this.nodes.forEach((node) -> node.compile(compiler));
    }

    /**
     * Set an attribute
     *
     * @param name The name of the attribute
     * @param attribute The attribute
     */
    public void putAttribute(String name, String attribute) {
        attributes.put(name, attribute);
    }

    /**
     * Returns true if the attribute with the given name exists
     *
     * @param name The name of the attribute
     * @return Whether the attribute exists or not
     */
    public Boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Get an attribute by its name
     *
     * @param name The name of the attribute
     * @return The attribute
     */
    public String getAttribute(String name) {
        if (!attributes.containsKey(name)) {
            throw new RuntimeException(
                    String.format("Attribute \"%s\" does not exist for Node \"%s\".", name, getClass().toString())
            );
        }

        return attributes.get(name);
    }

    /**
     * Removes an attribute by name
     *
     * @param name The name of the attribute to remove
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * Add a node at a specific index
     *
     * @param name The name of the node
     * @param node The node
     */
    public void addNode(Integer index, Node node) {
        nodes.add(index, node);
    }

    /**
     * Add a node
     *
     * @param node The node
     */
    public void addNode(Node node) {
        nodes.add(node);
    }

    /**
     * Returns true if the node with the given index exists
     *
     * @param name The index of the node
     * @return Whether the node exists or not
     */
    public Boolean hasNode(Integer index) {
        return nodes.size() > index;
    }

    /**
     * Get a node by its name
     *
     * @param index The index of the node
     * @return The node
     */
    public Node getNode(Integer index) {
        if (!hasNode(index)) {
            throw new RuntimeException(
                    String.format("Node \"%d\" does not exist for Node \"%s\".", index, getClass().toString())
            );
        }

        return nodes.get(index);
    }

    /**
     * Get all child nodes
     * @return All child nodes
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Removes a node by its index
     *
     * @param index The name of the node to remove
     */
    public void removeNode(Integer index) {
        nodes.remove(index);
    }

    /**
     * Gets the line the node is from
     *
     * @return The line
     */
    public Integer getLine() {
        return line;
    }

    /**
     * Sets the line the node is from
     *
     * @param line The line
     */
    public void setLine(Integer line) {
        this.line = line;
    }

    /**
     * Gets the tag name the node is associated with
     *
     * @return The tag name
     */
    public String getNodeTag() {
        return tag;
    }

    /**
     * Set the tag name the node is associated with
     *
     * @param tag The tag name
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
}
