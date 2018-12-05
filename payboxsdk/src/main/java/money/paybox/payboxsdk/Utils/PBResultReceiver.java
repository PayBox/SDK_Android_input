package money.paybox.payboxsdk.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by arman on 07.11.17.
 */

public class PBResultReceiver extends ResultReceiver {

    public static final int STATUS_PAYMENT_LOADED = 101;
    public static final int STATUS_ERROR = 110;
    public static final int STATUS_PAYMENT_REVOKE = 102;
    public static final int STATUS_PAYMENT_GETSTATUS = 103;
    public static final int STATUS_PAYMENT_CANCEL = 104;
    public static final int STATUS_CARD_ADDED = 106;
    public static final int STATUS_CARD_REMOVED = 107;
    public static final int STATUS_CARD_PAY_INITED = 108;
    public static final int STATUS_CARD_LIST_LOADED = 109;
    public static final int STATUS_DO_CAPTURE_INITED = 114;
    public static final int STATUS_RECURRING_PAYED = 111;


    public static final int STATUS_START_LOADING = 100;

    public interface Receiver{
        void onLoading();
        void onErrorLoaded();
        void onPaymentInited(JSONObject jsonObject);
        void onPaymentStatusChecked(JSONObject jsonObject);
        void onPaymentRevoked(JSONObject jsonObject);
        void onPaymentCanceled(JSONObject jsonObject);
        void onCardAddInited(JSONObject jsonObject);
        void onCardRemoved(JSONObject jsonObject);
        void onCardPayInited(JSONObject jsonObject);
        void onRecurringPayed(JSONObject jsonObject);
        void onDoCaptureInited(JSONObject jsonObject);
        void onCardListLoaded(JSONObject jsonObject);
    }
    private Receiver receiver;
    public PBResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if(receiver!=null){
            try {
                JSONObject response = null;
                if(resultData.containsKey(ServerHelper.RESPONSE)){
                    response = ParseUtils.getInstance().xmlToJson(resultData.getString(ServerHelper.RESPONSE));
                }
                Constants.logMessage("RESULT CODE "+resultCode);
                switch (resultCode){
                    case STATUS_PAYMENT_LOADED:
                        receiver.onPaymentInited(response);
                        break;
                    case STATUS_PAYMENT_REVOKE:
                        receiver.onPaymentRevoked(response);
                        break;
                    case STATUS_ERROR:
                        receiver.onErrorLoaded();
                        break;
                    case STATUS_START_LOADING:
                        receiver.onLoading();
                        break;
                    case STATUS_PAYMENT_GETSTATUS:
                        receiver.onPaymentStatusChecked(response);
                        break;
                    case STATUS_PAYMENT_CANCEL:
                        receiver.onPaymentCanceled(response);
                        break;
                    case STATUS_CARD_LIST_LOADED:
                        receiver.onCardListLoaded(response);
                        break;
                    case STATUS_CARD_PAY_INITED:
                        receiver.onCardPayInited(response);
                        break;
                    case STATUS_DO_CAPTURE_INITED:
                        receiver.onDoCaptureInited(response);
                        break;
                    case STATUS_CARD_ADDED:
                        receiver.onCardAddInited(response);
                        break;
                    case STATUS_RECURRING_PAYED:
                        receiver.onRecurringPayed(response);
                        break;
                    case STATUS_CARD_REMOVED:
                        receiver.onCardRemoved(response);
                        break;
                }
            } catch (Exception e){
                e.printStackTrace();
                receiver.onErrorLoaded();
            }


        }
    }
}
