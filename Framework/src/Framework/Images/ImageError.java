package Framework.Images;
import Framework.Error;

public enum ImageError implements Error {
    IMAGE_INCOMPATIBLE_TYPE,
    IMAGE_NOT_SET,
    SCALE_FACTOR_NEGATIVE_ERROR;

    private String source = "FRAMEWORK_IMAGES";

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