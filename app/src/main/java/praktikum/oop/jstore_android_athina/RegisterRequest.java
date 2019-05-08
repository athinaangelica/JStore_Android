package praktikum.oop.jstore_android_athina;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest
{
    private Map<String, String> params;

    public RegisterRequest(String name, String username, String email, String password, String ipAddress, Response.Listener<String> listener)
    {
        super(Method.POST, setUrl(ipAddress), listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }

    private static String setUrl(String ipAddress)
    {
        return "http://" + ipAddress + ":8080/newcustomer";
    }
}
