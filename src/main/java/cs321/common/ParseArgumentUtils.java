package cs321.common;

/**
 * <code>ParseArgumentUtils</code>:
 * Utility class for verifying the validity of values passed
 * to driver class as command line arguments.
 *
 * @author elijahsorensen
 * @version CS 321: Fall 2021
 */
public class ParseArgumentUtils {

    /**
     * Verifies if a given <code>argument</code> is within the specified
     * range.
     *
     * @param argument the value to verify
     * @param lowRangeInclusive the lower bound for the value
     * @param highRangeInclusive the upper bound for the value
     * @throws ParseArgumentException if <code>argument</code>
     *                                is not within the range
     */
    public static void verifyRanges(int argument, int lowRangeInclusive, int highRangeInclusive) throws ParseArgumentException {

        if (lowRangeInclusive <= argument) {

            if (!(argument <= highRangeInclusive)) {
                throw new ParseArgumentException("High range value is not greater than or equal to argument.");
            }

        } else {
            throw new ParseArgumentException("Low range value is not greater than or equal to argument.");
        }

    }

    /**
     * Attempts to parse an <code>Integer</code> value from the given
     * <code>String</code> <code>argument</code>.
     *
     * @param argument string to parse from
     * @return the successfully parsed value
     * @throws ParseArgumentException if no <code>Integer</code> was successfully
     *                                parsed from <code>argument</code>
     */
    public static int convertStringToInt(String argument) throws ParseArgumentException {

        try {
            return Integer.parseInt(argument);
        } catch (Exception e) {
            throw new ParseArgumentException("Argument is not a valid integer.");
        }

    }

}
