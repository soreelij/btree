package cs321.create;

import cs321.btree.BTree;
import cs321.common.ParseArgumentException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class GeneBankCreateBTree
{

    /** launch the program
     * @param args where:
     * first args: <0/1(no/with Cache)> using a cache or not
     * second args: <degree> degree if tree
     * third args: <gbk_file> the Gene bank file uesd
     * fourth args: <subsequence_length> the sequence length
     * Optional fourth: [<cache_size>] if cache is use the the cache size
     * Optional fifth: [<debug_level>] the level of debugging
     *
     * @author Natalie Hahle
     * @author Eli Sorenesen
     */
    public static void main(String[] args) throws Exception
    {
        GeneBankCreateBTreeArguments argPirates = parseArguments(args);

        BTree beeTree = new BTree(argPirates.getDegree());

        String DNAseq;
        int countInsertions = 0;

        SequenceUtils seq = new SequenceUtils();
        List<String> sequences = null;
        try {
            sequences = seq.getDNASequencesFromGBKGenomeFile(argPirates.getGbkFileName());
        } catch (Exception e) {
            printUsageAndExit("File not found");
        }

        //iterate through subsequences
        for(int i  = 0; i < sequences.size(); i++)
        {
            String sequencez = sequences.get(i);
            //insert into btree from command line args
            while(sequencez.length() >= argPirates.getSubsequenceLength())
            {
                countInsertions++; //counter for insertions
                DNAseq = sequencez.substring(0, argPirates.getSubsequenceLength());
                Long results = ConvertToLong(DNAseq);
                //System.out.println(" " + results + " ");
                beeTree.bTreeInsert(results);
                sequencez = sequencez.replaceFirst(String.valueOf(sequencez.charAt(0)), "");

            }
        }
        String cacheUsing = null;

        if (argPirates.getDebugLevel() == 1) {
            beeTree.dump("dump", argPirates.getSubsequenceLength());
        }
        else
        {
            if(argPirates.isUseCache() == true)
            {
                cacheUsing = " using cache at size: " + argPirates.getCacheSize();
            }
           else //is false
            {
                cacheUsing = " not using cache ";
            }
            System.out.println("--------------------------------------------------------------------");
            System.out.println("Root of BTree: " + beeTree.getRoot());
            System.out.println("Number of insertions: " + countInsertions +" at length sequence length of " + argPirates.getSubsequenceLength());
            System.out.println("Gene Bank File used: " + argPirates.getGbkFileName());
            System.out.println("Degree: " + argPirates.getDegree() +" and"+ cacheUsing);

            System.out.println("--------------------------------------------------------------------");
        }

        beeTree.writeMetadata();

    }


    /**
     * Sets BTree database and creates the database from
     * a file
     * @param tree a File of the tree
     */
    private static void CreateDatabase(File tree) {

        Connection connection = null;

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:GeneBankDB.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("drop table if exists genebank");
            statement.executeUpdate("create table genebank (sequence string, frequency integer)");

            Scanner fileScan = new Scanner(tree);

            while (fileScan.hasNextLine()) {

                String nextLine = fileScan.nextLine();
                Scanner lineScan = new Scanner(nextLine);

                String nextSequence = lineScan.next();
                nextSequence = nextSequence.substring(0, nextSequence.length() - 1);

                String nextFrequency = lineScan.next();

                String nextUpdate = "insert into genebank values('" + nextSequence + "', " + nextFrequency + ")";

                statement.executeUpdate(nextUpdate);

            }

            tree.delete();

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        } catch (FileNotFoundException e) {

            System.err.println(e.getMessage());

        }

    }


    /**
     * Converts the DNA subsequence into a long
     * with binary shifting
     *
     * @param DNASequenceChar the String to be converted
     * @return retVal the binary long of the sequence
     */
    private static long ConvertToLong(String DNASequenceChar)
    {
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


    /**
     * Parses the handles arguments and exceptions
     *
     * @param args String arguments
     */
    private static GeneBankCreateBTreeArguments parseArgumentsAndHandleExceptions(String[] args)
    {
        GeneBankCreateBTreeArguments geneBankCreateBTreeArguments = null;
        try
        {
            geneBankCreateBTreeArguments = parseArguments(args);
        }
        catch (ParseArgumentException e)
        {
            printUsageAndExit(e.getMessage());
        }
        return geneBankCreateBTreeArguments;
    }


    /**
     * Prints error message and exits program
     *
     * @param errorMessage String of error message
     */
    private static void printUsageAndExit(String errorMessage)
    {

        System.exit(1);
    }

    /**
     * Sets up and tests a GeneBankCreateBTree args configurations
     *
     * @param args String arguments
     * @throws ParseArgumentException for parsing exceptions
     */
    public static GeneBankCreateBTreeArguments parseArguments(String[] args) throws ParseArgumentException
    {
        GeneBankCreateBTreeArguments argsTester = null;

            argsTester = new GeneBankCreateBTreeArguments(args);

        return argsTester;
    }

}
