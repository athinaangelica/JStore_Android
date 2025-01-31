package praktikum.oop.jstore_android_athina;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MenuRequest extends StringRequest
{
    private Map<String, String> params;

    public MenuRequest(String ipAddress, Response.Listener<String> listener)
    {
        super(Request.Method.GET, setUrl(ipAddress), listener, null);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }

    private static String setUrl(String ipAddress)
    {
        return "http://" + ipAddress + ":8080/items";
    }
}
