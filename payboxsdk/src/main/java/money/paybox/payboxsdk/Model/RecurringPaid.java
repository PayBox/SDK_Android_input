package money.paybox.payboxsdk.Model;

import java.util.Date;

import money.paybox.payboxsdk.Utils.ParseUtils;

/**
 * Created by arman on 17.11.17.
 */

public class RecurringPaid extends Response {
    private String recurringProfile;
    private Date recurringExpireDate;
    private String currency;
    private int amount;

    public RecurringPaid(String status, String amount, String paymentId, String recurringProfile, String currency, String recurringExpireDate) {
        super(status, paymentId, null);
        this.recurringProfile = recurringProfile;
        this.amount = Integer.parseInt(amount);
        this.currency = currency;
        try {
            this.recurringExpireDate = ParseUtils.simpleDateFormat.parse(recurringExpireDate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getRecurringProfile() {
        return recurringProfile;
    }

    public Date getExpireDate() {
        return recurringExpireDate;
    }

    public String getCurrency() {
        return currency;
    }
    public int getAmount(){
        return amount;
    }
}
