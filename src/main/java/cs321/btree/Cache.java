package cs321.btree;
import java.util.LinkedList;

/**
 * 
 * Cache class includes constructor for a 1-level cache system.
 * Also includes methods for adding, removing, and getting objects, as well as
 * methods for clearing caches and printing stats. Implemented from Project 1 and
 * re-purposed for a BTree.
 * 
 * @author Brandon Cardoza
 * @version Fall 2021
 *
 */
public class Cache{
	
	private LinkedList<BTreeNode> level1Cache;
	private LinkedList<Integer> addresses;
	private int level1Size; //the number of entries in the 1st-level cache
	
	
	/**
	 * Constructor for a 1-level cache
	 * @param size - how many entries can be in the cache at once
	 */
	public Cache(int size)
	{
		this.level1Size = size;
		level1Cache = new LinkedList<BTreeNode>();
		addresses = new LinkedList<Integer>();
	}
	
	/**
	 * Searches the cache for the BTreeNode, moves it to the front if found
	 * @param address - the adress of the BTreeNode to search for
	 * @return - returns the BTreeNode if found, else returns null
	 */
	public BTreeNode getObject(Integer address)
	{
		int index = addresses.indexOf(address);
		
		if (index != -1) //if index is valid
		{
			BTreeNode retVal = level1Cache.get(index);
			
			//moves BTreeNode to front of cache
			level1Cache.remove(retVal);
			level1Cache.addFirst(retVal);
			
			//moves BTreeNode address to front of cache
			addresses.remove(address);
			addresses.addFirst(address);
			return retVal;
		}
		
		return null;
	}
	
	/**
	 * If cache is full, removes last BTreeNode from cache.
	 * @param Object - the BTreeNode being added to the cache
	 */
	public void addObject(BTreeNode Object)
	{
		if (level1Cache.size() == this.level1Size)
		{
			level1Cache.removeLast();
			addresses.removeLast();
		}
		
		//adds BTreeNode and it's address to the front of their respective caches
		level1Cache.addFirst(Object);
		addresses.addFirst(Object.getRafAddress());
	}	
	
	/**
	 * Removes BTreeNode and it's address from their respective caches.
	 * @param toRemove - the BTreeNode being removed
	 */
	public void removeObject(BTreeNode toRemove)
	{
		if (level1Cache.contains(toRemove))
		{
			int index = level1Cache.indexOf(toRemove);
			level1Cache.remove(toRemove);
			addresses.remove(index);
		}
	}
	
	/**
	 * Makes both caches empty
	 */
	public void clearCache()
	{
		level1Cache.clear();
		addresses.clear();
	}

}
