package Framework.QRCode;

import Framework.Error;

public enum QRCodeError implements Error {
    QR_CODE_NOT_DEFINED,
    QR_CODE_CORRUPTED,
    CLASS_NOT_VALIDATED,
    QR_CODE_LENGTH_MISMATCH;

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getSource() {
        return "QRCode:" + super.toString();
    }

}
