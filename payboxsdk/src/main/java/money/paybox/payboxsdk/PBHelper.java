package money.paybox.payboxsdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import money.paybox.payboxsdk.Interfaces.PBListener;
import money.paybox.payboxsdk.Model.Capture;
import money.paybox.payboxsdk.Model.Card;
import money.paybox.payboxsdk.Model.Configuration;
import money.paybox.payboxsdk.Model.Error;
import money.paybox.payboxsdk.Model.PStatus;
import money.paybox.payboxsdk.Model.RecurringPaid;
import money.paybox.payboxsdk.Model.Response;
import money.paybox.payboxsdk.UI.WebActivity;
import money.paybox.payboxsdk.Utils.Constants;
import money.paybox.payboxsdk.Utils.PBResultReceiver;
import money.paybox.payboxsdk.Utils.ParseUtils;
import money.paybox.payboxsdk.Utils.ServerHelper;

/**
 * Created by arman on 07.11.17.
 */
public class PBHelper implements PBResultReceiver.Receiver{

    private static Configuration configuration;
    private JSONObject responseJson;
    private static ParseUtils parser = ParseUtils.getInstance();
    public ArrayList<PBListener> pbListeners;
    public enum OPERATION {
        PAYMENT, REVOKE, CANCEL, CAPTURE, RECURRING, GETSTATUS, CARDLIST, CARDADD, CARDREMOVE, CARDPAY, CARDPAYINIT
    }
    private PBResultReceiver resultReceiver;
    private static PBHelper instance;
    private Context context;
    public static PBHelper getSdk(){
        if(instance==null){
            throw new NullPointerException("Please init Builder");
        }
        return instance;
    }

    public void registerPbListener(PBListener pbListener){
        if(!pbListeners.contains(pbListener)){
            pbListeners.add(pbListener);
        }

    }
    public void removePbListener(PBListener pbListener){
        pbListeners.remove(pbListener);
    }
    public void enableRecurring(int lifetime){
        configuration.setRecurring(true);
        configuration.setRECURRING_LIFETIME(lifetime);
    }
    public void disableRecurring(){
        configuration.setRecurring(false);
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    private PBHelper(Context context){
        this.context = context;
        resultReceiver = new PBResultReceiver(new Handler());
        resultReceiver.setReceiver(this);
        pbListeners = new ArrayList<>();
    }

    @Override
    public void onLoading() {
        //connectionListener.onStartConnection();
    }

    @Override
    public void onErrorLoaded() {
        Constants.logMessage("onErrorLoaded");
        onErrorConnection();
    }


    private void onErrorConnection(){
        try {
            Constants.logMessage("onErrorConnection");
            //connectionListener.onErrorResponse(new JSONObject(context.getString(R.string.internet_error_description)));
            for(PBListener pbListener : pbListeners){
                pbListener.onError(new Error(0,context.getString(R.string.error_connection)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentInited(JSONObject jsonObject) {
        showWebActivity(jsonObject, OPERATION.PAYMENT);
    }

    @Override
    public void onPaymentRevoked(JSONObject jsonObject) {
        if(responseIsOk(jsonObject)){
            for(PBListener pbListener : pbListeners){
                pbListener.onPaymentRevoke(getResponse(jsonObject));
            }

            //connectionListener.onSuccessConnection(OPERATION.REVOKE, jsonObject);
        }
    }

    @Override
    public void onPaymentStatusChecked(JSONObject jsonObject) {
        if(responseIsOk(jsonObject)) {
            //connectionListener.onSuccessConnection(OPERATION.GETSTATUS, jsonObject);
            for(PBListener pbListener : pbListeners){
                pbListener.onPaymentStatus(new PStatus(
                        parser.getStringFromResponse(jsonObject,Constants.PB_STATUS),
                        parser.getStringFromResponse(jsonObject,Constants.PB_PAYMENT_ID),
                        parser.getStringFromResponse(jsonObject,Constants.PB_TRANSACTION_STATUS),
                        parser.getStringFromResponse(jsonObject,Constants.PB_CAN_REJECT),
                        parser.getStringFromResponse(jsonObject,Constants.PB_CAPTURED),
                        parser.getStringFromResponse(jsonObject,Constants.PB_ACCEPTED_PAYSYSTEM),
                        parser.getStringFromResponse(jsonObject,Constants.PB_CARD_PAN),
                        parser.getStringFromResponse(jsonObject,Constants.PB_CREATE_DATE)
                ));
            }


        }
    }

    @Override
    public void onPaymentCanceled(JSONObject jsonObject) {
        if(responseIsOk(jsonObject)){
            for(PBListener pbListener : pbListeners){
                pbListener.onPaymentCanceled(getResponse(jsonObject));
            }
            //connectionListener.onSuccessConnection(OPERATION.CANCEL, jsonObject);
        }
    }

    @Override
    public void onCardAddInited(JSONObject jsonObject) {
        showWebActivity(jsonObject, OPERATION.CARDADD);
    }

    @Override
    public void onCardRemoved(JSONObject jsonObject) {
        JSONObject response = jsonObject.optJSONObject(Constants.RESPONSE);
        if(response!=null){
            JSONObject card = response.optJSONObject(Constants.CARD);
            if(card!=null){
                for(PBListener pbListener : pbListeners){
                    pbListener.onCardRemoved(new Card(
                            card.optString(Constants.PB_STATUS),
                            card.optString(Constants.MERCHANT_ID),
                            card.optString(Constants.PB_CARD_ID),
                            card.optString(Constants.PB_RECURRING_PROFILE_ID),
                            card.optString(Constants.PB_CARD_HASH),
                            card.optString(Constants.PB_CARD_DELETED_AT)));
                }

                //connectionListener.onSuccessConnection(OPERATION.CARDREMOVE, jsonObject);
            } else {
                //connectionListener.onErrorResponse(jsonObject);
                for(PBListener pbListener : pbListeners){
                    pbListener.onError(new Error(Integer.parseInt(response.optString(Constants.PB_ERROR_CODE)),response.optString(Constants.PB_ERROR_DESCRIPTION)));
                }

            }
        }
    }

    @Override
    public void onCardPayInited(JSONObject jsonObject) {
        if(responseIsOk(jsonObject)) {
            responseJson = jsonObject;
            //connectionListener.onSuccessConnection(OPERATION.CARDPAYINIT, jsonObject);
            for(PBListener pbListener : pbListeners){
                pbListener.onCardPayInited(getResponse(jsonObject));
            }

        }
    }

    @Override
    public void onRecurringPayed(JSONObject jsonObject) {
        if(responseIsOk(jsonObject)){
            for(PBListener pbListener : pbListeners){
                pbListener.onRecurringPaid(new RecurringPaid(
                        parser.getStringFromResponse(jsonObject,Constants.PB_STATUS),
                        parser.getStringFromResponse(jsonObject,Constants.AMOUNT),
                        parser.getStringFromResponse(jsonObject,Constants.PB_PAYMENT_ID),
                        parser.getStringFromResponse(jsonObject,Constants.PB_RECURRING_PROFILE),
                        parser.getStringFromResponse(jsonObject,Constants.PB_CURRENCY),
                        parser.getStringFromResponse(jsonObject,Constants.PB_RECURRING_EXPIRE_DATE)
                ));
            }

            //connectionListener.onSuccessConnection(OPERATION.RECURRING, jsonObject);
        }

    }

    @Override
    public void onDoCaptureInited(JSONObject jsonObject) {
        if(responseIsOk(jsonObject)){
            for(PBListener pbListener : pbListeners){
                pbListener.onPaymentCaptured(new Capture(
                        parser.getStringFromResponse(jsonObject,Constants.PB_STATUS),
                        parser.getStringFromResponse(jsonObject,Constants.AMOUNT),
                        parser.getStringFromResponse(jsonObject,Constants.PB_CLEARING_AMOUNT)));
            }

            //connectionListener.onSuccessConnection(OPERATION.CAPTURE,jsonObject);
        }
    }

    @Override
    public void onCardListLoaded(JSONObject jsonObject) {
        ArrayList<Card> cards = new ArrayList<>();
        JSONObject response = jsonObject.optJSONObject(Constants.RESPONSE);
        if(response!=null){
            JSONArray card_array = response.optJSONArray(Constants.CARD);
            JSONObject card = response.optJSONObject(Constants.CARD);
            if(card_array!=null){
                for(int o=0; o<card_array.length();o++){
                    cards.add(new Card(
                            card_array.optJSONObject(o).optString(Constants.PB_STATUS),
                            card_array.optJSONObject(o).optString(Constants.MERCHANT_ID),
                            card_array.optJSONObject(o).optString(Constants.PB_CARD_ID),
                            card_array.optJSONObject(o).optString(Constants.PB_RECURRING_PROFILE_ID),
                            card_array.optJSONObject(o).optString(Constants.PB_CARD_HASH),
                            card_array.optJSONObject(o).optString(Constants.PB_CARD_CREATED_AT)));
                }
            }
            if(card!=null){
                cards.add(new Card(
                        card.optString(Constants.PB_STATUS),
                        card.optString(Constants.MERCHANT_ID),
                        card.optString(Constants.PB_CARD_ID),
                        card.optString(Constants.PB_RECURRING_PROFILE_ID),
                        card.optString(Constants.PB_CARD_HASH),
                        card.optString(Constants.PB_CARD_CREATED_AT)));
            }
            for(PBListener pbListener : pbListeners){
                pbListener.onCardList(cards);
            }
            if(card==null&&card_array==null){
                for(PBListener pbListener : pbListeners){
                    pbListener.onError(new Error(1,context.getString(R.string.card_not_found)));
                }
            }
            //connectionListener.onSuccessConnection(OPERATION.CARDLIST,jsonObject);
        } else {

            try {
                //connectionListener.onErrorResponse(new JSONObject(context.getString(R.string.card_not_found_json)));

            } catch (Exception e){
                e.printStackTrace();
            }
            for(PBListener pbListener : pbListeners){
                pbListener.onError(new Error(1,context.getString(R.string.card_not_found)));
            }

        }

    }


    private void showWebActivity(JSONObject jsonObject, OPERATION command){
        if(responseIsOk(jsonObject)){
            responseJson = jsonObject;
            WebActivity.startWebActivity(context,parser.getStringFromResponse(jsonObject,Constants.PB_REDIRECT_URL),command);
        }
    }

    private boolean responseIsOk(JSONObject jsonObject){
        Constants.logMessage("responseIsOk "+jsonObject.toString());
        String status = parser.getStringFromResponse(jsonObject, Constants.PB_STATUS);
        if(status!=null&&!status.equals(Constants.STATUS_ERROR)){
            return true;
        } else {
            //connectionListener.onErrorResponse(jsonObject);
            for(PBListener pbListener : pbListeners){
                pbListener.onError(new Error(Integer.parseInt(parser.getStringFromResponse(jsonObject,Constants.PB_ERROR_CODE)), parser.getStringFromResponse(jsonObject,Constants.PB_ERROR_DESCRIPTION)));
            }

        }
        return false;
    }
    public void webSubmited(boolean isSuccess, OPERATION webCommand) {
        if(isSuccess){
            switch (webCommand){
                case CARDADD:
                    if(responseJson!=null){
                        //connectionListener.onSuccessConnection(webCommand, responseJson);
                        for(PBListener pbListener : pbListeners){
                            pbListener.onCardAdded(getResponse(responseJson));
                        }

                    }
                    break;
                case CARDPAY:
                    if(responseJson!=null){
                        //connectionListener.onSuccessConnection(webCommand, responseJson);
                        for(PBListener pbListener : pbListeners){
                            pbListener.onCardPaid(getResponse(responseJson));
                        }

                    }

                    break;
                case PAYMENT:
                    if(responseJson!=null){
                        //connectionListener.onSuccessConnection(webCommand, responseJson);
                        for(PBListener pbListener : pbListeners){
                            pbListener.onPaymentPaid(getResponse(responseJson));
                        }

                    }
                    break;
            }
        } else {
            try {
                //connectionListener.onErrorResponse(new JSONObject(context.getString(R.string.operation_failure_json)));
            } catch (Exception e){
                e.printStackTrace();
            }
            for(PBListener pbListener : pbListeners){
                pbListener.onError(new Error(3,context.getString(R.string.operation_failure)));
            }
        }

    }
    private HashMap<String, String> defParams(){
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.MERCHANT_ID, configuration.getMERCHANT_ID());
        params.put(Constants.TESTING_MODE, String.valueOf(configuration.isTest()));
        return params;
    }
    private Response getResponse(JSONObject jsonObject){
        JSONObject object = jsonObject.optJSONObject(Constants.RESPONSE);
        Response response = null;
        if(object!=null){
            response = new Response(object.optString(Constants.PB_STATUS),object.optString(Constants.PB_PAYMENT_ID),object.optString(Constants.PB_REDIRECT_URL));
        }
        return response;
    }
    public void payWithCard(int paymentId) {
        String url = new String();
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_PAYMENT_ID, String.valueOf(paymentId));
        param.put(Constants.PAYMENT_SYSTEM,configuration.getPAYMENT_SYSTEM());
        param.put(Constants.SALT, Long.toHexString(Double.doubleToLongBits(Math.random())));
        param.put(Constants.SIG, parser.getSig(configuration.getSECRET_KEY(),Constants.PB_CARDPAY,parser.sort(param)));
        for (Map.Entry<String,String> entry : param.entrySet()){
            url += entry.getKey()+"="+entry.getValue()+"&";
        }
        Constants.logMessage("url "+Constants.PB_CARDPAY_MERCHANT(configuration.getMERCHANT_ID()).concat(Constants.PB_CARDPAY)+"?"+url);
        WebActivity.startWebActivity(context,Constants.PB_CARDPAY_MERCHANT(configuration.getMERCHANT_ID()).concat(Constants.PB_CARDPAY)+"?"+url, OPERATION.CARDPAY);
    }
    public void initCardPayment(float amount, @NonNull String userId, int cardId, @Nullable String orderId, @NonNull String description, HashMap<String, String> extraParams){//TODO userId
        HashMap<String, String> param = defParams();
        param.put(Constants.AMOUNT, String.valueOf(amount));
        param.put(Constants.SUCCESS_URL, configuration.successUrl);
        param.put(Constants.FAILURE_URL, configuration.failureUrl);
        if(configuration.getRESULT_URL()!=null) {
            param.put(Constants.RESULT_URL, configuration.getRESULT_URL());
        }
        if(extraParams!=null){
            param.putAll(extraParams);
        }
        param.put(Constants.PB_USER_ID, userId);
        param.put(Constants.PB_CARD_ID, String.valueOf(cardId));
        param.put(Constants.DESCRIPTION, description);
        if(!TextUtils.isEmpty(orderId)){
            param.put(Constants.ORDER_ID, orderId);
        }
        init(param, OPERATION.CARDPAYINIT);
    }
    public void removeCard(@NonNull String userId, int cardId){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_USER_ID, userId);
        param.put(Constants.PB_CARD_ID, String.valueOf(cardId));
        init(param, OPERATION.CARDREMOVE);
    }
    public void getCards(@NonNull String userId){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_USER_ID, userId);
        init(param, OPERATION.CARDLIST);
    }
    public void addCard(@NonNull String userId, @Nullable String postUrl){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_USER_ID, userId);
        if(!TextUtils.isEmpty(postUrl)){
            param.put(Constants.PB_POST_URL, postUrl);
        }
        param.put(Constants.PB_BACK_URL, configuration.successUrl);
        init(param, OPERATION.CARDADD);
    }
    public void makeRecurringPayment(float amount, @Nullable String orderId, @NonNull String recuringProfile, @NonNull String description, @Nullable HashMap<String, String> extraParams){
        HashMap<String, String> param = configuration.toMiniMap();
        param.put(Constants.AMOUNT, String.valueOf(amount));
        param.put(Constants.PB_RECURRING_PROFILE, recuringProfile);
        if (!TextUtils.isEmpty(orderId)){
            param.put(Constants.ORDER_ID, orderId);
        }
        if(extraParams!=null){
            param.putAll(extraParams);
        }
        param.put(Constants.DESCRIPTION, description);
        init(param, OPERATION.RECURRING);
    }
    public void initCancelPayment(int paymentId){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_PAYMENT_ID, String.valueOf(paymentId));
        init(param, OPERATION.CANCEL);
    }
    public void initRevokePayment(int paymentId, float amount){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_PAYMENT_ID, String.valueOf(paymentId));
        param.put(Constants.PB_REFUND_AMOUNT, String.valueOf(amount));
        init(param, OPERATION.REVOKE);
    }
    public void initPaymentDoCapture(int paymentId){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_PAYMENT_ID, String.valueOf(paymentId));
        init(param, OPERATION.CAPTURE);
    }
    public void getPaymentStatus(int paymentId){
        HashMap<String, String> param = defParams();
        param.put(Constants.PB_PAYMENT_ID, String.valueOf(paymentId));
        init(param, OPERATION.GETSTATUS);
    }


    public void initNewPayment(@Nullable String orderId, @Nullable String userId, @NonNull float amount, @NonNull String description, @Nullable HashMap<String, String> extraParams){
        HashMap<String,String> param = configuration.toHashMap();
        if(orderId!=null){
            param.put(Constants.ORDER_ID,orderId);
        }
        if(extraParams!=null){
            param.putAll(extraParams);
        }
        if(userId!=null){
            param.put(Constants.PB_USER_ID, userId);
        }

        param.put(Constants.AMOUNT,String.valueOf(amount));
        param.put(Constants.DESCRIPTION,description);

        init(param, OPERATION.PAYMENT);

    }

    private void init(HashMap<String,String> param, OPERATION operation){
        param.put(Constants.SALT, Long.toHexString(Double.doubleToLongBits(Math.random())));
        ServerHelper.startPBConnection(context, operation, resultReceiver, param, configuration.getSECRET_KEY());
    }



    public static final class Builder {
        Context context;
        public Builder(Context context, String secretKey, int merchantId){
            this.context = context;
            configuration = new Configuration();
            configuration.setSECRET_KEY(secretKey);
            configuration.setMERCHANT_ID(merchantId);
        }

        public Builder setPaymentLifeTime(int lifetime){
            if(lifetime>300) {
                configuration.setLIFETIME(lifetime);
            }
            return this;
        }
        // public Builder setConnectionListener(PBConnectionListener listener){
        //    connectionListener = listener;
        //    return this;
        // }
        public Builder enabledAutoClearing(boolean enabled){
            configuration.setAUTO_CLEARING(enabled);
            return this;
        }
        public Builder setLanguage(Constants.PBLANGUAGE language){
            configuration.setLanguage(language);
            return this;
        }
        public Builder setPaymentCurrency(Constants.CURRENCY paymentCurrency){
            configuration.setCurrency(paymentCurrency);
            return this;
        }
        public Builder setPaymentSystem(Constants.PBPAYMENT_SYSTEM paymentSystem){
            configuration.setPAYMENT_SYSTEM(paymentSystem);
            return this;
        }
        public Builder enabledTestMode(boolean enabled){
            configuration.setTest(enabled);
            return this;
        }
        public Builder setUserInfo(String email, String phone){
            configuration.setUserPhone(phone);
            configuration.setUserEmail(email);
            return this;
        }


        public Builder setFeedBackUrl(String checkUrl, String resultUrl, String refundUrl, String captureUrl, Constants.PBREQUEST_METHOD requestMethod){
            configuration.setCHECK_URL(checkUrl);
            configuration.setRESULT_URL(resultUrl);
            configuration.setREFUND_URL(refundUrl);
            configuration.setCAPTURE_URL(captureUrl);
            configuration.setRequestMethod(requestMethod);
            return this;
        }



        public PBHelper build(){
            if(configuration!=null){
                if(instance==null){
                    instance = new PBHelper(context);
                }
            } else {
                throw new NullPointerException("Configuration is NULL");
            }
            return instance;
        }

    }

}