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
import android.widget.Toast;

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

public class CarResultActivity extends AppCompatActivity {

    ProgressDialog loading;
    private TextView price;

    TextView wheelbase, enginesize, horsepower, fueleconomy, cardwith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBarOnly(this);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_result);

        String wheelBase = getIntent().getStringExtra("wheelbase");
        String engineSize = getIntent().getStringExtra("enginesize");
        String horsePower = getIntent().getStringExtra("horsepower");
        String fuelEconomy = getIntent().getStringExtra("fueleconomy");
        String carWidth = getIntent().getStringExtra("carwitdh");

        wheelbase = findViewById(R.id.wheelbase);
        enginesize = findViewById(R.id.enginesize);
        horsepower = findViewById(R.id.horsepower);
        fueleconomy = findViewById(R.id.fueleconomy);
        cardwith = findViewById(R.id.carwidth);

        wheelbase.setText(wheelBase);
        enginesize.setText(engineSize);
        horsepower.setText(horsePower);
        fueleconomy.setText(fuelEconomy);
        cardwith.setText(carWidth);

        price = findViewById(R.id.price);

        getItems();

        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarResultActivity.this, ThanksActivity.class));
            }
        });

    }

    private void getItems() {

        loading =  ProgressDialog.show(this,"Loading","please wait",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbygfNOouZyzJnkT1Q4R-w5lv4vOSIpdjwgSFTHoKQ5BQgorpKeZ/exec?action=getItems",
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