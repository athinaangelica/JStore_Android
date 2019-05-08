package praktikum.oop.jstore_android_athina;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuatPesananRequest extends StringRequest {

    private Map<String, String> params;
    String ipAddress;

    public BuatPesananRequest(int items, int customerId, InvoiceStatus status, String ipAddress, Response.Listener<String> listener)
    {
        super(Method.POST, setUrl(status, ipAddress), listener, null);

        String customerIdString = "" + customerId;

        params = new HashMap<>();
        params.put("id_customer", customerIdString);

//        for (Integer itemId : items) {
//            params.put("list_item", itemId.toString());
//        }

        params.put("list_item", ("" + items));
    }

    public BuatPesananRequest(int items, int customerId, InvoiceStatus status, String installmentPeriod, String ipAddress, Response.Listener<String> listener)
    {

        super(Method.POST, setUrl(status, ipAddress), listener, null);

        String customerIdString = "" + customerId;

        params = new HashMap<>();
        params.put("id_customer", customerIdString);

//        for (Integer itemId : items) {
//            params.put("list_item", itemId.toString());
//        }

        params.put("list_item", ("" + items));

        params.put("durasi_installment", installmentPeriod);
    }

    private static String setUrl(InvoiceStatus status, String ipAddress){
        String Invoice_URL = "";

        if (status == InvoiceStatus.PAID) {
            Invoice_URL = "http://" + ipAddress + ":8080/createinvoicepaid";
        }
        else if (status == InvoiceStatus.UNPAID){
            Invoice_URL = "http://" + ipAddress + ":8080/createinvoiceunpaid"; //kosan
        }
        else {
            Invoice_URL = "http://" + ipAddress + ":8080/createinvoiceinstallment"; // kosan
        }

        return Invoice_URL;
    }

    @Override
    public Map<String, String> getParams()
    {
        return params;
    }
}
