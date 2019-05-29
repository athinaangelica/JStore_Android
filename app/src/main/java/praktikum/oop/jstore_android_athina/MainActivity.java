package praktikum.oop.jstore_android_athina;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Supplier> listSupplier = new ArrayList<>();
    private ArrayList<Item> listItem= new ArrayList<>();
    private HashMap<Supplier, ArrayList<Item>> childMapping = new HashMap<>();
    private int currentUserId;

    ExpandableListView expandableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        currentUserId = intent.getIntExtra("user_id", 0);

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);

        Button pesanan = (Button) findViewById(R.id.pesanan);

        refreshList();

        // kalo disini, dia somehow bikin list dulu baru keisi di refreshlist(), jadi dipindah ke dalem refreshlist()
//
//        MainListAdapter listAdapter = new MainListAdapter(MainActivity.this, listSupplier, childMapping);
//        expandableListView.setAdapter(listAdapter);

       expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
           public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
               Item selected = childMapping.get(listSupplier.get(i)).get(i1);

               Intent intent = new Intent(MainActivity.this, BuatPesananActivity.class);
               intent.putExtra("user_id", currentUserId);
               intent.putExtra("item", selected);
               startActivity(intent);

               return true;
           }
       });

       pesanan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
               intent.putExtra("user_id", currentUserId);
               startActivity(intent);
           }
       });
    }

    protected void refreshList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject item = jsonResponse.getJSONObject(i);
                        JSONObject supplier = item.getJSONObject("supplier");
                        JSONObject location = supplier.getJSONObject("location");

                        Location location1 = new Location(location.getString("city"), location.getString("province"), location.getString("description"));
                        //Toast.makeText(MainActivity.this, location1.getCity(), Toast.LENGTH_SHORT).show();

                        Supplier iSupplier = new Supplier(
                                supplier.getInt("id"),
                                supplier.getString("name"),
                                supplier.getString("email"),
                                supplier.getString("phoneNumber"),
                                location1
                        );

                        boolean supplierExists = false;

                        for (Supplier suppliers : listSupplier){
                            if(suppliers.getId() == supplier.getInt("id")) {
                                supplierExists = true;
                            }
                        }

                        if (!supplierExists) {
                            listSupplier.add(iSupplier);
                        }

                        listItem.add(new Item(
                                item.getInt("id"),
                                item.getString("name"),
                                item.getInt("price"),
                                item.getString("category"),
                                item.getString("status"),
                                iSupplier
                                )
                        );
                    }

                    for (int i = 0; i < listSupplier.size(); i++) {
                        ArrayList<Item> sItems = new ArrayList<>();

                        for (Item items : listItem){
                            if (items.getSupplier().getId() == listSupplier.get(i).getId()){
                                sItems.add(items);
                            }
                        }
                        childMapping.put(listSupplier.get(i), sItems);
                    }

                    MainListAdapter listAdapter = new MainListAdapter(MainActivity.this, listSupplier, childMapping);
                    expandableListView.setAdapter(listAdapter);

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Fetch Data Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        MenuRequest menuRequest = new MenuRequest(getResources().getString(R.string.ip_address), responseListener);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);

//        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);

//        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Please wait while retrieving data", Toast.LENGTH_SHORT).show();

//        MainListAdapter listAdapter = new MainListAdapter(MainActivity.this, listSupplier, childMapping);
//        expandableListView.setAdapter(listAdapter);
    }
}
