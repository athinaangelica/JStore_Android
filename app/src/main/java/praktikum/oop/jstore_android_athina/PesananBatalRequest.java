package praktikum.oop.jstore_android_athina;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PesananBatalRequest extends StringRequest 
{
    private Map<String, String> params;

    public PesananBatalRequest(int invoice_id,  String ipAddress, Response.Listener<String> listener)
    {
        super(Method.POST, setUrl(ipAddress), listener, null);
        params = new HashMap<>();
        params.put("id_invoice", ""+invoice_id);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }

    private static String setUrl(String ipAddress)
    {
        return "http://" + ipAddress + ":8080/canceltransaction";
    }
}
