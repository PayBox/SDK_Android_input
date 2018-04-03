package money.paybox.payboxsdk.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import money.paybox.payboxsdk.Utils.ParseUtils;

/**
 * Created by arman on 16.11.17.
 */

public class PStatus extends Response {
    private String transactionStatus;
    private boolean canReject;
    private boolean isCaptured;
    private String paymentSystem;
    private Date createDate;
    private String cardPan;




    public PStatus(String status, String paymentId, String transactionStatus, String canReject, String isCaptured, String paymentSystem, String cardPan, String createDate) {
        super(status, paymentId, null);

        this.transactionStatus = transactionStatus;
        if(Integer.parseInt(canReject)==1){
            this.canReject = true;
        } else {
            this.canReject = false;
        }
        if(Integer.parseInt(isCaptured)==1){
            this.isCaptured = true;
        } else {
            this.isCaptured = false;
        }
        try {
            this.createDate = ParseUtils.simpleDateFormat.parse(createDate);

        } catch (Exception e){
            e.printStackTrace();
        }
        this.paymentSystem = paymentSystem;
        this.cardPan = cardPan;
    }
    public String toString(){
        return transactionStatus+" "+canReject+" "+isCaptured+" "+paymentSystem+" "+createDate.toString();
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public boolean isCanReject() {
        return canReject;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public String getCardPan() {
        return cardPan;
    }
    public Date getCreateDate() {
        return createDate;
    }
}
