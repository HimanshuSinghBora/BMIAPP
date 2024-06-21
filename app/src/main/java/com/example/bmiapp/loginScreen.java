package com.example.bmiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class loginScreen extends AppCompatActivity {

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    Button login,signup;
    FirebaseDatabase database;
    String val2,val1;

    private final ActivityResultLauncher<Intent> activityResultLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK){
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
                    SharedPreferences Users= getSharedPreferences("Users",MODE_PRIVATE);
                    String userId = Users.getString("id","");
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                auth = FirebaseAuth.getInstance();
                                val1=auth.getCurrentUser().getDisplayName();
                                val2=auth.getCurrentUser().getEmail();
                                if(userId!="") {
                                    SharedPreferences.Editor editor= Users.edit();
                                    editor.putBoolean("flag",true);
                                    editor.apply();
                                    Intent iHome= new Intent(loginScreen.this, calculator.class);
                                    startActivity(iHome);
                                }
                                else{
                                    SharedPreferences.Editor editor= Users.edit();
                                    editor.putString("name",val1);
                                    editor.putString("email",val2);
                                    editor.apply();
                                    editor.putBoolean("flag",false);
                                    editor.apply();
                                    Intent iHome= new Intent(loginScreen.this, signInScreen.class);
                                    startActivity(iHome);
                                }
                                Toast.makeText(loginScreen.this,"Signed in successfully",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(loginScreen.this,"Failed to sigh in:"+task.getException(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        signup=findViewById(R.id.button2);
        login=findViewById(R.id.button);

        SharedPreferences Users= getSharedPreferences("Users",MODE_PRIVATE);
        Boolean check = Users.getBoolean("flag",false);
        String userId =Users.getString("id","");
        Boolean check1=(userId!="");
        if(check||check1){
            signup.setVisibility(View.INVISIBLE);
        }
        else{
            login.setVisibility(View.INVISIBLE);
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.client_id)).
                requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(loginScreen.this,gso);

        auth=FirebaseAuth.getInstance();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });
    }
}