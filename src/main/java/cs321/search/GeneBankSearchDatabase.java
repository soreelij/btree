package cs321.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <code>GeneBankSearchDatabase</code> uses an input query file and reference to an
 * existing SQLite database, populated with GeneBank data, to search the database
 * and output resultant queries of all DNA sequence substrings within the dataset.
 *
 * @version CS 321: Fall 2021
 * @author elijahsorensen
 */
public class GeneBankSearchDatabase {

    private static GeneBankSearchDatabaseArguments arguments;
    private static File queryFile;
    private static QueryBuilder builder;

    public static void main(String[] args) {

        try {

            arguments = new GeneBankSearchDatabaseArguments(args);
            queryFile = arguments.getQueryFile();

        } catch (SearchDatabaseArgumentException e) {

            printUsage();

        }

        Connection connection = null;

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:GeneBankDB.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            /* SEARCH DATABASE */
            builder = new QueryBuilder(queryFile);

            ArrayList<String> queries = builder.getQueryList();

            for (String query : queries) {

                ResultSet rs = statement.executeQuery("select * from genebank where sequence like '%" + query + "%'");

                int nextFrequency = 0;

                while (rs.next()) {

                    nextFrequency += rs.getInt("frequency");

                }

                System.out.println(query + ": " + nextFrequency);

            }

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        } catch (FileNotFoundException e) {

            System.err.println(e.getMessage());

        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e) {

                System.err.println(e.getMessage());

            }

        }

    }

    /* Print program usage */
    private static void printUsage() {
        System.out.println("Usage: <path_to_SQLite_database> <query_file> [<debug_level>]");
    }

}
