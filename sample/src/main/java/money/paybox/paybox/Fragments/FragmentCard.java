package money.paybox.paybox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.OnClick;
import money.paybox.payboxsdk.Model.Capture;
import money.paybox.payboxsdk.Model.Card;
import money.paybox.payboxsdk.Model.Error;
import money.paybox.payboxsdk.Model.PStatus;
import money.paybox.payboxsdk.Model.RecurringPaid;
import money.paybox.payboxsdk.Model.Response;
import money.paybox.payboxsdk.PBHelper;
import money.paybox.paybox.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by arman on 19.11.2017.
 */

public class FragmentCard extends BaseFragment {
    @BindView(R.id.userId)
    EditText cardAddUserId;
    @BindView(R.id.showUserId)
    EditText showCardUserId;
    @BindView(R.id.remUserId)
    EditText remCardUserId;
    @BindView(R.id.remCardId)
    EditText remCardId;
    @BindView(R.id.cardAddResult)
    public TextView cardAddResult;
    @BindView(R.id.cardPayAmount)
    EditText cardPayAmount;
    @BindView(R.id.cardPayId)
    EditText cardPayId;
    @BindView(R.id.cardUserId)
    EditText cardUserId;
    @BindView(R.id.cardPayDescription)
    EditText cardPayDescription;
    @BindView(R.id.cardOrderId)
    EditText cardOrderId;
    @BindView(R.id.showCardResult)
    public TextView showCardResult;
    @BindView(R.id.remCardResult)
    public TextView remCardResult;
    @BindView(R.id.cardPayResult)
    public TextView cardPayResult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @OnClick(R.id.addCard)
    public void initAdd(){
        if(!isEmpty(cardAddUserId)){
            PBHelper.getSdk().addCard(strET(cardAddUserId),"postUrl");
        }
    }
    @OnClick(R.id.remCard)
    public void initRemove(){
        if(!isEmpty(remCardUserId)&&!isEmpty(remCardId)){
            PBHelper.getSdk().removeCard(strET(remCardUserId),intET(remCardId));
        }
    }

    @OnClick(R.id.showCard)
    public void showCard(){
        if(!isEmpty(showCardUserId)){
            PBHelper.getSdk().getCards(strET(showCardUserId));
        }
    }
    @OnClick(R.id.cardPayInit)
    public void initCardPay(){
        if(!isEmpty(cardPayAmount)&&!isEmpty(cardPayId)&&!isEmpty(cardUserId)&&!isEmpty(cardPayDescription)&&!isEmpty(cardOrderId)){
            PBHelper.getSdk().initCardPayment(floatET(cardPayAmount),strET(cardUserId),intET(cardPayId),strET(cardOrderId),strET(cardPayDescription), null);
        }
    }


}

