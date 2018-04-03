package money.paybox.payboxsdk.Model;

/**
 * Created by arman on 16.11.17.
 */

public class Response {

    private String status;
    private String paymentId;
    private String redirectUrl;


    public Response(String status){
        this.status = status;
    }
    public Response(String status, String paymentId, String redirectUrl) {
        this.status = status;
        this.paymentId = paymentId;
        this.redirectUrl = redirectUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
