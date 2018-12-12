package money.paybox.payboxsdk.Utils;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import money.paybox.payboxsdk.PBHelper;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServerHelper extends IntentService {


    public static final String RESPONSE = "response";
    private PBHelper.OPERATION operation;
    private ResultReceiver receiver;
    public ServerHelper() {
        super("ServerHelper");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    private static final String CONFIG_EXTRA = "config";
    private static final String RESPRECIEVER_EXTRA = "receiver";
    private static final String COMMAND_EXTRA = "command";
    private static final String SECRET_EXTRA = "secret";
    private static ParseUtils parser = ParseUtils.getInstance();
    private String secretKey;
    public static void startPBConnection(Context context, PBHelper.OPERATION operation, PBResultReceiver resultReceiver, HashMap<String,String> config, String secretKey) {
        Intent intent = new Intent(context, ServerHelper.class);
        intent.putExtra(CONFIG_EXTRA, config);
        intent.putExtra(RESPRECIEVER_EXTRA,resultReceiver);
        intent.putExtra(COMMAND_EXTRA, operation);
        intent.putExtra(SECRET_EXTRA, secretKey);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent.hasExtra(RESPRECIEVER_EXTRA)&&intent.hasExtra(CONFIG_EXTRA)&&intent.hasExtra(COMMAND_EXTRA)&&intent.hasExtra(SECRET_EXTRA)){
            receiver = intent.getParcelableExtra(RESPRECIEVER_EXTRA);
            operation = (PBHelper.OPERATION)intent.getExtras().get(COMMAND_EXTRA);
            receiver.send(PBResultReceiver.STATUS_START_LOADING, Bundle.EMPTY);
            this.secretKey = intent.getStringExtra(SECRET_EXTRA);
            HashMap<String, String> param = parser.sort((HashMap<String, String>) intent.getExtras().get(CONFIG_EXTRA));
                switch (operation){
                    case PAYMENT:
                        initPostRequest(Constants.PB_ENTRY_URL, param);
                        break;
                    case GETSTATUS:
                        initPostRequest(Constants.PB_STATUS_URL, param);
                        break;
                    case REVOKE:
                        initPostRequest(Constants.PB_REVOKE_URL, param);
                        break;
                    case CANCEL:
                        initPostRequest(Constants.PB_CANCEL_URL, param);
                        break;
                    case RECURRING:
                        initPostRequest(Constants.PB_RECURRING_URL, param);
                        break;
                    case CARDADD:
                        initPostRequest(Constants.PB_CARD_MERCHANT(param.get(Constants.MERCHANT_ID)).concat(Constants.PB_ADDCARD_URL), param);
                        break;
                    case CARDLIST:
                        initPostRequest(Constants.PB_CARD_MERCHANT(param.get(Constants.MERCHANT_ID)).concat(Constants.PB_LISTCARD_URL), param);
                        break;
                    case CARDREMOVE:
                        initPostRequest(Constants.PB_CARD_MERCHANT(param.get(Constants.MERCHANT_ID)).concat(Constants.PB_REMOVECARD_URL), param);
                        break;
                    case CARDPAYINIT:
                        initPostRequest(Constants.PB_CARDPAY_MERCHANT(param.get(Constants.MERCHANT_ID)).concat(Constants.PB_CARDINITPAY), param);
                        break;
                    case CAPTURE:
                        initPostRequest(Constants.PB_DO_CAPTURE_URL, param);
                        break;
                }
        }
    }




    private void initPostRequest(String url, HashMap<String,String> bodys){


        if(PBHelper.isOnline(getApplicationContext())) {
            connectToPB(Constants.PBREQUEST_METHOD.POST, url, bodys,null);
        } else {
            receiver.send(PBResultReceiver.STATUS_ERROR, Bundle.EMPTY);
        }
    }


    private String makeParams(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuffer result = new StringBuffer();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));

        }
        //Constants.logMessage("request "+result.toString());
        return result.toString();
    }


    private void connectToPB(Constants.PBREQUEST_METHOD method, String url, HashMap<String,String> bodys, HashMap<String,String> parameters){
        try {
            String response = "";
            URL urlCon = new URL(url);
            String[] paths = urlCon.getPath().split("/");
            bodys.put(Constants.SIG, parser.getSig(secretKey, paths[paths.length-1], bodys));
//            SSLContext sslContext = SSLContext.getInstance("TLSv1.1");
//            sslContext.init(null,null,null);
//            SSLSocketFactory sslSocketFactory = null;
            //TODO TLS

            HttpsURLConnection.setDefaultSSLSocketFactory(new TLSSocketFactory());
            HttpsURLConnection connection = (HttpsURLConnection)urlCon.openConnection();

            connection.setConnectTimeout(25000);
            connection.setReadTimeout(25000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            OutputStream stream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stream, "UTF-8"));
            writer.write(makeParams(bodys));
            writer.flush();
            writer.close();
            stream.close();
            connection.connect();
            int responseCode=connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;

                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
                br.close();
                is.close();
                responceResolver(response);
            } else {
                receiver.send(PBResultReceiver.STATUS_ERROR, Bundle.EMPTY);
            }
            if(connection!=null){
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            receiver.send(PBResultReceiver.STATUS_ERROR, Bundle.EMPTY);
        }
    }

    private void responceResolver(String response) throws Exception{
        Bundle bundle = new Bundle();
        bundle.putString(RESPONSE, response);
        Constants.logMessage(response);
        switch (operation){
            case PAYMENT:
                receiver.send(PBResultReceiver.STATUS_PAYMENT_LOADED,bundle);
                break;
            case REVOKE:
                receiver.send(PBResultReceiver.STATUS_PAYMENT_REVOKE,bundle);
                break;
            case GETSTATUS:
                receiver.send(PBResultReceiver.STATUS_PAYMENT_GETSTATUS,bundle);
                break;
            case CANCEL:
                receiver.send(PBResultReceiver.STATUS_PAYMENT_CANCEL,bundle);
                break;
            case CARDLIST:
                receiver.send(PBResultReceiver.STATUS_CARD_LIST_LOADED,bundle);
                break;
            case CAPTURE:
                receiver.send(PBResultReceiver.STATUS_DO_CAPTURE_INITED,bundle);
                break;
            case CARDPAYINIT:
                receiver.send(PBResultReceiver.STATUS_CARD_PAY_INITED,bundle);
                break;
            case CARDADD:
                receiver.send(PBResultReceiver.STATUS_CARD_ADDED, bundle);
                break;
            case CARDREMOVE:
                receiver.send(PBResultReceiver.STATUS_CARD_REMOVED, bundle);
                break;
            case RECURRING:
                receiver.send(PBResultReceiver.STATUS_RECURRING_PAYED, bundle);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
