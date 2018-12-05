package money.paybox.payboxsdk.Model;

/**
 * Created by arman on 17.11.17.
 */

public class Capture extends Response{
    private float amount;
    private float clearingAmount;

    public Capture(String status, String amount, String clearingAmount) {
        super(status, null, null);
        this.amount = Float.parseFloat(amount);
        this.clearingAmount = Float.parseFloat(clearingAmount);
    }

    public float getAmount() {
        return amount;
    }

    public float getClearingAmount() {
        return clearingAmount;
    }
}
