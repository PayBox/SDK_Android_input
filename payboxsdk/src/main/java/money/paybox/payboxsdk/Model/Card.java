package money.paybox.payboxsdk.Model;

import java.util.Date;

import money.paybox.payboxsdk.Utils.ParseUtils;

/**
 * Created by arman on 16.11.17.
 */

public class Card  {
    private String status;
    private String merchantId;
    private String cardId;
    private String recurringProfile;
    private String cardhash;
    private Date date;

    public Card(String status, String merchantId, String cardId, String recurringProfile, String cardhash, String date) {
        this.status = status;
        this.merchantId = merchantId;
        this.cardId = cardId;
        this.recurringProfile = recurringProfile;
        this.cardhash = cardhash;
        try {
            this.date = ParseUtils.simpleDateFormat.parse(date);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCardId() {
        return cardId;
    }

    public String getRecurringProfile() {
        return recurringProfile;
    }

    public String getCardhash() {
        return cardhash;
    }

}
