package money.paybox.paybox.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import money.paybox.paybox.App;
import money.paybox.paybox.R;
import money.paybox.payboxsdk.Interfaces.PBListener;
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

public abstract class BaseFragment extends Fragment {



    protected int intET(EditText editText){
        return Integer.parseInt(editText.getText().toString());
    }
    protected String strET(EditText editText){
        return editText.getText().toString();
    }
    protected boolean isEmpty(EditText editText){
        if(TextUtils.isEmpty(editText.getText().toString())){
            Toast.makeText(getContext(),getString(R.string.text_empty),Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
