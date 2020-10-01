package nguyenvanhieu.fithou.hotrovayvon1.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Controller.BaiVietCuaToiActivity;
import nguyenvanhieu.fithou.hotrovayvon1.R;

import static android.app.Activity.RESULT_OK;

public class FragmentThongTin extends Fragment implements View.OnClickListener {
    View convertiew;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser userCurrent;
    String userCurrentID;
    viewHolder holder = null;
    int REQUEST_CODE = 1,READ_REQUEST_CODE = 2;
    StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(convertiew==null)
        {
            convertiew = inflater.inflate(R.layout.fragment_thongtin,container,false);
            addControls();

        }
        else
        {
            holder = (viewHolder) convertiew.getTag();
        }
        getProfileUserCurrent();
        addEvents();
        return convertiew;
    }
    public class viewHolder
    {
        TextView txtEmail,txtPhone,txtAddress, txtName,txtAccountType;
        ImageView imgCamera,imgFolder;
        CircleImageView imgFace;
        Button btnUpdate,btnMyPost;
    }
    private void getProfileUserCurrent() {
       // String PhotoUrl = getPhotoUrl();
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading profile user.......");
//        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userCurrent = firebaseAuth.getCurrentUser();
        userCurrentID = userCurrent.getUid();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameUserCurrent = dataSnapshot.child(userCurrentID).child("name").getValue().toString();
                String phoneUserCurrent = dataSnapshot.child(userCurrentID).child("phone").getValue().toString();
                String accountTypeUserCurrent = dataSnapshot.child(userCurrentID).child("accountType").getValue().toString();
                String addressUserCurrent = dataSnapshot.child(userCurrentID).child("address").getValue().toString();
                String emailUserCurrent = dataSnapshot.child(userCurrentID).child("email").getValue().toString();
                String photo = dataSnapshot.child(userCurrentID).child("photoURL").getValue().toString();
                String permission = dataSnapshot.child(userCurrentID).child("permission").getValue().toString();
                holder.txtName.setText(nameUserCurrent);
                holder.txtEmail.setText("Email : " + emailUserCurrent);
                holder.txtAddress.setText("Address : " + addressUserCurrent);
                holder.txtPhone.setText("Phone : " + phoneUserCurrent);
                Picasso.with(getActivity()).load(photo).into(holder.imgFace);
                if(permission.equals("admin"))
                {
                    holder.txtAccountType.setText("Adminstrator");
                }
                else
                {
                    holder.txtAccountType.setText(accountTypeUserCurrent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //progressDialog.cancel();
    }

//    private String getPhotoUrl() {
//
//        return ;
//    }

    private void addEvents() {
        holder.btnUpdate.setOnClickListener(this);
        holder.imgCamera.setOnClickListener(this);
        holder.imgFolder.setOnClickListener(this);
        holder.btnMyPost.setOnClickListener(this);
     //   holder.btnCheck.setOnClickListener(this);
    }

    private void addControls() {
        holder = new viewHolder();
        holder.txtEmail = (TextView) convertiew.findViewById(R.id.id_FragmentThongTin_txtEmail);
        holder.txtPhone = (TextView) convertiew.findViewById(R.id.id_FragmentThongTin_txtPhone);
        holder.txtAddress = (TextView) convertiew.findViewById(R.id.id_FragmentThongTin_txtAddress);
        holder.imgFace = (CircleImageView) convertiew.findViewById(R.id.id_FragmentThongTin_imgThanhVien);
        holder.imgCamera = (ImageView) convertiew.findViewById(R.id.id_FragmentThongTin_imgCamnera);
        holder.imgFolder = (ImageView) convertiew.findViewById(R.id.id_FragmentThongTin_imgFolder);
        holder.txtName = (TextView) convertiew.findViewById(R.id.id_FragmentThongTin_txtName);
        holder.txtAccountType = (TextView) convertiew.findViewById(R.id.id_FragmentThongTin_txtAccountType);
        holder.btnUpdate = (Button) convertiew.findViewById(R.id.id_FragmentThongTin_btnCapNhat);
        holder.btnMyPost = (Button) convertiew.findViewById(R.id.id_FragmentThongTin_btnBaiVietCuaToi);
      //  holder.btnCheck = (Button) convertiew.findViewById(R.id.id_FragmentThongTin_btnPheDuyet);
        convertiew.setTag(holder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_FragmentThongTin_btnCapNhat :
            FragmentDialogCapNhat fragmentDialogCapNhat = new FragmentDialogCapNhat();
            fragmentDialogCapNhat.show(getFragmentManager(),"frgDialogCapNhat");
            break;
            case R.id.id_FragmentThongTin_imgCamnera :
                getImageFromCamera();
                break;
            case R.id.id_FragmentThongTin_imgFolder :
                getImageFromFolder();
                break;
            case R.id.id_FragmentThongTin_btnBaiVietCuaToi :
                Intent intent = new Intent(getActivity(), BaiVietCuaToiActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getImageFromFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            holder.imgFace.setImageBitmap(bitmap);
            uploadImage();
        }
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            Uri uri = null;
            uri = data.getData();
            holder.imgFace.setImageURI(uri);
            uploadImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImage()
    {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = mStorageRef.child("User").child(userCurrentID +".png");

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = mStorageRef.child("images/" +userCurrentID+ ".png\"");

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        // Get the data from an ImageView as bytes
        holder.imgFace.setDrawingCacheEnabled(true);
        holder.imgFace.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) holder.imgFace.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                 Toast.makeText(getActivity(), "Cập nhật ảnh không thành công !", Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                  Toast.makeText(getActivity(), "Cập nhật ảnh thành công !", Toast.LENGTH_SHORT).show();
                  Log.d("aa",userCurrentID + ".png");
            }
        });
    }
}
