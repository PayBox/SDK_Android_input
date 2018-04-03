package money.paybox.payboxsdk.Model;

/**
 * Created by arman on 16.11.17.
 */

public class Error {
    private int errorCode;
    private String errorDesription;

    public Error(int errorCode, String errorDesription) {
        this.errorCode = errorCode;
        this.errorDesription = errorDesription;
    }

    public String getErrorDesription() {
        return errorDesription;
    }
    public int getErrorCode(){
        return errorCode;
    }
}
