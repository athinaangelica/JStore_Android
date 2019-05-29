package praktikum.oop.jstore_android_athina;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BuatPesananActivity extends AppCompatActivity {

    private int currentUserId;
    private int itemId;
    private String itemName;
    private String itemCategory;
    private String itemStatus;
    private double itemPrice;
    private int installmentPeriod;
    private String selectedPayment;
    private InvoiceStatus invoiceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_pesanan);

        // get intent and extras
        Intent intent = getIntent();
        currentUserId = intent.getIntExtra("user_id", 0);
        Item item = (Item) intent.getSerializableExtra("item");

        itemId = item.getId();
        itemName = item.getName();
        itemPrice = item.getPrice();
        itemCategory = item.getCategory();
        itemStatus = item.getStatus();


        //initialize layout items
        final TextView itemNameView = (TextView) findViewById(R.id.item_name);
        final TextView itemCategoryView = (TextView) findViewById(R.id.item_category);
        final TextView itemStatusView = (TextView) findViewById(R.id.item_status);
        final TextView itemPriceView = (TextView) findViewById(R.id.item_price);
        final TextView totalPriceView = (TextView) findViewById(R.id.total_price);
        final TextView installmentPeriodText = (TextView) findViewById(R.id.textPeriod);
        final EditText installmentPeriodInput = (EditText) findViewById(R.id.installment_period);
        final Button orderButton = (Button) findViewById(R.id.order);
        final Button countButton = (Button) findViewById(R.id.count);
        final RadioGroup paymentMethodGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton paidRadio = (RadioButton) findViewById(R.id.paid);
        final RadioButton unpaidRadio  = (RadioButton) findViewById(R.id.unpaid);
        final RadioButton installmentRadio  = (RadioButton) findViewById(R.id.installment);

        // set visibility to GONE
        orderButton.setVisibility(View.GONE);
        installmentPeriodText.setVisibility(View.GONE);
        installmentPeriodInput.setVisibility(View.GONE);

        // set field values
        String itemPriceString = "" + itemPrice;

        itemNameView.setText(itemName);
        itemCategoryView.setText(itemCategory);
        itemStatusView.setText(itemStatus);
        itemPriceView.setText(itemPriceString);
        totalPriceView.setText("0");

        // set onCheckedListener
        paymentMethodGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (installmentRadio.isChecked()){
                    installmentPeriodText.setVisibility(View.VISIBLE);
                    installmentPeriodInput.setVisibility(View.VISIBLE);
                } else {
                    installmentPeriodText.setVisibility(View.GONE);
                    installmentPeriodInput.setVisibility(View.GONE);
                }
            }
        });

        countButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(installmentRadio.isChecked()){
                    installmentPeriod = Integer.parseInt(installmentPeriodInput.getText().toString());
                    double totalPrice = itemPrice/installmentPeriod;
                    String totalPriceString = "" + totalPrice;

                    totalPriceView.setText(totalPriceString);
                } else {
                    String totalPriceString = "" + itemPrice;

                    totalPriceView.setText(totalPriceString);
                }

                countButton.setVisibility(View.GONE);
                orderButton.setVisibility(View.VISIBLE);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuatPesananActivity.this);
                                builder.setMessage("Order Placed!").create().show();
                            }
                        }
                        catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BuatPesananActivity.this);
                            builder.setMessage("Order Failed!").create().show();
                        }
                    }
                };

                BuatPesananRequest buatPesananRequest;

                if (paidRadio.isChecked()) {
                    invoiceStatus = InvoiceStatus.PAID;
                    buatPesananRequest = new BuatPesananRequest(itemId, currentUserId, invoiceStatus, getResources().getString(R.string.ip_address), responseListener);
                }
                else if (unpaidRadio.isChecked()) {
                    invoiceStatus = InvoiceStatus.UNPAID;
                    buatPesananRequest = new BuatPesananRequest(itemId, currentUserId, invoiceStatus, getResources().getString(R.string.ip_address), responseListener);
                }
                else {
                    invoiceStatus = InvoiceStatus.INSTALLMENT;
                    buatPesananRequest = new BuatPesananRequest(itemId, currentUserId, invoiceStatus, installmentPeriodInput.getText().toString(), getResources().getString(R.string.ip_address), responseListener);
                }

                RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                queue.add(buatPesananRequest);
            }
        });
    }
}
