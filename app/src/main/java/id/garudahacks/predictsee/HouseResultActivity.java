package id.garudahacks.predictsee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class HouseResultActivity extends AppCompatActivity {

    ProgressDialog loading;
    private TextView price;
    TextView bedrooms, bathrooms, sqft_living, sqft_lot, floors, yr_built, waterfront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBarOnly(this);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_result);

        String bedRooms = getIntent().getStringExtra("bedrooms");
        String bathRooms = getIntent().getStringExtra("bathrooms");
        String sqftLiving = getIntent().getStringExtra("sqft_living");
        String sqftLot = getIntent().getStringExtra("sqft_lot");
        String waterFront = getIntent().getStringExtra("waterfront");
        String Floors = getIntent().getStringExtra("floors");
        String yrBuild = getIntent().getStringExtra("yr_built");

        bedrooms = findViewById(R.id.bedrooms);
        bathrooms = findViewById(R.id.bathrooms);
        sqft_living = findViewById(R.id.area_living);
        sqft_lot = findViewById(R.id.area_lot);
        floors = findViewById(R.id.floors);
        waterfront = findViewById(R.id.waterfront);
        yr_built = findViewById(R.id.year_built);

        bedrooms.setText(bedRooms);
        bathrooms.setText(bathRooms);
        sqft_living.setText(sqftLiving);
        sqft_lot.setText(sqftLot);
        floors.setText(Floors);
        waterfront.setText(waterFront);
        yr_built.setText(yrBuild);

        price = findViewById(R.id.price);

        getItems();

        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HouseResultActivity.this, ThanksActivity.class));
            }
        });

    }

    private void getItems() {

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyacykt2RYemHR4ONu-zS_iyBDUqDLKwbXYfDPKK1nmJYRdBWc5/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    private void parseItems(String jsonResposnce) {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String price_result = jo.getString("price");
                price.setText(String.format("USD. %s", price_result.replaceAll("\\[", "").replaceAll("\\]","")));
                //Toast.makeText(this, price, Toast.LENGTH_SHORT).show();

                //HashMap<String, String> item = new HashMap<>();
                //item.put("price", price);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        loading.dismiss();
    }

    public void setTransparentStatusBarOnly(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}