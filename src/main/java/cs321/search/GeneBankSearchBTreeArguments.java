package cs321.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * <code>GeneBankSearchBTreeArguments</code> parses command line
 * arguments for runtime of <code>GeneBankSearchBTree</code>.
 *
 * @version CS 321: Fall 2021
 * @author elijahsorensen
 */
public class GeneBankSearchBTreeArguments {

    private boolean useCache;
    private int cacheSize;

    private RandomAccessFile btreeFile;
    private File queryFile;

    private int debugLevel;

    /**
     * Builds a new <code>GeneBankSearchBTreeArguments</code> with the specified command line arguments
     * and tests their validity.
     *
     * @param args the user-specified command line arguments
     * @throws GeneBankSearchBTreeArgumentException if any argument is invalid
     */
    public GeneBankSearchBTreeArguments(String[] args) throws GeneBankSearchBTreeArgumentException {

       if (args.length < 3) {

           throw new GeneBankSearchBTreeArgumentException("Too few arguments.");

       } else if (args.length > 5) {

           throw new GeneBankSearchBTreeArgumentException("Too many arguments.");

       } else {

           // Correct length discovered

           /* Cache arg */
           try {

               int cacheInput = Integer.parseInt(args[0]);

               if (cacheInput != 0 && cacheInput != 1) {

                   throw new GeneBankSearchBTreeArgumentException("Invalid cache option.");

               } else {

                   if (cacheInput == 0) {
                       useCache = false;
                   } else {

                       if (args.length < 4) {

                           throw new GeneBankSearchBTreeArgumentException("No cache size specified.");

                       }

                       useCache = true;
                   }

               }

           } catch (NumberFormatException e) {

               throw new GeneBankSearchBTreeArgumentException("Cache size is not a valid integer.");

           }

           /* BTree arg */
           try {
               btreeFile = new RandomAccessFile(args[1], "r");
           } catch (FileNotFoundException e) {
               throw new GeneBankSearchBTreeArgumentException("BTree file not found.");
           }

           /* Query arg */
           queryFile = new File(args[2]);

           if (!queryFile.exists()) {

               throw new GeneBankSearchBTreeArgumentException("Query file not found.");

           }

           /* Cache size arg */
           if (useCache) {

               try {

                   cacheSize = Integer.parseInt(args[3]);

                   if (cacheSize < 100 || cacheSize > 500) {

                       throw new GeneBankSearchBTreeArgumentException("Invalid cache size specified.");

                   }

               } catch (NumberFormatException e) {

                   throw new GeneBankSearchBTreeArgumentException("Invalid cache size specified.");

               }

           }

           /* Debug arg */
           if((useCache && args.length == 5) || args.length == 4) {

               try {

                   debugLevel = Integer.parseInt(args[args.length - 1]);

                   if (debugLevel < 0 || debugLevel > 1) {

                       throw new GeneBankSearchBTreeArgumentException("Invalid debug level specified.");

                   }

               } catch (NumberFormatException e) {

                   throw new GeneBankSearchBTreeArgumentException("Debug level is not a valid integer.");

               }

           } else {

               debugLevel = 0;

           }

       }

    }

    /**
     * Return whether the user specified to use a <code>Cache</code> in
     * the provided <code>args</code>.
     *
     * @return if a cache should be used
     */
    public boolean cache() { return useCache; }

    /**
     * Return the user-specified size of the <code>cache</code> to use in
     * the provided <code>args</code>.
     *
     * @return size of cache, -1 if cache is not being used
     */
    public int getCacheSize() {

        if (cache()) {

            return cacheSize;

        } else {

            return -1;

        }

    }

    /**
     * Return reference to the user-specified <code>btreeFile</code> in the
     * provided args.
     *
     * @return existing B tree file
     */
    public RandomAccessFile getBtreeFile() { return btreeFile; }

    /**
     * Return reference to the user-specified <code>queryFile</code> in the
     * provided args.
     *
     * @return existing query file
     */
    public File getQueryFile() { return queryFile; }

    /**
     * Return user-specified <code>debugLevel</code> in the provided
     * args.
     *
     * @return debug level, 0 if no level was specified
     */
    public int getDebugLevel() { return debugLevel; }

}
