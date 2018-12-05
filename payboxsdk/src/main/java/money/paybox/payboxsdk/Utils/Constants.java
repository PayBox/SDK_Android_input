package money.paybox.payboxsdk.Utils;

import android.util.Log;

/**
 * Created by arman on 07.11.17.
 */

public class Constants {
    public static final String PB_URL = "http://paybox.money/";
    public static final String PB_MAIN = "https://api.paybox.money/";
    public static final String PB_ENTRY_URL = PB_MAIN+"init_payment.php";
    public static final String PB_STATUS_URL = PB_MAIN+"get_status.php";
    public static final String PB_REVOKE_URL = PB_MAIN+"revoke.php";
    public static final String PB_CANCEL_URL = PB_MAIN+"cancel.php";
    public static final String PB_DO_CAPTURE_URL = PB_MAIN+"do_capture.php";
    public static final String PB_RECURRING_URL = PB_MAIN+"make_recurring_payment.php";
    private static final String PB_CARD_URL_1 = PB_MAIN.concat("v1/merchant/");
    private static final String PB_CARD_URL_2 = "/cardstorage/";
    private static final String PB_CARD_URL_3 = "/card/";
    public static final String PB_LISTCARD_URL = "list";
    public static final String PB_CARDINITPAY = "init";
    public static final String PB_CARDPAY = "pay";
    public static final String PB_ADDCARD_URL = "add";
    public static final String PB_REMOVECARD_URL = "remove";
    public static String PB_CARDPAY_MERCHANT(String merchant_id){
        return PB_CARD_URL_1.concat(merchant_id).concat(PB_CARD_URL_3);
    }
    public static String PB_CARD_MERCHANT(String merchant_id){
        return PB_CARD_URL_1.concat(merchant_id).concat(PB_CARD_URL_2);
    }

    public enum CURRENCY {
        KZT, USD, RUB, EUR, KGS
    }

    public static final String APPLOG = "AAA";


    public static final String PB_REDIRECT_URL = "pg_redirect_url";

    public enum PBPAYMENT_SYSTEM {
        KAZPOSTKZT, CYBERPLATKZT, CONTACTKZT, SBERONLINEKZT, ONLINEBANK, CASHBYCODE, KASPIKZT, KAZPOSTYANDEX, SMARTBANKKZT, NURBANKKZT, BANKRBK24KZT, ALFACLICKKZT, FORTEBANKKZT, EPAYWEBKGS, EPAYKGS, HOMEBANKKZT, EPAYKZT, KASSA24, P2PKKB, EPAYWEBKZT
    }
    public enum PBREQUEST_METHOD {
        GET, POST
    }
    public enum PBLANGUAGE {
        ru, en
    }

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String CUSTOMER = "customer=";

    // CARD
    public static final String PB_CARD_CREATED_AT = "created_at";
    public static final String PB_CARD_DELETED_AT = "deleted_at";
    public static final String PB_USER_ID = "pg_user_id";
    public static final String PB_POST_URL = "pg_post_link";
    public static final String PB_BACK_URL = "pg_back_link";
    public static final String PB_CARD_ID = "pg_card_id";
    public static final String PB_CARD_PAN = "pg_card_pan";
    public static final String PB_CARD_HASH = "pg_card_hash";
    public static final String PB_RECURRING_PROFILE_ID = "pg_recurring_profile_id";
    public static final String CARD = "card";


    //RECURING
    public static final String PB_RECURRING_PROFILE = "pg_recurring_profile";
    public static final String PB_CREATE_DATE = "pg_create_date";
    public static final String PB_TRANSACTION_STATUS = "pg_transaction_status";
    public static final String PB_CAN_REJECT = "pg_can_reject";
    public static final String PB_CAPTURED = "pg_captured";
    public static final String PB_ACCEPTED_PAYSYSTEM = "pg_accepted_payment_systems";
    public static final String PB_RECURRING_EXPIRE_DATE = "pg_recurring_profile_expiry_date";

    // RESPONSE
    public static final String RESPONSE = "response";
    public static final String PB_STATUS = "pg_status";
    public static final String PB_ERROR_CODE = "pg_error_code";
    public static final String PB_ERROR_DESCRIPTION = "pg_error_description";
    public static final String STATUS_ERROR = "error";

    //CLEARING
    public static final String PB_CLEARING_AMOUNT = "pg_clearing_amount";

//  REFUND
    public static final String PB_PAYMENT_ID = "pg_payment_id";
    public static final String PB_REFUND_AMOUNT = "pg_refund_amount";

//    INIT_PAYMENT
    public static final String AUTO_CLEARING = "pg_auto_clearing";
    public static final String MERCHANT_ID = "pg_merchant_id"; //NOT NULL
    public static final String ORDER_ID = "pg_order_id";
    public static final String AMOUNT = "pg_amount"; //NOT NULL
    public static final String PB_CURRENCY = "pg_currency";
    public static final String CHECK_URL = "pg_check_url";
    public static final String RESULT_URL = "pg_result_url";
    public static final String REFUND_URL = "pg_refund_url";
    public static final String CAPTURE_URL = "pg_capture_url";
    public static final String REQUEST_METHOD = "pg_request_method";
    public static final String SUCCESS_URL = "pg_success_url";
    public static final String FAILURE_URL = "pg_failure_url";
    public static final String SUCCESS_METHOD = "pg_success_url_method";
    public static final String FAILURE_METHOD = "pg_failure_url_method";
    public static final String STATE_URL = "pg_state_url";
    public static final String STATE_METHOD = "pg_state_url_method";
    public static final String SITE_URL = "pg_site_url";
    public static final String PAYMENT_SYSTEM = "pg_payment_system";
    public static final String LIFETIME = "pg_lifetime";
    public static final String ENCODING = "pg_encoding";
    public static final String DESCRIPTION = "pg_description"; //NOT NULL
    public static final String USER_PHONE = "pg_user_phone";
    public static final String CONTACT_EMAIl = "pg_user_contact_email";
    public static final String USER_MONEY_EMAIL = "pg_user_email";
    public static final String USER_IP = "pg_user_ip";
    public static final String POSTPONE_PAYMENT = "pg_postpone_payment";
    public static final String LANGUAGE = "pg_language";
    public static final String TESTING_MODE = "pg_testing_mode";
    public static final String RECURRING_START = "pg_recurring_start";
    public static final String RECURRING_LIFETIME = "pg_recurring_lifetime";
    public static final String SALT = "pg_salt"; //NOT NULL
    public static final String SIG = "pg_sig"; //NOT NULL

    public static void logMessage(String message){

    }
}
