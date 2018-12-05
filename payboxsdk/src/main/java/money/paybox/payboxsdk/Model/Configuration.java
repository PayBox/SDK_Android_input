package money.paybox.payboxsdk.Model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;

import money.paybox.payboxsdk.Utils.Constants;

/**
 * Created by arman on 08.11.17.
 */

public class Configuration implements Serializable{
    private String CHECK_URL;
    private String RESULT_URL;
    private String REFUND_URL;
    private String CAPTURE_URL;
    private int MERCHANT_ID;
    private boolean isRecurring = false;
    private int RECURRING_LIFETIME = 1;
    private int PAYMENT_LIFETIME = 300;
    private String ENCODING = "UTF-8";
    private String language = Constants.PBLANGUAGE.ru.name();
    private boolean isTest = false;
    private String USER_PHONE;
    private String USER_EMAIL;
    private String PAYMENT_SYSTEM = Constants.PBPAYMENT_SYSTEM.EPAYWEBKZT.name();
    private String REQUEST_METHOD = Constants.PBREQUEST_METHOD.GET.name();
    private String currency = Constants.CURRENCY.KZT.name();
    private String SECRET_KEY;
    private boolean AUTO_CLEARING = false;
    public String successUrl = Constants.PB_MAIN+Constants.SUCCESS;
    public String failureUrl = Constants.PB_MAIN+Constants.FAILURE;



    public boolean getAUTO_CLEARING(){
        return AUTO_CLEARING;
    }
    public void setAUTO_CLEARING(boolean AUTO_CLEARING){
        this.AUTO_CLEARING = AUTO_CLEARING;
    }

    public String getUserPhone() {
        return USER_PHONE;
    }

    public void setUserPhone(String userPhone) {
        this.USER_PHONE = userPhone;
    }

    public String getUserEmail() {
        return USER_EMAIL;
    }

    public void setUserEmail(String userEmail) {
        this.USER_EMAIL = userEmail;
    }



    public boolean isRecurring() {
        return isRecurring;
    }

    public void setPAYMENT_SYSTEM(Constants.PBPAYMENT_SYSTEM paymentSystem){
        this.PAYMENT_SYSTEM = paymentSystem.name();
    }
    public String getPAYMENT_SYSTEM(){
        return PAYMENT_SYSTEM;
    }
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public int getRECURRING_LIFETIME() {
        return RECURRING_LIFETIME;
    }

    public void setRECURRING_LIFETIME(int RECURRING_LIFETIME) {
        this.RECURRING_LIFETIME = RECURRING_LIFETIME;
    }

    public void setCHECK_URL(String CHECK_URL) {
        this.CHECK_URL = CHECK_URL;
    }

    public void setRESULT_URL(String RESULT_URL) {
        this.RESULT_URL = RESULT_URL;
    }

    public void setREFUND_URL(String REFUND_URL) {
        this.REFUND_URL = REFUND_URL;
    }

    public void setCAPTURE_URL(String CAPTURE_URL) {
        this.CAPTURE_URL = CAPTURE_URL;
    }

    public void setMERCHANT_ID(int MERCHANT_ID) {
        this.MERCHANT_ID = MERCHANT_ID;
    }

    public void setLIFETIME(int LIFETIME) {
        this.PAYMENT_LIFETIME = LIFETIME;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(Constants.CURRENCY currency) {
        this.currency = currency.name();
    }

    public void setENCODING(String ENCODING) {
        this.ENCODING = ENCODING;
    }

    public void setLanguage(Constants.PBLANGUAGE language) {
        this.language = language.name();
    }

    public void setTest(boolean test) {
        isTest = test;
    }


    public void setRequestMethod(Constants.PBREQUEST_METHOD requestMethod) {
        this.REQUEST_METHOD = requestMethod.name();
    }

    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public String getMERCHANT_ID() { return String.valueOf(MERCHANT_ID); }

    public String getCHECK_URL() {
        return CHECK_URL;
    }

    public String getRESULT_URL() {
        return RESULT_URL;
    }

    public String getREFUND_URL() {
        return REFUND_URL;
    }

    public String getCAPTURE_URL() {
        return CAPTURE_URL;
    }

    public int getLIFETIME() {
        return PAYMENT_LIFETIME;
    }

    public String getENCODING() {
        return ENCODING;
    }

    public String getLanguage() {
        return language;
    }

    public int isTest() {
        return isTest?1:0;
    }


    public String getRequestMethod() {
        return REQUEST_METHOD;
    }

    public String getSECRET_KEY() {
        return SECRET_KEY;
    }




    private boolean isEmpty(String text){
        return TextUtils.isEmpty(text);
    }

    public HashMap<String, String> toMiniMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.MERCHANT_ID, getMERCHANT_ID());
        map.put(Constants.ENCODING, getENCODING());
        map.put(Constants.TESTING_MODE, String.valueOf(isTest()));
        if(!isEmpty(getCHECK_URL())){
            map.put(Constants.CHECK_URL,getCHECK_URL());
        }
        if(!isEmpty(getRESULT_URL())){
            map.put(Constants.RESULT_URL,getRESULT_URL());
        }
        if(!isEmpty(getREFUND_URL())){
            map.put(Constants.REFUND_URL,getREFUND_URL());
        }
        if(!isEmpty(getCAPTURE_URL())){
            map.put(Constants.CAPTURE_URL,getCAPTURE_URL());
        }
        if(!isEmpty(getCAPTURE_URL())||!isEmpty(getREFUND_URL())||!isEmpty(getRESULT_URL())||!isEmpty(getCHECK_URL())) {
            map.put(Constants.REQUEST_METHOD, getRequestMethod());
        }
        return map;
    }
    public HashMap<String, String> toHashMap(){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.MERCHANT_ID, getMERCHANT_ID());
        map.put(Constants.LANGUAGE, getLanguage());
        map.put(Constants.LIFETIME, String.valueOf(getLIFETIME()));
        map.put(Constants.ENCODING, getENCODING());
        if(!isEmpty(getCHECK_URL())){
            map.put(Constants.CHECK_URL,getCHECK_URL());
        }
        if(!isEmpty(getRESULT_URL())){
            map.put(Constants.RESULT_URL,getRESULT_URL());
        }
        if(!isEmpty(getREFUND_URL())){
            map.put(Constants.REFUND_URL,getREFUND_URL());
        }
        if(!isEmpty(getCAPTURE_URL())){
            map.put(Constants.CAPTURE_URL,getCAPTURE_URL());
        }
        if(!isEmpty(getPAYMENT_SYSTEM())){
            map.put(Constants.PAYMENT_SYSTEM, getPAYMENT_SYSTEM());
        }
        map.put(Constants.PB_CURRENCY, getCurrency());
        map.put(Constants.TESTING_MODE, String.valueOf(isTest()));
        map.put(Constants.REQUEST_METHOD, getRequestMethod());
        map.put(Constants.RECURRING_LIFETIME, String.valueOf(getRECURRING_LIFETIME()));
        map.put(Constants.RECURRING_START, String.valueOf(isRecurring()?1:0));
        map.put(Constants.SUCCESS_URL, successUrl);
        map.put(Constants.FAILURE_URL, failureUrl);
        map.put(Constants.FAILURE_METHOD, "GET");
        map.put(Constants.SUCCESS_METHOD, "GET");
        map.put(Constants.AUTO_CLEARING, String.valueOf(AUTO_CLEARING ? 1 : 0));
        if(!isEmpty(getUserEmail())){
            map.put(Constants.CONTACT_EMAIl, getUserEmail());
        }
        if(!isEmpty(getUserPhone())){
            map.put(Constants.USER_PHONE, getUserPhone());
        }

        return map;
    }
}
