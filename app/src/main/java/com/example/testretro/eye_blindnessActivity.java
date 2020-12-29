package com.example.testretro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class eye_blindnessActivity extends AppCompatActivity {
    Button ch,up,Signout;
    ImageView img;
    String recent="";
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    String current_user_id;
    Button predict;
    private StorageTask uploadTask;
    public Uri imguri;
    private DatabaseReference UsersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_blindness);
        predict=findViewById(R.id.btnpredict);
        mAuth=FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();
        mStorageRef= FirebaseStorage.getInstance().getReference("Images");
        ch=(Button)findViewById(R.id.btnchoose);
        up=(Button)findViewById(R.id.btnupload);
        img=(ImageView)findViewById(R.id.imgview);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Signout=findViewById(R.id.signout);
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_model user=new url_model(recent);
                sendNetworkRequest(user);
            }
        });

        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask!=null && uploadTask.isInProgress())
                {
                    Toast.makeText(eye_blindnessActivity.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    Fileuploader();
                }
            }
        });
    }

    private void sendNetworkRequest(url_model m_url_model)
    {
        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl("https://skinegy-final.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        urlService client =retrofit.create(urlService.class);
        Call<url_model> call=client.predict(m_url_model);
        call.enqueue(new Callback<url_model>() {
            @Override
            public void onResponse(Call<url_model> call, Response<url_model> response) {
                if(response.body()!=null)
                {
                    Toast.makeText(eye_blindnessActivity.this, "Result"+response.body().getResult(), Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(eye_blindnessActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<url_model> call, Throwable t) {
                Toast.makeText(eye_blindnessActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void Fileuploader()
    {
        final StorageReference Ref=mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        uploadTask=Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl=uri.toString();
                                HashMap m =new HashMap();
                                recent=downloadUrl;
                                m.put("recent",downloadUrl);
                                UsersRef.child(current_user_id).updateChildren(m).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        Toast.makeText(eye_blindnessActivity.this, "Added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Calendar calFordDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                                String saveCurrentDate = currentDate.format(calFordDate.getTime());

                                Calendar calFordTime = Calendar.getInstance();
                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                                String saveCurrentTime = currentTime.format(calFordDate.getTime());
                                int date=calFordDate.get(Calendar.DAY_OF_MONTH);
                                int month=calFordDate.get(Calendar.MONTH);
                                String postRandomName;
                                int year=calFordDate.get(Calendar.YEAR);
                                //postRandomName = saveCurrentDate + saveCurrentTime;
                                if(month<10)
                                {
                                    if(date<10)
                                        postRandomName=Integer.toString(year)+"0"+Integer.toString(month)+"0"+Integer.toString(date)+saveCurrentTime;
                                    else
                                        postRandomName=Integer.toString(year)+"0"+Integer.toString(month)+Integer.toString(date)+saveCurrentTime;
                                }
                                else
                                {
                                    if(date<10)
                                        postRandomName=Integer.toString(year)+Integer.toString(month)+"0"+Integer.toString(date)+saveCurrentTime;
                                    else
                                        postRandomName=Integer.toString(year)+Integer.toString(month)+Integer.toString(date)+saveCurrentTime;
                                }
                                HashMap imageMap=new HashMap();
                                imageMap.put("url",downloadUrl);
                                imageMap.put("result","aaa");
                                UsersRef.child(current_user_id).child("Images").child(postRandomName).updateChildren(imageMap)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                Toast.makeText(eye_blindnessActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                //Toast.makeText(MainActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
                            }
                        });
                        //String downloadUrl=imguri.toString();

                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
    private void Filechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imguri=data.getData();
            img.setImageURI(imguri);
        }

    }

}