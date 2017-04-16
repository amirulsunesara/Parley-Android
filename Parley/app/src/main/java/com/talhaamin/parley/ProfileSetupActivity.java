package com.talhaamin.parley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.talhaamin.parley.Models.User;

import java.io.FileNotFoundException;

public class ProfileSetupActivity extends AppCompatActivity {

    ImageView imgTarget;
    EditText edtName;
    Uri uri;
    Bundle bundle;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        imgTarget = (ImageView) findViewById(R.id.imgTarget);
        edtName = (EditText) findViewById(R.id.edtName);

        bundle = getIntent().getBundleExtra("user");


    }

    public void onClickImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            uri = targetUri;
            //textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imgTarget.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

        }
    }

    public void onClickSubmit(View v)
    {
        //TODO: save to firebase storage and call next activity
        if(edtName.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Enter a Username",Toast.LENGTH_SHORT).show();
        }
        else if(!hasImage(imgTarget))
        {
            Toast.makeText(this,"Select a profile Image",Toast.LENGTH_SHORT).show();
        }
        else {
//            progressDialog.setMessage("Setting up your profile...");
//            progressDialog.show();

            //getting userid from sharedpreferences for image key
            showProgressDialog();

          final User user = new User();
          String dsn =   edtName.getText().toString();
            //storing name in database
            user.displayName = edtName.getText().toString();
            user.userId = bundle.getString(getString(R.string.userId));
            user.password = bundle.getString(getString(R.string.password));


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference(getString(R.string.users)).child(user.userId);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //myRef.setValue()
                        //myRef.child(getString(R.string.displayName)).setValue(edtName.getText().toString());
                    }
                    else {
                        myRef.setValue(user);
                       // savetoApp(user);
//                        Toast.makeText(getApplicationContext(),"Your Profile has been created",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //Storing picture in firebase
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.fireBaseBundle)).child(getString(R.string.profilePictures));
            StorageReference userRef = storageRef.child(user.userId).child(user.userId);
            userRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ProfileSetupActivity.this,"Profile Setup Complete",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //Update Shared Preferences so that it knows profile has been set up
                    SharedPreferences sharedPreferences = MainActivity.context.getSharedPreferences(getString(R.string.preferencesFile),0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putBoolean(getString(R.string.isSetup), true);
                    editor.putString(getString(R.string.displayName), edtName.getText().toString());
                    editor.apply();
                    editor.commit();

                    user.picture = imgTarget;

                    savetoApp(user);
                    Intent intent = new Intent(ProfileSetupActivity.this,ChatListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ProfileSetupActivity.this,"Picture could not be uploaded",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public void savetoApp(User user)
    {
        ((App) this.getApplication()).setProfile(user);
    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing Data ...");
        progressDialog.show();

        // To Dismiss progress dialog
        //progressDialog.dismiss();
    }
}

