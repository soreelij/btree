package cs321.btree;

import java.io.*;
import java.util.LinkedList;

/** Creates a bTree with BTreeNodes that have a TreeObject as the key,
 *  and a pointer to child nodes. Includes methods to get the number of nodes,
 *  get the content of a node, and print out all of the nodes.
 *
 * @author Brandon Cardoza
 * @author elijahsorensen
 * @author Natalie Hahle
 *
 * @version Fall 2021
 *
 */
public class BTree implements BTreeInterface
{

	private BTreeNode root;
	private int degree;

	private final long METADATA_ADDRESS = 0;

	private int rootAddress; // address of root node for metadata
	private int nextAddress; // the next address in the random access file
	private int nodeSize; // largest size of a btree node, like in the hw

	private final int BYTES_PER_KEY = 12;
	private final int BYTES_PER_CHILD = 4;
	private final int BYTES_IN_NODE = 8;

	private RandomAccessFile raf;

	private Cache cache;
	private boolean usingCache = false;

	/** Creates a new BTree with degree given as a parameter. Creates a new raf,
	 * writes the degree to the start of it, allocates a root node and writes that to the raf.
	 * @param degree - the degree of the BTree, used to find the nodeSize
	 * @throws IOException - thrown if there is an error reading from the raf
	 */
	public BTree(int degree) throws IOException //without cache
	{
		raf = new RandomAccessFile(new File("test"), "rw");

		this.degree = degree;
		nextAddress = 0;

		raf.writeInt(degree); // writes the degree of the BTree to the start of the raf
		raf.writeInt(nextAddress); // writes the root's initial address

		nextAddress = nextAddress + 8;
		nodeSize = ((2 * degree - 1) * BYTES_PER_KEY) + ((2 * degree) * BYTES_PER_CHILD) + BYTES_IN_NODE;

		BTreeNode x = new BTreeNode(raf, degree, nextAddress, true); // allocates a node
		nextAddress = nextAddress + nodeSize; // increments the next address in the raf

		//DISK-WRITE(x)
		x.diskWrite();

		this.root = x;
	}

	/** Creates a new BTree with degree and given as a parameter. Uses a cache of size
	 * specified as a parameter. Creates a new cache and raf, writes the degree to
	 * the start of the raf, allocates a root node, writes that to the raf and cache.
	 * @param degree - the degree of the BTree, used to find the nodeSize
	 * @param cacheSize - the size of the cache
	 * @throws IOException - thrown if there is an error reading from the raf
	 */
	public BTree(int degree, int cacheSize) throws IOException //using cache
	{
		cache = new Cache(cacheSize);
		usingCache = true;

		raf = new RandomAccessFile(new File("test"), "rw");

		this.degree = degree;
		nextAddress = 0;

		raf.writeInt(degree); //writes the degree of the BTree to the start of the raf
		raf.writeInt(nextAddress); // writes the root's initial address

		nextAddress = nextAddress + 8;
		nodeSize = ((2 * degree - 1) * BYTES_PER_KEY) + ((2 * degree) * BYTES_PER_CHILD) + BYTES_IN_NODE;

		BTreeNode x = new BTreeNode(raf, degree, nextAddress, true); //allocates a node
		nextAddress = nextAddress + nodeSize; //increments the next address in the raf

		//DISK-WRITE(x)
		x.diskWrite();
		cache.addObject(x); //adds node to the cache

		this.root = x;
	}

	/**
	 * Initializes a new <code>BTree</code> from a predetermined <code>RandomAccessFile</code>
	 * containing all metadata and <code>BTreeNode</code> information.
	 *
	 * @param raf file containing this <code>BTree</code>
	 * @throws IOException if there is an error reading from the file
	 */
	public BTree(RandomAccessFile raf) throws IOException {

		usingCache = false;

		this.raf = raf;

		this.degree = raf.readInt();
		nextAddress = raf.readInt();

		nodeSize = ((2*degree - 1)* BYTES_PER_KEY) + ((2*degree)* BYTES_PER_CHILD) + BYTES_IN_NODE;

		BTreeNode x = new BTreeNode(degree, raf, nextAddress);

		this.root = x;

	}

	/**
	 * Initializes a new <code>BTree</code> from a predetermined <code>RandomAccessFile</code>
	 * containing all metadata and <code>BTreeNode</code> information, utilizing a <code>Cache</code>
	 * for searching and storage purposes.
	 *
	 * @param raf file containing this <code>BTree</code>
	 * @param cacheSize size to initialize the <code>Cache</code>
	 * @throws IOException  if there is an error reading from the file
	 */
	public BTree(RandomAccessFile raf, int cacheSize) throws IOException {

		cache = new Cache(cacheSize);
		usingCache = true;

		this.raf = raf;

		this.degree = raf.readInt();
		nextAddress = raf.readInt();

		nodeSize = ((2*degree - 1)* BYTES_PER_KEY) + ((2*degree)* BYTES_PER_CHILD) + BYTES_IN_NODE;

		BTreeNode x = new BTreeNode(degree, raf, nextAddress);

		this.root = x;

	}

	@Override
	public void writeMetadata() throws IOException {

		raf.seek(METADATA_ADDRESS);

		raf.writeInt(degree);
		raf.writeInt(root.getRafAddress());

	}

	@Override
	public BTreeNode bTreeSearch(BTreeNode node, long key) throws IOException
	{
		int i = 0;

		//walks through node checking if key is not
		while(i < node.getNumberOfKeys() && key > node.getKey(i))
		{
			i++;
		}

		//returns node if found
		if (i < node.getNumberOfKeys() && key == node.getKey(i))
		{
			return node;
		}

		//if key not found and node is leaf, returns null
		else if (node.isLeaf())
		{
			return null;
		}

		//if key is not found and node is not leaf, reads child node from raf
		else
		{
			if(usingCache)
			{
				BTreeNode thisNode = cache.getObject(node.getChild(i));

				if(thisNode != null)
				{
					return bTreeSearch(thisNode, key); //searches child node for key
				}
				else
				{
					//DISK-READ(node.child(i)) and add node.child(i) to cache
					BTreeNode nodeChildI = new BTreeNode(degree, raf, node.getChild(i));
					cache.addObject(nodeChildI);
					return bTreeSearch(nodeChildI, key);
				}
			}
			else
			{
				// DISK-READ(node.child(i));
				BTreeNode nodeChildI = new BTreeNode(degree, raf, node.getChild(i));
				return bTreeSearch(nodeChildI, key);
			}

		}
	}

	@Override
	public void bTreeSplitChild(BTreeNode node, int i, BTreeNode y) throws IOException
	{
		BTreeNode z = new BTreeNode(raf, degree, nextAddress, false); //allocates a node
		nextAddress = nextAddress + nodeSize;

		z.leaf(y.isLeaf());
		z.setNumberOfKeys(degree - 1);

		for (int j = 0; j < degree - 1; j++)
		{
			z.setKey(y.getKey(j + degree), j);
			z.setFrequency(j, y.getFreq(j+degree));
		}

		if(!y.isLeaf())
		{
			for (int j = 0; j < degree; j++)
			{
				z.setChild(y.child(j+degree), j);
			}
		}

		y.setNumberOfKeys(degree - 1);
		for(int j = node.getNumberOfKeys(); j >= i + 1; j--)
		{
			node.setChild(node.child(j), j+1);
		}

		node.setChild(z, i+1);
		for(int j = node.getNumberOfKeys() - 1; j >= i; j--)
		{
			node.setKey(node.getKey(j), j+1);
		}

		node.setKey(y.getKey(degree-1), i);
		node.setNumberOfKeys(node.getNumberOfKeys() + 1);

		if(usingCache)
		{
			if(cache.getObject(node.getRafAddress()) != null)
			{
				cache.removeObject(node);
			}
			cache.addObject(node);
			if(cache.getObject(y.getRafAddress()) != null)
			{
				cache.removeObject(y);
			}
			cache.addObject(y);
			if(cache.getObject(z.getRafAddress()) != null)
			{
				cache.removeObject(z);
			}
			cache.addObject(z);
		}
		// DISK-WRITE(node);
		node.diskWrite();
		// DISK-WRITE(y);
		y.diskWrite();
		// DISK-WRITE(z);
		z.diskWrite();
	}

	@Override
	public void bTreeInsert(long key) throws IOException
	{
		BTreeNode r = root;

		//if root node is full
		if(r.getNumberOfKeys() == 2*degree -1)
		{
			BTreeNode s = new BTreeNode(raf, degree, nextAddress, false); //allocate node
			nextAddress = nextAddress + nodeSize; //increment nextAddress in raf
			root = s; //update root node
			s.setChildAddress(0, r.getRafAddress()); //set child of s to previous root
			bTreeSplitChild(s, 0, r); //split previous root
			bTreeInsertNonFull(s, key); //insert key into new root
		}
		//if root node is not full
		else
		{
			bTreeInsertNonFull(r, key); //insert key into root
		}

	}

	@Override
	public void bTreeInsertNonFull(BTreeNode node, long key) throws IOException
	{
		int i = node.getNumberOfKeys()- 1;

		if(node.isLeaf())
		{
			//if node is already in BTree
			if(node.insertDuplicate(key))
			{
				node.diskWrite();
				return;
			}

			//shift keys to insert new key into correct spot
			while(i >= 0 && key < node.getKey(i))
			{
				node.setKey(node.getKey(i), i+1);
				i--;
			}

			node.setKey(key, i+1); //set key at correct spot
			node.setNumberOfKeys(node.getNumberOfKeys() + 1); //increment number of keys

			//add to cache if USING CACHE
			if(usingCache)
			{
				cache.removeObject(node);
				cache.addObject(node);
			}
			// DISK-WRITE(node)
			node.diskWrite();
		}

		// if node is not a leaf
		else
		{
			while (i >= 0 && key < node.getKey(i))
			{
				i--;
			}

			//if node is already in BTree
			if(node.insertDuplicate(key))
			{
				node.diskWrite();
				return;
			}

			i++;

			BTreeNode nodeChildI = null;
			if(usingCache)
			{
				BTreeNode thisNode = cache.getObject(node.getChild(i));

				if(thisNode != null)
				{
					nodeChildI = thisNode;
				}
				else
				{
					//DISK-READ(node.child(i)) and add node.child(i) to cache
					nodeChildI = new BTreeNode(degree, raf, node.getChild(i));
					cache.addObject(nodeChildI);
				}
			}
			else
			{
				// DISK-READ(node.child(i));
				nodeChildI = node.child(i);
			}
			//DISK-READ(node.child(i));
			if (nodeChildI.getNumberOfKeys() == 2*degree - 1)
			{
				bTreeSplitChild(node, i, nodeChildI);

				if(node.getKey(i) == key)
				{
					node.incrementKeyFreq(i);
					node.diskWrite();
					return;
				}

				if (key > node.getKey(i))
				{
					i++;
					nodeChildI = node.child(i);
				}

			}

			bTreeInsertNonFull(nodeChildI, key);
		}
	}

	@Override
	public BTreeNode getRoot()
	{
		return root;
	}

	@Override
	public int getNumberOfNodes(BTreeNode x) throws IOException
	{
		int numberOfNodes = 0;

		if(root == null)
		{
			return 0;
		}

		if(x.isLeaf())
		{
			return 1;
		}

		else
		{
			numberOfNodes = 1;
			for(int i = 0; i < x.getNumberOfKeys() + 1; i++)
			{
				BTreeNode xChildI = new BTreeNode(degree, raf, x.getChild(i));
				numberOfNodes += getNumberOfNodes(xChildI);
			}
		}
		return numberOfNodes;
	}

	@Override
	public BTreeNode getArrayOfNodeContentsForNodeIndex(int i) throws IOException
	{
		LinkedList<Integer> addedAddresses = new LinkedList<Integer>();
		LinkedList<Integer> BFS = new LinkedList<Integer>();

		addedAddresses.add(root.getRafAddress());
		BFS.add(root.getRafAddress());

		while (addedAddresses.size() <= i)
		{
			BTreeNode toRead = new BTreeNode(degree, raf, BFS.removeFirst());
			if(!toRead.isLeaf())
			{
				for(int j = 0; j <= toRead.getNumberOfKeys(); j++)
				{
					addedAddresses.add(toRead.getChild(j));
					BFS.add(toRead.getChild(j));
				}
			}
		}
		return new BTreeNode(degree, raf, addedAddresses.get(i));
	}

	@Override
	public void dump(String filename, int sequenceLength) throws FileNotFoundException {
		PrintStream ps = new PrintStream(filename);
		try {
			root.dumpNode(ps, sequenceLength);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.setOut(ps);
		ps.close();
	}

}
