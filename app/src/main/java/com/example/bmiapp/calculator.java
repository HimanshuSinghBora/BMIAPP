package com.example.bmiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class calculator extends AppCompatActivity {
    TextView weightKgEditText, heightCmEditText;
    TextView weightLbsEditText, heightFtEditText, heightInEditText;
    Button calculateButton,logoutButton,updateButton;
    TextView bmiTextView, categoryTextView;
    Button toggleUnitsButton;
    View bmiResultCardView;
    String name,email;

    private boolean inMetricUnits;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        weightKgEditText = findViewById(R.id.weight_kgs);
        heightCmEditText = findViewById(R.id.height_cm);

        weightLbsEditText = findViewById(R.id.weight_lbs);
        heightFtEditText = findViewById(R.id.height_ft);
        heightInEditText = findViewById(R.id.height_in);

        calculateButton = findViewById(R.id.activity_main_calculate);
        toggleUnitsButton = findViewById(R.id.activity_main_toggleunits);
        logoutButton = findViewById(R.id.logout);
        updateButton=findViewById(R.id.update);

        bmiTextView = findViewById(R.id.activity_main_bmi);
        categoryTextView = findViewById(R.id.activity_main_category);
        bmiResultCardView = findViewById(R.id.activity_main_resultcard);

        inMetricUnits = true;
        updateInputsVisibility();
        bmiResultCardView.setVisibility(View.GONE);

        SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
        String userId =Users.getString("id","");
        DatabaseReference userReferences= FirebaseDatabase.getInstance().getReference("USER_DETAILS");
        final String[] w = new String[1];
        final String[] h = new String[1];
        userReferences.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userDetails=snapshot.getValue(UserDetails.class);
                w[0] =userDetails.getWeight();
                String weight=String.valueOf(w[0]);
                h[0] =userDetails.getHeight();
                String height=String.valueOf(h[0]);
                weightKgEditText.setText(weight);
                heightCmEditText.setText(height);
                int w=Integer.parseInt(weight);
                double w2=w*2.205;
                String weight_lbs=String.valueOf(w2);
                weightLbsEditText.setText(weight_lbs);
                int h= Integer.parseInt(height);
                double h2=h/30.48;
                String height_in=String.valueOf(h2);
                heightFtEditText.setText(String.valueOf(height_in.charAt(0)));
                heightInEditText.setText(String.valueOf(height_in.charAt(2)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inMetricUnits) {
                    if (weightKgEditText.length() == 0 || heightCmEditText.length() == 0) {
                        Toast.makeText(calculator.this, "Populate Weight and Height to Calculate BMI", Toast.LENGTH_SHORT).show();
                    } else {
                        double heightInCms = Double.parseDouble(heightCmEditText.getText().toString());
                        double weightInKgs = Double.parseDouble(weightKgEditText.getText().toString());
                        double bmi = BMICalcUtil.getInstance().calculateBMIMetric(heightInCms, weightInKgs);
                        displayBMI(bmi);
                    }
                } else {
                    if (weightLbsEditText.length() == 0 || heightFtEditText.length() == 0 || heightInEditText.length() == 0) {
                        Toast.makeText(calculator.this, "Populate Weight and Height to Calculate BMI", Toast.LENGTH_SHORT).show();
                    } else {
                        double heightFeet = Double.parseDouble(heightFtEditText.getText().toString());
                        double heightInches = Double.parseDouble(heightInEditText.getText().toString());
                        double weightLbs = Double.parseDouble(weightLbsEditText.getText().toString());
                        double bmi = BMICalcUtil.getInstance().calculateBMIImperial(heightFeet, heightInches, weightLbs);
                        displayBMI(bmi);
                    }
                }
            }
        });

        toggleUnitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inMetricUnits = !inMetricUnits;
                updateInputsVisibility();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
                SharedPreferences.Editor editor=Users.edit();
                editor.clear();
                editor.apply();
                editor.putString("id",userId);
                editor.apply();
                Intent iHome =new Intent(calculator.this, loginScreen.class);
                startActivity(iHome);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userReferences= FirebaseDatabase.getInstance().getReference("USER_DETAILS");
                final String[] n = new String[1];
                final String[] e = new String[1];
                userReferences.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDetails userDetails = snapshot.getValue(UserDetails.class);
                        n[0] = userDetails.getName();
                        name = String.valueOf(n[0]);
                        e[0] = userDetails.getEmail();
                        email=String.valueOf(e[0]);
                        SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
                        SharedPreferences.Editor editor= Users.edit();
                        editor.putString("name",name);
                        editor.putString("email",email);
                        editor.apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
                SharedPreferences.Editor editor= Users.edit();
                editor.putBoolean("update",true);
                editor.apply();
                Intent iHome =new Intent(calculator.this, signInScreen.class);
                startActivity(iHome);
            }
                });
    }

    private void updateInputsVisibility() {
        if (inMetricUnits) {
            heightCmEditText.setVisibility(View.VISIBLE);
            weightKgEditText.setVisibility(View.VISIBLE);
            heightFtEditText.setVisibility(View.GONE);
            heightInEditText.setVisibility(View.GONE);
            weightLbsEditText.setVisibility(View.GONE);
        } else {
            heightCmEditText.setVisibility(View.GONE);
            weightKgEditText.setVisibility(View.GONE);
            heightFtEditText.setVisibility(View.VISIBLE);
            heightInEditText.setVisibility(View.VISIBLE);
            weightLbsEditText.setVisibility(View.VISIBLE);
        }
    }

    private void displayBMI(double bmi) {
        bmiResultCardView.setVisibility(View.VISIBLE);

        bmiTextView.setText(String.format("%.2f", bmi));

        String bmiCategory = BMICalcUtil.getInstance().classifyBMI(bmi);
        categoryTextView.setText(bmiCategory);

        switch (bmiCategory) {
            case BMICalcUtil.BMI_CATEGORY_UNDERWEIGHT:
                bmiResultCardView.setBackgroundColor(Color.YELLOW);
                break;
            case BMICalcUtil.BMI_CATEGORY_HEALTHY:
                bmiResultCardView.setBackgroundColor(Color.GREEN);
                break;
            case BMICalcUtil.BMI_CATEGORY_OVERWEIGHT:
                bmiResultCardView.setBackgroundColor(Color.YELLOW);
                break;
            case BMICalcUtil.BMI_CATEGORY_OBESE:
                bmiResultCardView.setBackgroundColor(Color.RED);
                break;
        }
    }
}
class BMICalcUtil {
    public static final BMICalcUtil instance = new BMICalcUtil();

    private static final int CENTIMETERS_IN_METER = 100;
    private static final int INCHES_IN_FOOT = 12;
    private static final int BMI_IMPERIAL_WEIGHT_SCALAR = 703;

    public static final String BMI_CATEGORY_UNDERWEIGHT = "Underweight";
    public static final String BMI_CATEGORY_HEALTHY = "Healthy Weight Range";
    public static final String BMI_CATEGORY_OVERWEIGHT = "Overweight";
    public static final String BMI_CATEGORY_OBESE = "Obese";

    public static BMICalcUtil getInstance() {
        return instance;
    }

    public double calculateBMIMetric(double heightCm, double weightKg) {
        return (weightKg / ((heightCm / CENTIMETERS_IN_METER) * (heightCm / CENTIMETERS_IN_METER)));
    }

    public double calculateBMIImperial(double heightFeet, double heightInches, double weightLbs) {
        double totalHeightInInches = (heightFeet * INCHES_IN_FOOT) + heightInches;
        return (BMI_IMPERIAL_WEIGHT_SCALAR * weightLbs) / (totalHeightInInches * totalHeightInInches);
    }

    public String classifyBMI(double bmi) {
        if (bmi < 18.5) {
            return BMI_CATEGORY_UNDERWEIGHT;
        } else if (bmi >= 18.5 && bmi < 25) {
            return BMI_CATEGORY_HEALTHY;
        } else if (bmi >= 25 && bmi < 30) {
            return BMI_CATEGORY_OVERWEIGHT;
        } else {
            return BMI_CATEGORY_OBESE;
        }
    }
}
