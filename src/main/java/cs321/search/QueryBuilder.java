package cs321.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <code>QueryBuilder</code> parses a valid query file and builds
 * an ArrayList containing all queries to search for.
 *
 * @author elijahsorensen
 * @version CS 321: Fall 2021
 */
public class QueryBuilder {

    private ArrayList<String> queryList;

    /**
     * Builds a new <code>QueryBuilder</code> and populates its <code>queryList</code>
     * with all queries contained in the input <code>file</code>.
     *
     * @param file query file
     * @throws FileNotFoundException if the query file does not exist
     */
    public QueryBuilder(File file) throws FileNotFoundException {

        if (file.exists()) {

            queryList = new ArrayList();

            Scanner queryScan = new Scanner(file);

            while (queryScan.hasNextLine()) {

                queryList.add(queryScan.nextLine());

            }

            queryScan.close();

        } else {
            throw new FileNotFoundException("File " + file.getName() + " does not exist.");
        }

    }

    /**
     * Returns the <code>queryList</code> created by this <code>QueryBuilder.</code>
     *
     * @return <code>ArrayList</code> of all query strings
     */
    public ArrayList<String> getQueryList() {

        ArrayList<String> list = new ArrayList();

        for (String query : queryList) {
            list.add(query);
        }

        return list;

    }

}
