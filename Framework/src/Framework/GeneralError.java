package Framework;

public enum GeneralError implements Error{
    NO_ERROR;

    @Override
    public String getSource() {
        return this.source;
    }
    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }

    private String source;
}
