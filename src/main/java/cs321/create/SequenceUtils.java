package cs321.create;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extract DNA Sequences and Generate DNA Subsequences
 */
public class SequenceUtils {
    private static final String MARKER_START_DNA_SEQUENCE = "ORIGIN";


    /** Obtains the DNASequences from the GBKGenomeFile and parses through
     * the files to find the sequences
     * writes the degree to the start of it, allocates a root node and writes that to the raf.
     * @param gbkGenomeFileName - String of the filename
     * @throws Exception
     */
    public static List<String> getDNASequencesFromGBKGenomeFile(String gbkGenomeFileName) throws Exception {

        ArrayList<String> sequences = new ArrayList();

        /* Accept the .gbk file as input */
        FileReader fileReader = new FileReader(gbkGenomeFileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        boolean sequenceBegin = false;
        boolean endOfFile = false;

        String nextSequence = "";

        while (!endOfFile) {

            /* Read line by line until the ORIGIN keyword is reached */
            if (!sequenceBegin) {

                String nextLine = bufferedReader.readLine();

                if (nextLine == null) {

                    sequences.add(nextSequence);
                    endOfFile = true;

                } else if (nextLine.contains("ORIGIN")) {

                    sequenceBegin = true;

                }

            } else {

                /* Read next sequence */

                int nextChar = bufferedReader.read();

                // Check if char is an integer
                if (!(nextChar > 47 && nextChar < 58) && (char) nextChar != ' ') {

                    // Check if sequence has ended
                    if (nextChar == '/') {

                        sequenceBegin = false;

                    } else if ((char) nextChar == 'n') {

                        if (!nextSequence.equals("")) {
                            sequences.add(nextSequence);
                            nextSequence = "";
                        }

                    } else if (nextChar == -1) {

                        endOfFile = true;

                    } else if ((char) nextChar != '\n') {

                        nextSequence += (char) nextChar;

                    }

                }


            }

        }

        return sequences;

    }

}
