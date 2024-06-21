package com.example.bmiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signInScreen extends AppCompatActivity {
    TextView name,email,weight,height;
    Button signIn,login,update;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name=findViewById(R.id.name);
        email=findViewById(R.id.mail);
        weight=findViewById(R.id.weight);
        height=findViewById(R.id.height);
        signIn=findViewById(R.id.button4);
        login=findViewById(R.id.button5);
        update=findViewById(R.id.button3);
        SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
        String name1 =Users.getString("name","");
        name.setText(name1);
        String email1 =Users.getString("email","");
        email.setText(email1);
        String userId = Users.getString("id","");
        Boolean modify = Users.getBoolean("update",false);
        if(modify){
            login.setVisibility(View.INVISIBLE);
            signIn.setVisibility(View.INVISIBLE);
        }
        else{
            update.setVisibility(View.INVISIBLE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
                SharedPreferences.Editor editor=Users.edit();
                editor.putBoolean("flag",false);
                editor.apply();

                Intent iHome= new Intent(signInScreen.this, loginScreen.class);
                startActivity(iHome);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
                SharedPreferences.Editor editor=Users.edit();
                editor.putBoolean("flag",true);
                editor.apply();
                String d3=weight.getText().toString();
                String d4=height.getText().toString();
                editor.putString("weight",d3);
                editor.putString("height",d4);
                editor.apply();
                DatabaseReference userReferences= FirebaseDatabase.getInstance().getReference("USER_DETAILS");
                String userId1 = userReferences.push().getKey();
                String name1 =Users.getString("name","");
                String email1 =Users.getString("email","");
                String weight1 =Users.getString("weight","");
                String height1 =Users.getString("height","");
                UserDetails userDetails = new UserDetails(name1,email1,weight1,height1);
                userReferences.child(userId1).setValue(userDetails);

                editor.putString("id",userId1);
                editor.apply();

                Intent iHome= new Intent(signInScreen.this, calculator.class);
                startActivity(iHome);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences Users=getSharedPreferences("Users",MODE_PRIVATE);
                SharedPreferences.Editor editor=Users.edit();
                editor.putBoolean("update",true);
                editor.apply();
                String d3=weight.getText().toString();
                String d4=height.getText().toString();
                editor.putString("weight",d3);
                editor.putString("height",d4);
                editor.apply();
                DatabaseReference userReferences= FirebaseDatabase.getInstance().getReference("USER_DETAILS");
                String name1 =Users.getString("name","");
                String email1 =Users.getString("email","");
                String weight1 =Users.getString("weight","");
                String height1 =Users.getString("height","");
                UserDetails userDetails = new UserDetails(name1,email1,weight1,height1);
                userReferences.child(userId).setValue(userDetails);

                editor.putString("id",userId);
                editor.apply();

                Intent iHome= new Intent(signInScreen.this, calculator.class);
                startActivity(iHome);
            }
        });

    }

}