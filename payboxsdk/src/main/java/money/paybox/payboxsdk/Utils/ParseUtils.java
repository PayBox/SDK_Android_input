package money.paybox.payboxsdk.Utils;



import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by arman on 08.11.17.
 */

public class ParseUtils  {



    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static ParseUtils instance;
    public static ParseUtils getInstance(){
        if(instance==null){
            instance = new ParseUtils();
        }
        return instance;
    }
    private ParseUtils(){

    }


    public String getSig(String secretKey, String url, HashMap<String, String> param){
        String sig = url;
        int size = param.entrySet().size();
        for(Map.Entry<String,String> entry : param.entrySet()){
            sig +=";";
            sig +=entry.getValue();
            size--;
            if(size==0){
                sig +=";";
                sig +=secretKey;
            }
        }
        return md5(sig.toString());
    }
    private String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }


    public String getStringFromResponse(JSONObject jsonObject, String key){
        if(jsonObject.has(Constants.RESPONSE)){
            return jsonObject.optJSONObject(Constants.RESPONSE).optString(key);
        }
        return "";
    }
    public JSONObject xmlToJson(String xml) throws JSONException{
        return XML.toJSONObject(xml,true);
    }


    public LinkedHashMap<String, String>  sort(Map<String, String> map) {

        List<Map.Entry<String, String>> list = new LinkedList<>(map.entrySet());
        Collections.sort( list, new Comparator<Map.Entry<String,String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return (o1.getKey()).compareTo( o2.getKey() );
            }
        });

        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
