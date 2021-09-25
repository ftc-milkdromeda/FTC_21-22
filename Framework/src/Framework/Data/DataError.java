package Framework.Data;
import Framework.Error;
public enum DataError implements Error {
    FILE_ALREADY_EXISTS_ERROR,
    FILE_WRITING_ERROR,
    CONSOLE_IS_NULL_ERROR,
    ;

    private String source = "FRAMEWORK_DATA";
    @Override
    public String toString() {
        return this.source + ": " + super.toString();
    }
    @Override
    public String getSource()
    {
        return this.source;
    }


}
