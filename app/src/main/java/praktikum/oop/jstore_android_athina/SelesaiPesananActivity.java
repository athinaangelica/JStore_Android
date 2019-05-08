package praktikum.oop.jstore_android_athina;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelesaiPesananActivity extends AppCompatActivity {

    private int currentUserId;
    private int invoiceId;
    private String customerName;
    private String itemName;
    private String orderDate;
    private double totalPrice;
    private String invoiceStatus;
    private String invoiceType;
    private String dueDate;
    private int installmentPeriod;

    // declare common text views
    TextView invoiceIdView;
    TextView customerNameView;
    TextView itemNameView;
    TextView orderDateView;
    TextView totalPriceView;
    TextView invoiceStatusView;
    TextView invoiceTypeView;

    // declare disappearing text views
    TextView dueDateTitle;
    TextView installmentPeriodTitle;
    TextView dueDateView;
    TextView installmentPeriodView;

    // declare buttons
    Button cancel;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selesai_pesanan);

        Intent intent = getIntent();
        currentUserId = intent.getIntExtra("user_id",0);

        // initialize common text views
        invoiceIdView = findViewById(R.id.invoice_id);
        customerNameView = findViewById(R.id.customer_name);
        itemNameView = findViewById(R.id.item_name);
        orderDateView = findViewById(R.id.order_date);
        totalPriceView = findViewById(R.id.total_price);
        invoiceStatusView = findViewById(R.id.invoice_status);
        invoiceTypeView = findViewById(R.id.invoice_type);

        // initialize disappearing text views
        dueDateTitle = findViewById(R.id.staticDueDate);
        installmentPeriodTitle = findViewById(R.id.staticInstallmentPeriod);
        dueDateView = findViewById(R.id.due_date);
        installmentPeriodView = findViewById(R.id.installment_period);

        // initialize buttons
        cancel = findViewById(R.id.cancel);
        finish = findViewById(R.id.finish);

        // start with invisible everything
        invoiceIdView.setVisibility(View.GONE);
        customerNameView.setVisibility(View.GONE);
        itemNameView.setVisibility(View.GONE);
        orderDateView.setVisibility(View.GONE);
        totalPriceView.setVisibility(View.GONE);
        invoiceStatusView.setVisibility(View.GONE);
        invoiceTypeView.setVisibility(View.GONE);
        dueDateTitle.setVisibility(View.GONE);
        installmentPeriodTitle.setVisibility(View.GONE);
        dueDateView.setVisibility(View.GONE);
        installmentPeriodView.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        finish.setVisibility(View.GONE);

        // fetch the invoice
        fetchPesanan();

        // button onClickListeners
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTransaction();

                Intent intent = new Intent(SelesaiPesananActivity.this, MainActivity.class);
                intent.putExtra("user_id", currentUserId);
                startActivity(intent);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTransaction();

                Intent intent = new Intent(SelesaiPesananActivity.this, MainActivity.class);
                intent.putExtra("user_id", currentUserId);
                startActivity(intent);
            }
        });
    }

    private void fetchPesanan()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    JSONArray jsonResponse = new JSONArray(response);
//                    JSONObject invoice = jsonResponse.getJSONObject(0);

                    // get JSON objects

                    JSONObject invoice = new JSONObject(response);

                    JSONArray items = invoice.getJSONArray("item");
                    JSONObject item = items.getJSONObject(0);

                    JSONObject customer = invoice.getJSONObject("customer");

                    // get information
                    invoiceId = invoice.getInt("id");
                    customerName = customer.getString("name");
                    itemName = item.getString("name");
                    orderDate = invoice.getString("date");
                    totalPrice = invoice.getDouble("totalPrice");
                    invoiceStatus = invoice.getString("INVOICE_STATUS");
                    invoiceType = invoice.getString("INVOICE_TYPE");

                    if (invoiceStatus.toLowerCase().equals(InvoiceStatus.UNPAID.toString().toLowerCase())) {
                        dueDate = invoice.getString("dueDate");
                    } else if (invoiceStatus.toLowerCase().equals(InvoiceStatus.INSTALLMENT.toString().toLowerCase())) {
                        installmentPeriod =  Integer.parseInt(invoice.getString("installmentPeriod"));
                    }

                    // test
                    Toast.makeText(SelesaiPesananActivity.this, customerName, Toast.LENGTH_SHORT).show();

                    // set visibility
                    invoiceIdView.setVisibility(View.VISIBLE);
                    customerNameView.setVisibility(View.VISIBLE);
                    itemNameView.setVisibility(View.VISIBLE);
                    orderDateView.setVisibility(View.VISIBLE);
                    totalPriceView.setVisibility(View.VISIBLE);
                    invoiceStatusView.setVisibility(View.VISIBLE);
                    invoiceTypeView.setVisibility(View.VISIBLE);
                    dueDateTitle.setVisibility(View.VISIBLE);
                    installmentPeriodTitle.setVisibility(View.VISIBLE);

                    if (invoiceStatus.toLowerCase().equals(InvoiceStatus.UNPAID.toString().toLowerCase())) {
                        dueDateView.setVisibility(View.VISIBLE);
                    } else if (invoiceStatus.toLowerCase().equals(InvoiceStatus.INSTALLMENT.toString().toLowerCase())) {
                        installmentPeriodView.setVisibility(View.VISIBLE);
                    }

                    cancel.setVisibility(View.VISIBLE);
                    finish.setVisibility(View.VISIBLE);

                    // set text
                    invoiceIdView.setText(invoiceId);
                    customerNameView.setText(customerName);
                    itemNameView.setText(itemName);
                    orderDateView.setText(orderDate);
                    totalPriceView.setText(""+totalPrice);
                    invoiceStatusView.setText(invoiceStatus);
                    invoiceTypeView.setText(invoiceType);

                    if (invoiceStatus.toLowerCase().equals(InvoiceStatus.UNPAID.toString().toLowerCase())) {
                        dueDateView.setText(dueDate);
                    } else if (invoiceStatus.toLowerCase().equals(InvoiceStatus.INSTALLMENT.toString().toLowerCase())) {
                        installmentPeriodView.setText(installmentPeriod);
                    }
                } catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("No orders found!").create().show();

                    Intent intent = new Intent(SelesaiPesananActivity.this, MainActivity.class);
                    intent.putExtra("user_id", currentUserId);
                    startActivity(intent);
                }
            }
        };

        PesananFetchRequest pesananFetchRequest = new PesananFetchRequest(currentUserId, getResources().getString(R.string.ip_address), responseListener);

        RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
        queue.add(pesananFetchRequest);
    }

    public void cancelTransaction()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // get JSON objects
                    JSONObject invoice = new JSONObject(response);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("Order canceled!").create().show();

                } catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("Order not canceled!").create().show();
                }
            }
        };

        PesananBatalRequest pesananBatalRequest = new PesananBatalRequest(invoiceId, getResources().getString(R.string.ip_address), responseListener);

        RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
        queue.add(pesananBatalRequest);
    }

    public void finishTransaction()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // get JSON objects
                    JSONObject invoice = new JSONObject(response);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("Order finished!").create().show();

                } catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelesaiPesananActivity.this);
                    builder.setMessage("Order not finished!").create().show();
                }
            }
        };

        PesananSelesaiRequest pesananSelesaiRequest = new PesananSelesaiRequest(invoiceId, getResources().getString(R.string.ip_address), responseListener);

        RequestQueue queue = Volley.newRequestQueue(SelesaiPesananActivity.this);
        queue.add(pesananSelesaiRequest);
    }
}
