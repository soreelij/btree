package cs321.btree;

import java.io.IOException;

/**
 * Interface for a <code>BTreeNode</code> containing
 * generic objects
 *
 * @author elijahsorensen
 * @version CS 321: Fall 2021
 */

public interface BTreeNodeInterface {

    /**
     * Writes this instance of <code>BTreeNode</code> to disk,
     * contained within a <code>RandomAccessFile</code> that
     * represents the <code>BTree</code> in which it is
     * contained.
     *
     * @throws IOException if there is an error writing to the file
     */
    void diskWrite() throws IOException;

    /**
     * Returns the random access address of this <code>BTreeNode</code>.
     *
     * @return the address of this node
     */
    int getRafAddress();

    /**
     * Returns the random access address of the child <code>BTreeNode</code>
     * contained at the given index
     *
     * @param index the position of the address in the array
     * @return the address of the child node
     */
    Integer getChild(int index);

    /**
     * Sets the random access address of the child <code>BTreeNode</code>
     * contained at the given index
     *
     * @param index the position of the address in the array
     * @param address the address to set for the child node
     */
    void setChildAddress(int index, int address);


    /**
     * Sets the given <code>key</code> at the provided
     * <code>position</code>.
     *
     * @param key the element to set
     * @param position the position to set the key
     */
    void setKey(long key, int position);

    /**
     * Adds the given <code>key</code> to the end
     * of this <code>BTreeNode</code>.
     *
     * @param key the element to add
     */
    void addKey(long key);

    /**
     * Gets the <code>key</code> element
     * from the <code>TreeObject</code> stored at the provided
     * <code>index</code> value.
     *
     * @param position the index of the element
     * @return the key value
     */
    long getKey(int position);

    /**
     * Returns a copy of the array containing the <code>keys</code>
     * stored within this <code>BTreeNode</code>.
     *
     * @return the elements stored in
     *         this <code>BTreeNode</code>
     */
    long[] getKeys();

    /**
     * Returns the number of <code>keys</code> stored
     * in this <code>BTreeNode</code>.
     *
     * @return the number of elements contained
     *         within this <code>BTreeNode</code>
     */
    int getNumberOfKeys();

    /**
     * Sets the <code>numberOfKeys</code> contained within this
     * <code>BTreeNode</code>.
     *
     * @param numberOfKeys the updated number of keys stored
     */
    void setNumberOfKeys(int numberOfKeys);

    /**
     * Returns the child node of this <code>BTreeNode</code> stored
     * at the provided <code>position</code>.
     *
     * @param position the index of the child node
     * @return the child node at <code>position</code>
     * @throws IOException if there was an error reading this child node
     *                     from disk
     */
    BTreeNode child(int position) throws IOException;


    /**
     * Sets the <code>child</code> node at the given index to a provided
     * <code>BTreeNode</code>.
     *
     * @param child the node to set
     * @param position the position to set
     */
    void setChild(BTreeNode child, int position);

    /**
     * Sets if this <code>BTreeNode</code> is a leaf node.
     *
     * @param isLeaf if this node is a leaf
     */
    void leaf(boolean isLeaf);

    /**
     * Returns <code>true</code> if this <code>BTreeNode</code>
     * is a leaf.
     *
     * @return <code>true</code> if this node is a leaf
     */
    boolean isLeaf();

    /**
     * Returns a String representation of this <code>BTreeNode</code>
     * and the elements that it contains
     *
     * @return contained elements as String
     */
    String toString();

}
