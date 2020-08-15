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
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HouseQuestionActivity extends AppCompatActivity {

    EditText bedrooms, bathrooms, sqft_living, sqft_lot, floors, yr_built;
    Switch waterfront;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBarOnly(this);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_question);

        bedrooms = findViewById(R.id.bedrooms);
        bathrooms = findViewById(R.id.bathrooms);
        sqft_living = findViewById(R.id.area_living);
        sqft_lot = findViewById(R.id.area_lot);
        floors = findViewById(R.id.floors);
        waterfront = findViewById(R.id.waterfront);
        yr_built = findViewById(R.id.year_built);

        Button buttonPredict = findViewById(R.id.button_predict);
        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToSheet();
                //startActivity(new Intent(HouseQuestionActivity.this, ThanksActivity.class));
            }
        });
    }

    private void addItemToSheet(){
        final ProgressDialog loading = ProgressDialog.show(this, "Adding Item", "Please Wait");
        final String bedRooms =  bedrooms.getText().toString().trim();
        final String bathRooms = bathrooms.getText().toString().trim();
        final String sqftLiving = sqft_living.getText().toString().trim();
        final String sqftLot = sqft_lot.getText().toString().trim();
        final String Floors = floors.getText().toString().trim();

        final String waterFront;
        if(waterfront.isChecked()){
            waterFront = waterfront.getTextOn().toString();
        }
        else{
            waterFront = waterfront.getTextOff().toString();
        }

        final String yrBuilt = yr_built.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzG3vQ4fOwol7-AA6io5N-U0bXXNJkHFnfGBgOxp8G40GGA7h8/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(HouseQuestionActivity.this, "", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HouseResultActivity.class);
                        intent.putExtra("bedrooms", bedRooms);
                        intent.putExtra("bathrooms", bathRooms);
                        intent.putExtra("sqft_living", sqftLiving);
                        intent.putExtra("sqft_lot", sqftLot);
                        intent.putExtra("floors", Floors);
                        intent.putExtra("waterfront", waterFront);
                        intent.putExtra("yr_built", yrBuilt);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("action","addItem");
                params.put("bedrooms", bedRooms);
                params.put("bathrooms", bathRooms);
                params.put("sqft_living", sqftLiving);
                params.put("sqft_lot", sqftLot);
                params.put("floors", Floors);
                params.put("waterfront", waterFront);
                params.put("yr_built", yrBuilt);

                return params;

            }
        };

        int socketTimeOut = 50000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(HouseQuestionActivity.this, MainActivity.class));
        finish();
    }

    public void setTransparentStatusBarOnly(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}