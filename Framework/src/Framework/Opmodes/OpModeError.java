package Framework.Opmodes;

import Framework.Error;

/**
 * @author Tyler Wang
 * Enum that defines a set of errors for {@link OpMode}. An implementation of {@link Error}
 */
public enum OpModeError implements Error {
    /**
     * Issued when an attempt to access an {@link OpMode} is made when the {@link OpMode} hasn't been initialized.
     */
    OP_MODE_NOT_INIT,

    /**
     * Issued when {@link OpMode} is attempted to be reinitialized.
     */
    OP_MODE_ALREADY_INIT,

    /**
     * Issued when an {@link OpMode} is attempted to be initialized when another {@link OpMode} is active.
     */
    ANOTHER_OP_MODE_INIT
    ;

    /**
     * Returns error in a printable string format.
     * @return A string in the format "Opmode: [error]".
     */
    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    /**
     * Returns the source of the error.
     * @return returns the string "OpMode".
     */
    @Override
    public String getSource() {
        return this.source;
    }

    private String source = "OpMode";
}
