package money.paybox.paybox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.OnClick;
import money.paybox.paybox.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import money.paybox.payboxsdk.Model.Capture;
import money.paybox.payboxsdk.Model.Card;
import money.paybox.payboxsdk.Model.Error;
import money.paybox.payboxsdk.Model.PStatus;
import money.paybox.payboxsdk.Model.RecurringPaid;
import money.paybox.payboxsdk.Model.Response;
import money.paybox.payboxsdk.PBHelper;

/**
 * Created by arman on 19.11.2017.
 */

public class FragmentPaymentB extends BaseFragment {


    @BindView(R.id.recurAmount)
    EditText recurAmount;
    @BindView(R.id.recurProfile)
    EditText recurProfile;
    @BindView(R.id.recurComment)
    EditText recurComment;
    @BindView(R.id.recurPayResult)
    public TextView recurPayResult;
    @BindView(R.id.statusPaymentId)
    EditText statusPayId;
    @BindView(R.id.statusPaymentResult)
    public TextView statusPayResult;
    @BindView(R.id.capturePayId)
    EditText capturePayId;
    @BindView(R.id.captureResult)
    public TextView captureResult;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_b, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.recurMake)
    public void initRecurring(){
        if(!isEmpty(recurAmount)&&!isEmpty(recurProfile)&&!isEmpty(recurComment)){
            PBHelper.getSdk().makeRecurringPayment(intET(recurAmount),null,strET(recurProfile),strET(recurComment));
        }
    }
    @OnClick(R.id.showStatusPayment)
    public void showStatusPayment(){
        if(!isEmpty(statusPayId)){
            PBHelper.getSdk().getPaymentStatus(intET(statusPayId));
        }
    }
    @OnClick(R.id.capture)
    public void cliring(){
        if(!isEmpty(capturePayId)){
            PBHelper.getSdk().initPaymentDoCapture(intET(capturePayId));
        }
    }
}
