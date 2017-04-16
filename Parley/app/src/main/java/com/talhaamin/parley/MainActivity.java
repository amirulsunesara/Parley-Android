package com.talhaamin.parley;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.talhaamin.parley.Models.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    EditText edtPhone;

    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;
    boolean loggedin = false;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
      //  Log.v("nothing", FirebaseInstanceId.getInstance().getToken());Log.v("nothing", FirebaseInstanceId.getInstance().getToken());
        //SharedPreferences sp = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        context = this;
    }

    public void onClickRegister(View v) {
        final User user = new User();
        user.userId = edtPhone.getText().toString();
        user.password = edtPassword.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference(getString(R.string.users)).child(user.userId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(),"This Number is Already Registered",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Account Successfully Registered",Toast.LENGTH_SHORT).show();
                   // myRef.setValue(user);
                    Intent intent = new Intent(getApplicationContext(),ProfileSetupActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.userId),user.userId);
                    bundle.putString(getString(R.string.password),user.password);
                    intent.putExtra("user",bundle);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //myRef.child("first");


    }

    public void onClickLogin(final View v) {
        final User user = new User();
        user.userId = edtPhone.getText().toString();

        user.password = edtPassword.getText().toString();
        if(user.userId.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please provide phone number", Toast.LENGTH_SHORT).show();
        }
        else if(user.password.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please provide your password", Toast.LENGTH_SHORT).show();
        }
        else {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference(getString(R.string.users)).child(user.userId);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User chkuser = dataSnapshot.getValue(User.class);
                        if (chkuser.password.equals(user.password)) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            loggedin = true;
                            saveInfo(user);
                            savetoApp(chkuser);
                            SharedPreferences sp = context.getSharedPreferences(getString(R.string.preferencesFile), 0);

//                            if(sp.getBoolean(getString(R.string.isSetup),false))
//                            {
                                Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
                                startActivity(intent);
                                finish();
//                            }
//                            else {
//                                Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString(getString(R.string.userId),user.userId);
//                                intent.putExtra("user",bundle);
//                                startActivity(intent);
//                                //finish();
//                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }

                     else {
                        Toast.makeText(getApplicationContext(), "No such user exists", Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void saveInfo(User user)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preferencesFile), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(getString(R.string.userId),user.userId);
        editor.putString(getString(R.string.password),user.password);
        editor.putBoolean(getString(R.string.isLoggedIn),true);
        editor.apply();
    }

    public void savetoApp(User user)
    {
        ((App) this.getApplication()).setProfile(user);
    }
}

