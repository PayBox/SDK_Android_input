package money.paybox.paybox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LaunchActivity extends AppCompatActivity {



    @BindView(R.id.authSecretKey)
    EditText authSecretKey;
    @BindView(R.id.authMerchantId)
    EditText authMerchantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this, this);
        if (App.instance.isLoggedIn()){
            showHomeActivity();
        }

    }

    @OnClick(R.id.signIn)
    public void signIn(){
        if (!TextUtils.isEmpty(authMerchantId.getText().toString())&&!TextUtils.isEmpty(authSecretKey.getText().toString())){
           if (App.instance.saveSettings(authMerchantId.getText().toString(), authSecretKey.getText().toString())) {
               showHomeActivity();
           }
        }
    }

    private void showHomeActivity(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
}
