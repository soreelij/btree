package cs321.btree;

/**
 * TreeObjects represents a single object that contains
 * a generic key object, frequency count, and probe count.
 *
 * @author Natalie Hahle
 * @author Brandon Cardoza
 * @author elijahsorensen
 */
public class TreeObject implements TreeObjectInterface
{
    private int frequencyCount; // The number of occurrences of this TreeObject in the BTree
    private long key; // the value of the TreeObject
    private String sequence;

    /**
     * Create a new TreeObject
     * @param key - TreeObject value
     * @param frequency - Number of occurrences of this TreeObject in the BTree, initially 1
     */
    public TreeObject(long key, int frequency)
    {
        this.frequencyCount = frequency;
        this.key = key;
    }


    @Override
    public void incrementFrequencyCount()
    {
        frequencyCount++;
    }

    @Override
    public int getFrequency()
    {
    	return frequencyCount;
    }

    @Override
    public long getKey()
    {
        return key;
    }

    /**
     * Sets the given <code>key</code> at the provided
     * <code>position</code>.
     *
     * @param key the long key value
     */
    public void setKey(long key)
    {
        this.key = key;
    }


    /**
     * Sets the given frequencyCount with a provided integer
     * <code>position</code>.
     *
     * @param i integer of i
     */
    public void setFrequencyCount(int i)
    {
        this.frequencyCount = i;
    }

    @Override
    public String toString()
    {
        return this.key + "";
    }



    public String toStringDump(int sequenceLength)
    {
        return this.sequence + ": " + this.frequencyCount;
    }

    /**
     * Takes the long value and changes it to a full
     * binary value based on the sequenceLength ex. 00110001
     * and adjusts and adds zeros accordingly
     *
     * @param sequenceLength the length of the sequences
     * @return the String of the binary sequence as 1's or 0's
     */
    public String toStringToBinary(int sequenceLength)
    {
        int numberPairs = sequenceLength; //for four length only
        String binaryString = Long.toBinaryString(key);
        if(binaryString.length() % 2 == 1) //odd number length (with c only)
        {
            binaryString = "0" + binaryString;
        }
        if(binaryString.length() < (numberPairs*2)) //odd number length (with c only)
        {
            for(int i = binaryString.length(); i < numberPairs*2; i++ )
            {
                binaryString = "0" + binaryString;
            }
        }
        return binaryString + "";
    }


    /**
     * Takes in the string binary value and changes it back to
     * the original character sequence
     *
     * @param sequenceLength the length of the sequences
     * @return the String of the character sequence ex. aaagtc
     */
    public String toString(int sequenceLength)
    {
        //binaryString string fully
        String binaryString = toStringToBinary(sequenceLength);

        String reverted = "";
        String binaryPair = "";

        //convert long back into DNA
        //convert into binary
        //convert that back into string sequence

        for(int i = 0; i < sequenceLength*2; i = i+2)
        {
            binaryPair = binaryString.substring(i, i+2);
            if (binaryPair.equals("00")) //all a's
            {
                reverted = reverted + "a";
            }
            if (binaryPair.equals("01")) //one c
            {
                reverted = reverted + "c";
            }
            if (binaryPair.equals("10")) //one g
            {
                reverted = reverted + "g";
            }
            if (binaryPair.equals("11")) //one t
            {
                reverted = reverted + "t";
            }
        }
        this.sequence = reverted;
        //return reverted;
        return this.sequence + ": " + this.frequencyCount;
    }

}
