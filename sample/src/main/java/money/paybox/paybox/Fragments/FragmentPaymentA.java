package money.paybox.paybox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import money.paybox.paybox.App;
import money.paybox.payboxsdk.Model.Capture;
import money.paybox.payboxsdk.Model.Card;
import money.paybox.payboxsdk.Model.Error;
import money.paybox.payboxsdk.Model.PStatus;
import money.paybox.payboxsdk.Model.RecurringPaid;
import money.paybox.payboxsdk.Model.Response;
import money.paybox.payboxsdk.PBHelper;
import money.paybox.paybox.R;

/**
 * Created by arman on 19.11.2017.
 */

public class FragmentPaymentA extends BaseFragment {
    public FragmentPaymentA(){

    }
    @BindView(R.id.amount)
    EditText payAmount;
    @BindView(R.id.comment)
    EditText payComment;
    @BindView(R.id.payUserId)
    EditText payUserId;
    @BindView(R.id.paymentCancel)
    EditText paymentIdCancel;
    @BindView(R.id.paymentRevoke)
    EditText paymentIdRevoke;
    @BindView(R.id.amountRevoke)
    EditText amountRevoke;
    @BindView(R.id.payInitResult)
    public TextView payInitResult;
    @BindView(R.id.payCancelResult)
    public TextView payCancelResult;
    @BindView(R.id.payRevokeResult)
    public TextView payRevokeResult;

    @BindView(R.id.recurring)
    CheckBox checkIsRecurring;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        ButterKnife.bind(this,view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.create)
    public void initPay(){
        if(!isEmpty(payAmount)&&
                !isEmpty(payComment)&&
                !isEmpty(payUserId)){
            if(checkIsRecurring.isChecked()){
                PBHelper.getSdk().enableRecurring(2);
            } else {
                PBHelper.getSdk().disableRecurring();
            }
            PBHelper.getSdk().initNewPayment(null,strET(payUserId),intET(payAmount),strET(payComment));
        }
    }
    @OnClick(R.id.cancel)
    public void initCancel(){
        if(!isEmpty(paymentIdCancel)){
            PBHelper.getSdk().initCancelPayment(intET(paymentIdCancel));
        }
    }

    @OnClick(R.id.revoke)
    public void initRevoke(){
        if(!isEmpty(paymentIdRevoke)&&!isEmpty(amountRevoke)){
            PBHelper.getSdk().initRevokePayment(intET(paymentIdRevoke),intET(amountRevoke));
        }
    }
}
