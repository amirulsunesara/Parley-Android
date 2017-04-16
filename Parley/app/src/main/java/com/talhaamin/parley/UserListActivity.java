package com.talhaamin.parley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.talhaamin.parley.Models.Chat;
import com.talhaamin.parley.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserListActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    UserListRVadapter adapter;
    Boolean started_re = false;
    EditText edtTitle;
    App app ;
    App app2;
    User mProfile;
    ProgressDialog progressDialog;

    List<User> user_list = new ArrayList<User>();
    Map<String,Object> temp_use;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("People");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        edtTitle = (EditText) findViewById(R.id.edtMessage);

        setRecyclerAdapters();
        app2 = ((App) this.getApplication());
        mProfile = ((App) this.getApplication()).getProfile();

        final String myID = ((App) this.getApplication()).getProfile().userId;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(getString(R.string.users));
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // build a user list
              final User user = dataSnapshot.getValue(User.class);

                if(!user.userId.equals(myID)){
                    // Geting user image
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.fireBaseBundle)).child(getString(R.string.profilePictures));
                    StorageReference userRef = storageRef.child(user.userId).child(user.userId);
                    userRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Log.v("imagelink", String.valueOf(uri));
                            user.picture = new ImageView(UserListActivity.this);
                            Picasso.with(UserListActivity.this)
                                    .load(String.valueOf(uri))
                                    .into(user.picture);
                            user_list.add(user);
                            user.pic_url = uri.toString();
//                            adapter.notifyDataSetChanged();

                            //start recycler view
                            if(user_list.size() > 0 )
                            {
                                started_re = true;
                                setRecyclerAdapters();

                            }
                            adapter.notifyDataSetChanged();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent i = new Intent(UserListActivity.this,MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    public void setRecyclerAdapters()
    {
        adapter = new UserListRVadapter(this,user_list);
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClicknext(View v)
    {
        if (Selections())
        {
            // some user is selected
            // is titles emppty
            if(edtTitle.getText().toString().isEmpty())
            {
                Toast.makeText(this,"Enter a Title",Toast.LENGTH_SHORT).show();
            }
            else{


                showProgressDialog();
                // see if unique

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference(getString(R.string.chats)).child(edtTitle.getText().toString());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(UserListActivity.this,"This topic already exists",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else
                        {
                            Chat chat = new Chat();
                            chat.topic = edtTitle.getText().toString();
                            List<User> selected_users = selectedUser();
                            chat.members.put("0", mProfile.userId);

                            // building member object

                            int key = 1;
                            for (User user: selected_users) {
                                chat.members.put(String.valueOf(key),user.userId);
                                key += 1;
                            }

                            myRef.setValue(chat);
                            User my = app2.getProfile();
                            int il = my.chats.keySet().size() + 1 ;
                            my.chats.put(String.valueOf(il),chat.topic);
                            app2.setProfile(my);

                            selected_users.add(mProfile);

                            // adding topics in userlists

                            for (User user: selected_users) {
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference myRef = database.getReference(getString(R.string.users)).child(user.userId).child(getString(R.string.chats)).push();
                                myRef.setValue(edtTitle.getText().toString());
                            }

                            app2.setUser_list(selected_users);

                            Intent i = new Intent(UserListActivity.this,MessengerActivity.class);
                            Bundle b = new Bundle();
                            b.putString(getString(R.string.topic) ,edtTitle.getText().toString());
                            i.putExtra("bundle",b);
                            progressDialog.dismiss();
                            startActivity(i);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
        else
        {
            Toast.makeText(this, "Please select one or more users", Toast.LENGTH_SHORT).show();

        }


    }

    public Boolean Selections()
    {
        for (User user:user_list) {
            if (user.isSelected)
            {
                return true;
            }

        }
        return false;

    }

    public List<User> selectedUser ()
    {
        List<User> selected_users = new ArrayList<>();
        for (User user: user_list) {
            if(user.isSelected)
            {
                selected_users.add(user);
            }
        }
        return  selected_users;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing Data ...");
        progressDialog.show();

        // To Dismiss progress dialog
        //progressDialog.dismiss();
    }
}
