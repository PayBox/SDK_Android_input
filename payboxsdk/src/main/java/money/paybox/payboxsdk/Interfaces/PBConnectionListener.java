package money.paybox.payboxsdk.Interfaces;

import org.json.JSONObject;

import money.paybox.payboxsdk.PBHelper;

/**
 * Created by arman on 07.11.17.
 */

public interface PBConnectionListener {
    void onStartConnection();
    void onErrorResponse(JSONObject object);
    void onSuccessConnection(PBHelper.OPERATION operation, JSONObject object);
}
