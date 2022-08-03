package cs321.btree;

import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Generic <code>BTreeNode</code>. Each <code>BTreeNode</code>
 * contains an array of keys stored within <code>TreeObjects</code>,
 * and pointers to its parent and child nodes.
 *
 * @author elijahsorensen
 * @author Brandon Cardoza
 * @version CS 321 - Fall 2021
 */
public class BTreeNode implements BTreeNodeInterface {

    // Keys stored in this node (non-decreasing order)
    private TreeObject[] keys;
    private Integer[] children;

    // Number of total keys stored in this node
    private int numberOfKeys;

    private boolean leaf;

    private RandomAccessFile raf;
    private int rafAddress;
    private int degree;

    /**
     * Builds a new <code>BTreeNode</code> with the given parameters.
     *
     * @param raf - the file where the node is stored
     * @param degree - the degree of the <code>BTree</code> which constructed this node
     * @param address - the location of the node in the <code>raf</code>
     * @param leaf - whether this node is a leaf node
     */
    public BTreeNode(RandomAccessFile raf, int degree, int address, boolean leaf) {

    	this.degree = degree;
    	this.rafAddress = address;
    	this.raf = raf;
    	
        keys = new TreeObject[2 * degree - 1];
        children = new Integer[2 * degree];

        numberOfKeys = 0;

        this.leaf = leaf;
    }

    /**
     * Constructs a <code>BTreeNode</code> from data stored within a
     * pre-existing <code>RandomAccessFile</code>. Acts as DISK-READ.
     *
     * @param degree - the degree of the <code>BTree</code> which constructed this node
     * @param raf - the file where the node's properties are stored
     * @param address - the location of this node in the <code>raf</code>
     * @throws IOException if there is an error reading the node from <code>raf</code>
     */
    public BTreeNode(int degree, RandomAccessFile raf, int address) throws IOException {
    	
    	this.degree = degree;
    	this.rafAddress = address;
    	this.raf = raf;
    	
    	keys = new TreeObject[2 * degree - 1];
        children = new Integer[2 * degree];
    	
    	raf.seek(address); //the location to start reading from on the raf to build the BTreeNode

        //raf.read for each instance variable written with disk-write
        //THE READ ORDER NEEDS TO BE IN THE SAME ORDER AS WRITTEN TO DISK

    	this.numberOfKeys = raf.readInt();

    	for(int i = 0; i < numberOfKeys; i++) {

    		long key = raf.readLong();
    		int freq = raf.readInt();
    		
    		this.keys[i] = new TreeObject(key, freq);

    	}
    	
    	if(raf.readInt() == 1) {

    		leaf = true;

    	} else {

    		leaf = false;

    		for(int i = 0; i < numberOfKeys + 1; i++) {
        		this.children[i] = raf.readInt();
        	}

    	}

    }


    /** Sets the frequency of the TreeObject at position i to the frequency f.
     * @param i - The position of the TreeObject in the keys array to change the frequency of
     * @param f - The new frequency to change the TreeObject to
     */
    public void setFrequency(int i, int f) 
    {
        keys[i].setFrequencyCount(f);
    }
    
    /**
     * Checks if the keys are equal to each other
     * for an accurate frequency count
     *
     * @param key the key value that is being compared to
     * @returns a true or false of the key is equal or not
     */
    public boolean insertDuplicate(long key)
    {
        for(int i = 0; i< numberOfKeys; i++)
        {
            if(key == keys[i].getKey()) {
                keys[i].incrementFrequencyCount();
                return true;
            }
        }
        return false;
    }

    /** Increments a TreeObject's frequency count by 1.
     * @param position - the TreeObject at this position will have it's frequency incremented
     */
    public void incrementKeyFreq(int position)
    {
        keys[position].incrementFrequencyCount();

    }

    @Override
    public void diskWrite() throws IOException {

    	raf.seek(rafAddress); //finds the next open space in the raf and starts writing to that point

        // Write each instance variable of this node to the raf

    	raf.writeInt(this.numberOfKeys);

    	for(int i = 0; i < numberOfKeys; i++) {

    		raf.writeLong(keys[i].getKey());
    		raf.writeInt(keys[i].getFrequency());

    	}

    	if(this.leaf) {

    		raf.writeInt(1);

    	} else {

    		raf.writeInt(0);

    		for(int i = 0; i < numberOfKeys + 1; i++) {
        		raf.writeInt(children[i]);
        	}

    	}

    }

    @Override
    public int getRafAddress() { return this.rafAddress; }

    @Override
    public Integer getChild(int index) { return children[index]; }

    @Override
    public void setChildAddress(int index, int address) {
        children[index] = address;
    }

    @Override
    public void setKey(long key, int position) {

        TreeObject newObject = new TreeObject(key, 1);
        keys[position] = newObject;

    }

    @Override
    public void addKey(long key) {

        if (numberOfKeys == keys.length) {

            // Expand capacity
            keys = Arrays.copyOf(keys, keys.length * 2);

        }

        keys[keys.length - 1] = new TreeObject(key, 1);
        numberOfKeys++;
    }

    @Override
    public long getKey(int position) {

        long key = keys[position].getKey();

        return key;

    }

    /** Returns the frequency of a TreeObject
     * @param position - the TreeObject at position is the TreeObject being checked
     * @return - the frequency of the TreeObject at position
     */
    public int getFreq(int position) 
    {
        return keys[position].getFrequency();
    }

    @Override
    public long[] getKeys() {

        long[] output = new long[keys.length];

        for (int i = 0; i < output.length; i++) {

            output[i] = keys[i].getKey();

        }

        return output;

    }

    @Override
    public int getNumberOfKeys() {
        return numberOfKeys;
    }

    @Override
    public void setNumberOfKeys(int numberOfKeys) { this.numberOfKeys = numberOfKeys; }

    @Override
    public BTreeNode child(int position) throws IOException 
    {
    	return new BTreeNode(degree, raf, children[position]); 
    }

    @Override
    public void setChild(BTreeNode child, int position) 
    {
        children[position] = child.rafAddress;
    }

    @Override
    public void leaf(boolean isLeaf) {

        this.leaf = isLeaf;

    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }


    @Override
    public String toString() {

        String output = "[";

        for (int i = 0; i < numberOfKeys; i++) {

            if(keys[i] != null) {
                output += keys[i].toString();
            }

            if (i != numberOfKeys - 1) {

                output += ", ";

            }

        }


        output += "]";

        return output;

    }

    /**
     * Dumps the contents of the BTree in a file and
     * appends the nodes to the printStream in and in-order
     * traversal
     *
     * @param printStream the PrintStream to append too
     * @param sequenceLength the length of the sequence
     */
    public void dumpNode(PrintStream printStream, int sequenceLength) throws IOException {
        try {
            if(this.leaf == true)
            {
                for(int i = 0; i < numberOfKeys; i++)
                {
                    printStream.append(keys[i].toString(sequenceLength));
                    printStream.append("\n");
                }
            }
            else {
                for(int i = 0; i < numberOfKeys; i++)
                {

                    BTreeNode node = child(i);
                    node.dumpNode(printStream, sequenceLength);

                    printStream.append(keys[i].toString(sequenceLength));
                    printStream.append("\n");
                }
                BTreeNode nodeBig = child(numberOfKeys);
                nodeBig.dumpNode(printStream, sequenceLength);
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }
}
