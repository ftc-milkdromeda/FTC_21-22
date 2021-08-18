package Framework.Opmodes;

import Framework.Error;

public enum OpModeError implements Error {
    OP_MODE_NOT_INIT,
    NO_OP_MODE_INIT,

    OP_MODE_ALREADY_INIT,
    ANOTHER_OP_MODE_INIT
    ;

    @Override
    public String toString() {
        return super.toString() + ": " + this.source;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    private String source = "OpMode";
}
