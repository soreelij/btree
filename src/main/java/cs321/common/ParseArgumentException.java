package cs321.common;

/**
 * Class for handling command line arguments when and exception occurs
 * that extends Class Exception
 *
 * @version Fall 2021
 */
public class ParseArgumentException extends Exception
{
    public ParseArgumentException(String message)
    {
        super(message);
    }
}
