package cs321.search;

import java.io.File;

/**
 * <code>GeneBankSearchDatabaseArguments</code> parses commandline
 * arguments for runtime of <code>GeneBankSearchDatabase</code>.
 *
 * @version CS 321: Fall 2021
 * @author elijahsorensen
 */
public class GeneBankSearchDatabaseArguments {

    private int debugLevel;
    private File queryFile;

    public GeneBankSearchDatabaseArguments(String[] args) throws SearchDatabaseArgumentException {

        debugLevel = 0;

        if (args.length < 2) {
            throw new SearchDatabaseArgumentException("Too few arguments.");
        } else if (args.length > 3) {
            throw new SearchDatabaseArgumentException("Too many arguments.");
        }

        File databaseFile = new File(args[0]);

        if (databaseFile.exists()) {

            queryFile = new File(args[1]);

            if (queryFile.exists()) {

                if (args.length == 3) {

                    try {

                        debugLevel = Integer.parseInt(args[2]);

                    } catch (NumberFormatException e) {

                        throw new SearchDatabaseArgumentException("Debug level error.");

                    }

                }

            } else {
                throw new SearchDatabaseArgumentException("Error opening query file.");
            }

        } else {
            throw new SearchDatabaseArgumentException("Error opening database file.");
        }

    }

    /**
     * Returns the <code>debugLevel</code> parsed by this
     * <code>GeneBankSearchDatabaseArguments</code>.
     *
     * @return debug level
     */
    public int getDebugLevel() { return debugLevel; }

    /**
     * Returns the reference to the <code>queryFile</code> parsed
     * by this <code>GeneBankSearchDatabaseArguments</code>.
     *
     * @return query file
     */
    public File getQueryFile() { return queryFile; }

}
