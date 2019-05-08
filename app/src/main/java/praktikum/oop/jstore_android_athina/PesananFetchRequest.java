package praktikum.oop.jstore_android_athina;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PesananFetchRequest extends StringRequest
{
    private Map<String, String> params;

    public PesananFetchRequest(int user_id,  String ipAddress, Response.Listener<String> listener)
    {
        super(Method.POST, setUrl(user_id, ipAddress), listener, null);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }

    private static String setUrl(int user_id, String ipAddress)
    {
        return "http://" + ipAddress + ":8080/invoicecustomer/" + user_id;
    }
}
