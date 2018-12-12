package money.paybox.payboxsdk.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import money.paybox.payboxsdk.PBHelper;
import money.paybox.payboxsdk.R;
import money.paybox.payboxsdk.Utils.Constants;

public class WebActivity extends AppCompatActivity {


    private PBHelper.OPERATION webCommand;
    WebView pbsdk_web;
    private static String COMMAND_EXTRA = "command_extra";
    private static String URL_EXTRA = "url";
    private String customer;
    public static void startWebActivity(Context context, String redirectUrl, PBHelper.OPERATION webCommand){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(COMMAND_EXTRA, webCommand);
        intent.putExtra(URL_EXTRA, redirectUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        Intent intent = getIntent();
        pbsdk_web = (WebView)findViewById(R.id.pbsdk_web);
        if(intent.hasExtra(URL_EXTRA)) {
            initWeb(intent.getStringExtra(URL_EXTRA));
        }
        if(intent.hasExtra(COMMAND_EXTRA)) {
            webCommand = (PBHelper.OPERATION)intent.getExtras().get(COMMAND_EXTRA);
        }

    }




    private void initWeb(String url){
        pbsdk_web.setWebViewClient(new SdkWebView());
        pbsdk_web.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        pbsdk_web.getSettings().setJavaScriptEnabled(true);
        pbsdk_web.getSettings().setAllowFileAccess(true);
        pbsdk_web.getSettings().setAllowFileAccessFromFileURLs(true);
        pbsdk_web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        pbsdk_web.loadUrl(url);
        if (url.contains(Constants.CUSTOMER)) {
            this.customer = url.split(Constants.CUSTOMER)[1];
        }
    }




    private class SdkWebView extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Constants.logMessage("redirect "+url);
            switch (webCommand){
                case CARDPAY:
                    if(url.contains(Constants.SUCCESS)){
                        PBHelper.getSdk().webSubmited(true, webCommand);
                        finish();
                    } else
                    if (url.contains(Constants.FAILURE)){
                        PBHelper.getSdk().webSubmited(false, webCommand);
                        finish();
                    }
                    pbsdk_web.loadUrl(url);
                    break;
                case CARDADD:
                    if(url.contains(Constants.SUCCESS)){
                        PBHelper.getSdk().webSubmited(true, webCommand);
                        finish();
                    } else
                    if(url.contains(Constants.FAILURE)){
                        PBHelper.getSdk().webSubmited(false, webCommand);
                        finish();
                    }
                    pbsdk_web.loadUrl(url);
                    break;
                case PAYMENT:
                    if(url.contains(customer)){
                        pbsdk_web.loadUrl(url);
                    }
                    if(url.contains(Constants.SUCCESS)){
                        PBHelper.getSdk().webSubmited(true, webCommand);
                        finish();
                    }
                    if(url.contains(Constants.FAILURE)){
                        PBHelper.getSdk().webSubmited(false, webCommand);
                        finish();
                    }
                     break;
            }
            return true;
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
