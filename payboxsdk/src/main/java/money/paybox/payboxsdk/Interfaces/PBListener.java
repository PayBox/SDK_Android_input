package money.paybox.payboxsdk.Interfaces;

import java.util.ArrayList;

import money.paybox.payboxsdk.Model.Capture;
import money.paybox.payboxsdk.Model.Card;
import money.paybox.payboxsdk.Model.Error;
import money.paybox.payboxsdk.Model.PStatus;
import money.paybox.payboxsdk.Model.RecurringPaid;
import money.paybox.payboxsdk.Model.Response;

/**
 * Created by arman on 16.11.17.
 */

public interface PBListener {
    void onCardList(ArrayList<Card> cards);
    void onPaymentRevoke(Response response);
    void onPaymentPaid(Response response);
    void onPaymentStatus(PStatus pStatus);
    void onCardAdded(Response response);
    void onCardRemoved(Card card);
    void onCardPayInited(Response response);
    void onCardPaid(Response response);
    void onRecurringPaid(RecurringPaid recurringPaid);
    void onPaymentCaptured(Capture capture);
    void onPaymentCanceled(Response response);
    void onError(Error error);


}
