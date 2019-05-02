package praktikum.oop.jstore_android_athina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Supplier> listSupplier = new ArrayList<>();
    private ArrayList<Item> listItem= new ArrayList<>();
    private HashMap<Supplier, ArrayList<Item>> childMapping = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable_list);

        MainListAdapter listAdapter = new MainListAdapter(MainActivity.this, listSupplier, childMapping);
        expandableListView.setAdapter(listAdapter);
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

                        listSupplier.add(new Supplier(
                                supplier.getInt("id"),
                                supplier.getString("name"),
                                supplier.getString("email"),
                                supplier.getString("phoneNumber"),
                                location1
                                )
                        );

                        listItem.add(new Item(
                                item.getInt("id"),
                                item.getString("name"),
                                item.getInt("price"),
                                item.getString("category"),
                                item.getString("status"),
                                listSupplier.get(i)
                                )
                        );
                    }

                    for (int i = 0; i < listSupplier.size(); i++) {
                        childMapping.put(listSupplier.get(i), listItem);
                    }

                } catch (JSONException e) {

                }
            }
        };
    }
}
