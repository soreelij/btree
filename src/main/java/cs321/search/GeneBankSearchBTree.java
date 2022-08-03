package cs321.search;

import cs321.btree.BTree;
import cs321.btree.BTreeNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <code>GeneBankSearchBTree </code>searches a <code>BTree</code> which contains
 * DNA Sequences for subsequences provided in a valid <code>queryFile</code> and prints
 * results of sequence frequency to the standard output stream.
 *
 * @version CS 321: Fall 2021
 * @author elijahsorensen
*/
public class GeneBankSearchBTree {

    private static GeneBankSearchBTreeArguments arguments;

    private BTree bTree;
    private File queryFile;

    private QueryBuilder builder;


    public static void main(String[] args) {

        GeneBankSearchBTree gbSearch;

        try {

            arguments = new GeneBankSearchBTreeArguments(args);

            gbSearch = new GeneBankSearchBTree(arguments);

        } catch (GeneBankSearchBTreeArgumentException e) {

            System.err.println(e.getMessage());

        } catch (IOException e) {

            System.err.println(e.getMessage());

        }

    }

    /**
     * Creates a new <code>GeneBankSearchBTree</code> from the arguments provided in
     * <code>main</code> and retrieves required references to complete a successful
     * search.
     *
     * @param arguments the argument parser
     * @throws IOException if there is an error reading the <code>BTree</code> from disk
     */
    public GeneBankSearchBTree(GeneBankSearchBTreeArguments arguments) throws IOException {

        if (arguments.cache()) {
            bTree = new BTree(arguments.getBtreeFile(), arguments.getCacheSize());
        } else {
            bTree = new BTree(arguments.getBtreeFile());
        }

        queryFile = arguments.getQueryFile();
        builder = new QueryBuilder(queryFile);

        search();

    }

    /** Search the BTree **/
    private void search() throws IOException {

        ArrayList<String> queryList = builder.getQueryList();

        for (String query : queryList) {

            long nextKey = ConvertToLong(query);

            BTreeNode nextNode = bTree.bTreeSearch(bTree.getRoot(), nextKey);

            if (nextNode != null) {

                int i = 0;

                while (nextNode.getKey(i) != nextKey) {
                    i++;
                }

                System.out.println(query + ": " + nextNode.getFreq(i));

            }

        }

    }

    /** Convert a sequence to a long for searching purposes **/
    private long ConvertToLong(String DNASequenceChar) {
        long retVal = 0;

        for(int i = 0; i < DNASequenceChar.length(); i++)
        {
            char Dna = DNASequenceChar.charAt(i);
            retVal = retVal << 2;

            if(Dna == 'A' || Dna == 'a')
            {
                retVal = retVal + 0;
            }
            if(Dna == 'C' || Dna == 'c')
            {
                retVal = retVal + 1;
            }
            if(Dna == 'T' || Dna == 't')
            {
                retVal = retVal + 3;
            }
            if(Dna == 'G' || Dna == 'g')
            {
                retVal = retVal + 2;
            }
        }
        return retVal;
    }

}

