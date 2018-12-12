package money.paybox.paybox;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import money.paybox.payboxsdk.PBHelper;
import money.paybox.payboxsdk.Utils.Constants;

/**
 * Created by arman on 19.11.2017.
 */

public class App extends Application {
    public static App instance;
    public PBHelper.Builder builder;
    SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public void initBuilder(String secret, int merchant, boolean clearing){
        builder = new PBHelper.Builder(getApplicationContext(),secret,merchant).
                setPaymentSystem(Constants.PBPAYMENT_SYSTEM.EPAYWEBKZT).
                enabledAutoClearing(clearing).enabledTestMode(true).
                setFeedBackUrl("http://test.paybox.kz/","http://test.paybox.kz/","http://test.paybox.kz/","http://test.paybox.kz/",Constants.PBREQUEST_METHOD.GET);
    }

    public boolean isLoggedIn(){
        if (preferences.contains(getString(R.string.secretKeyIndex))&&preferences.contains(getString(R.string.merchantIdIndex))) {
            return true;
        }
        return false;
    }
    public String getSecretKey(){
        return preferences.getString(getString(R.string.secretKeyIndex), null);
    }

    public boolean getAutoClearing(){
        return preferences.getBoolean(getString(R.string.autoClearingIndex), false);
    }

    public int getMerchandID(){
        return Integer.parseInt(preferences.getString(getString(R.string.merchantIdIndex), null));
    }

    public boolean saveSettings(String merchantId, String secretKey){
        return preferences.edit().putString(getString(R.string.secretKeyIndex), secretKey).putString(getString(R.string.merchantIdIndex), merchantId).commit();
    }
}
