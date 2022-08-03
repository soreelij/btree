package cs321.create;
import cs321.common.ParseArgumentException;

/**
 * <code>GeneBankCreateBTreeArguments</code> parses command line
 * arguments for runtime of <code>GeneBankCreateBTree</code>.
 *
 * @author Natalie Hahle
 *
 * @version Fall 2021
 */
public class GeneBankCreateBTreeArguments
{
    private final boolean useCache;
    private final int degree;
    private final String gbkFileName;
    private final int subsequenceLength;
    private final int cacheSize;
    private final int debugLevel;

    /**
     * Builds a new <code>GeneBankCreateBTreeArguments</code> with the specified command line arguments
     * and tests their validity.
     *
     * @param args the user-specified command line arguments
     * @throws ParseArgumentException if any argument is invalid
     */
    public GeneBankCreateBTreeArguments(String args[]) throws ParseArgumentException
    {
        //args length checks
        if(args.length < 4)
        {
            System.out.println("<0/1(no/with Cache)> <degree> <gbk_file> <subsequence_length> [<cache_size>] [<debug_level>]");
            throw new ParseArgumentException("Too few arguments.");
        }
        if(args.length > 6)
        {
            System.out.println("<0/1(no/with Cache)> <degree> <gbk_file> <subsequence_length> [<cache_size>] [<debug_level>]");
            throw new ParseArgumentException("Too many arguments.");
        }

        //gets the use cache
        int cacheUse = Integer.parseInt(args[0]);
        //creates a boolean for cache or no cache
        //sets cache or not
        boolean usingCache = false;
        if(cacheUse == 0)
        {
            usingCache = false;
        }
        if(cacheUse == 1)
        {
            usingCache = true;
        }
        this.useCache = usingCache;
        //gets degree number
        int degreez;
        degreez = Integer.parseInt(args[1]);
        if(degreez == 0)
        {
            degreez = 156;
        }
        this.degree = degreez;
        //gets filename
        this.gbkFileName = args[2];
        //determines sequence length
        this.subsequenceLength = Integer.parseInt(args[3]);
        if(this.subsequenceLength > 31 || subsequenceLength < 1)
        {

            throw new ParseArgumentException("Sequence length must be between 1 and 31 (inclusive)");
        }

        //optional (needed since final int)
        int debugger;
        int cacheSizes;
        debugger = 0;
        cacheSizes = 0;

        if(args.length > 4)
        {
            if(args.length == 5) //optional field's check for length of five (debug or cache size)
            {
                if(cacheUse == 0)//not using cache so debug
                {
                    debugger = Integer.parseInt(args[4]);
                    if(debugger > 1)
                    {
                        throw new ParseArgumentException("Debug Level is incorrect: 0 or 1.");
                    }
                    cacheSizes = 0;
                }
                if(cacheUse == 1)//using cache and no debug
                {
                    cacheSizes = Integer.parseInt(args[4]);
                    if(cacheSizes < 100 || cacheSizes > 501) //cache incorrect size
                    {
                        throw new ParseArgumentException("Cache size is incorrect. Size: 100-500.");
                    }
                    debugger = 0;
                }
            }
            if(args.length == 6) //full arguments
            {
                cacheSizes = Integer.parseInt(args[4]);
                debugger = Integer.parseInt(args[5]);
                if(debugger > 1 || debugger < 0)
                {
                    throw new ParseArgumentException("Debug Level is incorrect: 0 or 1.");
                }
                if(cacheUse == 1) {
                    if (cacheSizes < 100 || cacheSizes > 501) //cache incorrect size
                    {
                        throw new ParseArgumentException("Cache size is incorrect. Size: 100-500.");
                    }
                }
            }
        }
        this.debugLevel = debugger;
        this.cacheSize = cacheSizes;
    }


    /**
     * Creates a new <code>GeneBankCreateBTreeArguments</code> using the given arguments
     *
     * @param useCache boolean for using cache or not
     * @param degree degree for BTree
     * @param gbkFileName String of filename
     * @param subsequenceLength sequence length
     * @param cacheSize size of cache if using
     * @param debugLevel level of debugging
     */
    public GeneBankCreateBTreeArguments(boolean useCache, int degree, String gbkFileName, int subsequenceLength, int cacheSize, int debugLevel)
    {
        this.useCache = useCache;
        this.degree = degree;
        this.gbkFileName = gbkFileName;
        this.subsequenceLength = subsequenceLength;
        this.cacheSize = cacheSize;
        this.debugLevel = debugLevel;
    }

    @Override
    public boolean equals(Object obj)
    {
        //this method was generated using an IDE
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        GeneBankCreateBTreeArguments other = (GeneBankCreateBTreeArguments) obj;
        if (cacheSize != other.cacheSize)
        {
            return false;
        }
        if (debugLevel != other.debugLevel)
        {
            return false;
        }
        if (degree != other.degree)
        {
            return false;
        }
        if (gbkFileName == null)
        {
            if (other.gbkFileName != null)
            {
                return false;
            }
        }
        else
        {
            if (!gbkFileName.equals(other.gbkFileName))
            {
                return false;
            }
        }
        if (subsequenceLength != other.subsequenceLength)
        {
            return false;
        }
        if (useCache != other.useCache)
        {
            return false;
        }
        return true;
    }


    /**
     * Return whether the user specified to use a <code>Cache</code> in
     * the provided <code>args</code>.
     *
     * @return useCache if a cache should be used
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     * Return the chosen Degree for the BTree
     * based on the provided <code>args</code>.
     *
     * @return degree of BTree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Return the user-specified String filename
     *
     * @return gbkFilename String or name of the file
     */
    public String getGbkFileName() {
        return gbkFileName;
    }

    /**
     * Return the user-specified length of the sequence
     *
     * @return the length of the desired sequence
     */
    public int getSubsequenceLength() {
        return subsequenceLength;
    }

    /**
     * Return the user-specified size of the cache is using a cache
     *
     * @return cache size
     */
    public int getCacheSize() {
        return cacheSize;
    }

    /**
     * Return the user-specified format for debugging
     *
     * @return debug level 0 or 1
     */
    public int getDebugLevel() {
        return debugLevel;
    }

    @Override
    public String toString()
    {
        return "GeneBankCreateBTreeArguments{" +
                "useCache=" + useCache +
                ", degree=" + degree +
                ", gbkFileName='" + gbkFileName + '\'' +
                ", subsequenceLength=" + subsequenceLength +
                ", cacheSize=" + cacheSize +
                ", debugLevel=" + debugLevel +
                '}';
    }
}
