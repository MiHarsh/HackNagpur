package com.example.testretro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class alzymerActivity extends AppCompatActivity {

    Button ch,up,Signout;

    TextView result1,result2,result3,result4;
    LinearLayout linear;

    TextView symptoms;
    ProgressBar progressBar;
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
        setContentView(R.layout.activity_alzymer);
        symptoms=findViewById(R.id.btn_symptoms);
        predict=findViewById(R.id.btnpredict);
        progressBar=findViewById(R.id.progress);

        result1=findViewById(R.id.result1);
        result2=findViewById(R.id.result2);
        result3=findViewById(R.id.result3);
        result4=findViewById(R.id.result4);
        linear=findViewById(R.id.linear);

        mAuth=FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();
        mStorageRef= FirebaseStorage.getInstance().getReference("Images");
        ch=(Button)findViewById(R.id.btnchoose);
        up=(Button)findViewById(R.id.btnupload);
        img=(ImageView)findViewById(R.id.imgview);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Signout=findViewById(R.id.signout);

        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.mayoclinic.org/diseases-conditions/alzheimers-disease/symptoms-causes/syc-20350447"));
                startActivity(intent);
            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                predict.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
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
                //up.setVisibility(View.VISIBLE);
                ch.setVisibility(View.GONE);
                Filechooser();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if(uploadTask!=null && uploadTask.isInProgress())
                {
                    Toast.makeText(alzymerActivity.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
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
                .baseUrl("https://medivision-hack.herokuapp.com/alzymer/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        urlService client =retrofit.create(urlService.class);
        Call<url_model> call=client.predict(m_url_model);
        call.enqueue(new Callback<url_model>() {
            @Override
            public void onResponse(Call<url_model> call, Response<url_model> response) {
                if(response.body()!=null)
                {
                    progressBar.setVisibility(View.GONE);
                    //predict.setVisibility(View.GONE);

                   linear.setVisibility(View.VISIBLE);

                    result1.setText("Mild Demented: "+response.body().getMild_Demented());
                    result2.setText("Moderate Demented: "+response.body().getModerate_Demented());
                    result3.setText("Non Demented: "+response.body().getNon_Demented());
                    result4.setText("Very Mild Demented: "+response.body().getVery_Mild_Demented());
                    //Toast.makeText(alzymerActivity.this, "Result"+response.body().getResult(), Toast.LENGTH_SHORT).show();
                    }
                else {
                    Toast.makeText(alzymerActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<url_model> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                up.setVisibility(View.VISIBLE);
                Toast.makeText(alzymerActivity.this, "something went wrong..Try again!", Toast.LENGTH_SHORT).show();
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
                                        //Toast.makeText(alzymerActivity.this, "Added", Toast.LENGTH_SHORT).show();
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
                                                //Toast.makeText(alzymerActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        progressBar.setVisibility(View.GONE);
                                        predict.setVisibility(View.VISIBLE);
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
        up.setVisibility(View.VISIBLE);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener
            navListner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            return true;

                        case R.id.nav_profile:
                            Intent Lintent=new Intent(getApplicationContext(),ProfileActivity.class);
                            Lintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Lintent);
                            finish();
                            return true;

                        case R.id.nav_info:
                            Intent Lintent1=new Intent(getApplicationContext(),InfoActivity.class);
                            Lintent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Lintent1);
                            finish();
                            return true;
                    }

                    return false;
                }
            };
    @Override
    public void onBackPressed() {

        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }

}