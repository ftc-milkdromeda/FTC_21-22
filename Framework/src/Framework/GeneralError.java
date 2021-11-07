package Framework;

/**
 * @author Tyler Wang
 * A enum that defines general errors that may be issued. An implementation {@link Error}
 */
public enum GeneralError implements Error{
    /**
     * Issued when a function returns without any errors.
     */
    NO_ERROR;

    /**
     * A function that returns the source of the Error.
     * @return a string that represents the source of the Error.
     */
    @Override
    public String getSource() {
        return this.source;
    }

    /**
     * A function that returns a error message in the form of a string.
     * @return the error message of the specific error.
     */
    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    private String source = "General";
}
