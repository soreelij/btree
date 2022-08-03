package cs321.btree;
/**
 * This TreeObjectInterface represents a single TreeObject containing
 * a generic key object, frequency count, and probe count.
 * This TreeObject, similar to HashObjects, this object
 * overrides the toString() and equals() and
 * contains a speciality getKey() methods and other various utility methods
 *
 * @author Natalie Hahle
 */
public interface TreeObjectInterface{

    /**
     * Increments the frequency count of a particular object
     */
    public void incrementFrequencyCount();
    
    /**
     * Returns the frequency of a specific TreeObject
     *
     * @return frequency the count of the frequency
     */
    public int getFrequency();

    /**
     * Prints a TreeObject with its necessary values
     * @return the sequence with probe/frequency count
     */
    @Override
    public String toString();

    /**
     * Retrieves and gets the key value of the object
     * @return key value of the TreeObject
     */
    public long getKey();

    /**
     * Sets the key value of the object to the key value
     * @param key
     */
    public void setKey(long key);

}
