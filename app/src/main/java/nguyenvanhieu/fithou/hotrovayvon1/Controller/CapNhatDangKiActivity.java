package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class CapNhatDangKiActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText edtName,edtPhone,edtAddress;
    RadioGroup rdgAccountType;
    RadioButton rdbtnCompany,rdbtnPerson,rdbtnStartup;
    Button btnUpdateData;
    String dataAccountType,userCurrentID;
    TextView txtTitleEmail;
    ImageView imgCamera,imgFolder,imgFace;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference mStorageRef;
    FirebaseUser user;
    String name,phone,address,testAccountType,email;
    member mb;
    ProgressDialog progressDialog;
    int REQUEST_CODE = 1;
    int READ_REQUEST_CODE = 2;
    public static final String status = "offline";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_dang_ki);
        initActionBar();
        addControls();
        addEvents();
        getEmailCurrentUser();
    }
    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityCapNhatDangKi_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cập nhật");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getEmailCurrentUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(CapNhatDangKiActivity.this,DangKiActivity.class));
        }
        else
        {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            txtTitleEmail.setText("" + user.getEmail());
        }
    }

    private void addEvents() {
        btnUpdateData.setOnClickListener(this);
        imgCamera.setOnClickListener(this);
        imgFolder.setOnClickListener(this);
    }

    private void addControls() {
        txtTitleEmail = (TextView) findViewById(R.id.id_ActivityCapNhatDangKi_txtEmail);
        edtName = (EditText) findViewById(R.id.id_ActivityCapNhatDangKi_edtName);
        edtPhone = (EditText) findViewById(R.id.id_ActivityCapNhatDangKi_edtPhone);
        edtAddress = (EditText) findViewById(R.id.id_ActivityCapNhatDangKi_edtAddress);
        rdgAccountType = (RadioGroup) findViewById(R.id.id_ActivityCapNhatDangKi_rdgAccountType);
        rdbtnCompany = (RadioButton) findViewById(R.id.id_ActivityCapNhatDangKi_rdCompany);
        rdbtnPerson = (RadioButton) findViewById(R.id.id_ActivityCapNhatDangKi_rdPerson);
        rdbtnStartup = (RadioButton) findViewById(R.id.id_ActivityCapNhatDangKi_rdStartup);
        btnUpdateData = (Button) findViewById(R.id.id_ActivityCapNhatDangKi_btnCapNhatThongTin);
        imgCamera = (ImageView) findViewById(R.id.id_ActivityCapNhatDangKi_imgCamera);
        imgFolder = (ImageView) findViewById(R.id.id_ActivityCapNhatDangKi_imgFolder);
        imgFace = (ImageView) findViewById(R.id.id_ActivityCapNhatDangKi_imgAnh);
    }
    public String getDataAccountType()
    {
        //final String dataAccountType ;
        rdgAccountType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.id_ActivityCapNhatDangKi_rdCompany:
                        dataAccountType = rdbtnCompany.getText().toString();
                        break;
                    case R.id.id_ActivityCapNhatDangKi_rdPerson:
                        dataAccountType = rdbtnPerson.getText().toString();
                        break;
                    case R.id.id_ActivityCapNhatDangKi_rdStartup:
                        dataAccountType = rdbtnStartup.getText().toString();
                        break;

                }

            }
        });
        return dataAccountType;

    }
    public void updateDataUser()
    {
         name = edtName.getText().toString().trim();
         phone = edtPhone.getText().toString().trim();
         address = edtAddress.getText().toString().trim();
         testAccountType = getDataAccountType();
        if(name.equals("")||phone.equals("")||address.equals("")||testAccountType==null)
        {
            Toast.makeText(this, "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        else
        {
            setDataForUser();
        }
    }

    private void setDataForUser() {

            databaseReference = FirebaseDatabase.getInstance().getReference();
            mStorageRef = FirebaseStorage.getInstance().getReference();
            user = firebaseAuth.getCurrentUser();
            email = user.getEmail();
            userCurrentID = user.getUid();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Đang cập nhật.........");
            progressDialog.show();
        StorageReference mountainsRef = mStorageRef.child("User").child(user.getUid()+".png");

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = mStorageRef.child("images/" +user.getUid()+ ".png\"");

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        // Get the data from an ImageView as bytes
        imgFace.setDrawingCacheEnabled(true);
        imgFace.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgFace.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                // Toast.makeText(CapNhatDangKiActivity.this, "Lỗi :  "+exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String photoURL = downloadUrl.toString();
                Log.d("aa",downloadUrl.toString());
                mb = new member(name,testAccountType,email,phone,address,userCurrentID,photoURL,status,"user");
                databaseReference.child("DanhSachUser").child(user.getUid()).setValue(mb, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError == null)
                        {
                            Toast.makeText(CapNhatDangKiActivity.this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(CapNhatDangKiActivity.this,TrangChuActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //Toast.makeText(CapNhatDangKiActivity.this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(CapNhatDangKiActivity.this, "Cập nhật không thành công !", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
         switch (v.getId())
         {
             case R.id.id_ActivityCapNhatDangKi_btnCapNhatThongTin:
                 updateDataUser();
                 break;
             case R.id.id_ActivityCapNhatDangKi_imgCamera:
                 getImageCamera();
                 break;
             case R.id.id_ActivityCapNhatDangKi_imgFolder:
                 getImageFolder();
                 break;
         }
    }

    private void getImageFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }
    private void getImageCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgFace.setImageBitmap(bitmap);
        }
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imgFace.setImageBitmap(bitmap);
                Uri uri = null;
                uri = data.getData();
                Log.i("AAAA", "Uri: " + uri.toString());
                imgFace.setImageURI(uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
