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
import android.widget.ProgressBar;
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

public class CarQuestionActivity extends AppCompatActivity {

    EditText wheelbase, enginesize, horsepower, fueleconomy, cardwith;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBarOnly(this);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_question);

        wheelbase = findViewById(R.id.wheelbase);
        enginesize = findViewById(R.id.enginesize);
        horsepower = findViewById(R.id.horsepower);
        fueleconomy = findViewById(R.id.fueleconomy);
        cardwith = findViewById(R.id.carwidth);

        Button buttonPredict = findViewById(R.id.button_predict);
        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToSheet();
                //startActivity(new Intent(CarQuestionActivity.this, ThanksActivity.class));
            }
        });

    }

    private void addItemToSheet(){
        final ProgressDialog loading = ProgressDialog.show(this, "Adding Item", "Please Wait");
        final String wheelBase =  wheelbase.getText().toString().trim();
        final String engineSize = enginesize.getText().toString().trim();
        final String horsePower = horsepower.getText().toString().trim();
        final String fuelEconomy = fueleconomy.getText().toString().trim();
        final String cardWith = cardwith.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwPjjbbxCisIlcw_vOcGm7fNuAN-o6CAL6yDLAcXNzLELHOCitB/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Intent intent = new Intent(getApplicationContext(), CarResultActivity.class);
                        intent.putExtra("wheelbase", wheelBase);
                        intent.putExtra("enginesize", engineSize);
                        intent.putExtra("horsepower", horsePower);
                        intent.putExtra("fueleconomy", fuelEconomy);
                        intent.putExtra("carwitdh", cardWith);
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
                params.put("wheelbase", wheelBase);
                params.put("enginesize", engineSize);
                params.put("horsepower", horsePower);
                params.put("fueleconomy", fuelEconomy);
                params.put("carwidth", cardWith);

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
        startActivity(new Intent(CarQuestionActivity.this, MainActivity.class));
        finish();
    }

    public void setTransparentStatusBarOnly(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}