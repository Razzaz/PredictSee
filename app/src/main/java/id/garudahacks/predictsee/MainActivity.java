package id.garudahacks.predictsee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBarOnly(this);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView profileName = findViewById(R.id.profile_name);
        String fullname = user.getDisplayName();
        String[] firstname = fullname.split(" ");
        profileName.setText(String.format("Hi, %s!", firstname[0]));

        ImageView profilePhoto = findViewById(R.id.profile_photo);
        Glide.with(this)
                .load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(profilePhoto);

        ImageView buttonLogout = findViewById(R.id.logout_button);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish();
            }
        });

        ImageView buttonCars = findViewById(R.id.cars_button_grey);
        final ImageView buttonCarsGreen = findViewById(R.id.cars_button);
        buttonCarsGreen.setVisibility(View.INVISIBLE);

        buttonCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCarsGreen.setVisibility(View.VISIBLE);
                startActivity(new Intent(MainActivity.this, CarQuestionActivity.class));
                finish();
            }
        });

        ImageView buttonHouse = findViewById(R.id.house_button_grey);
        final ImageView buttonHouseGreen = findViewById(R.id.house_button);
        buttonHouseGreen.setVisibility(View.INVISIBLE);

        buttonHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonHouseGreen.setVisibility(View.VISIBLE);
                startActivity(new Intent(MainActivity.this, HouseQuestionActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public void setTransparentStatusBarOnly(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}