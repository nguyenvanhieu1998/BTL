package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Adapter.MessageAdapter;
import nguyenvanhieu.fithou.hotrovayvon1.Class.message;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class ChiTietTroChuyenActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 1;
    private static final int REQUEST_CODE = 2;
    Toolbar toolbar;
    CircleImageView imgFace;
    TextView txtName;
    EditText edtMessage;
    ImageView imgImage,imgSend;
    RecyclerView rcvMessage;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StorageReference mStorageRef;
    String userCurrentID;
    String typeDataSend = "text";
    String userID,imgUrl,name;
    ArrayList<message> listMessage;
    MessageAdapter messageAdapter;
    String  mess;
    ValueEventListener eventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_tro_chuyen);
        addControls();
        initActionBar();
        getDataFromChat();
        addEvents();
    }
    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityChiTietTroChuyen_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(ChiTietTroChuyenActivity.this,TrangChuActivity.class));
            }
        });
    }

    private void addControls() {
        imgFace = (CircleImageView) findViewById(R.id.id_ActivityChiTietTroChuyen_Toolbar_imgFace);
        txtName = (TextView) findViewById(R.id.id_ActivityChiTietTroChuyen_Toolbar_txtName);
        edtMessage = (EditText) findViewById(R.id.id_ActivityChiTietTroChuyen_edtWriteMessage);
        imgImage = (ImageView) findViewById(R.id.id_ActivityChiTietTroChuyen_imgImage);
        imgSend = (ImageView) findViewById(R.id.id_ActivityChiTietTroChuyen_imgSend);
        rcvMessage = (RecyclerView) findViewById(R.id.id_ActivityChiTietTroChuyen_rcvMessage);
        rcvMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        linearLayout.setStackFromEnd(true);
        rcvMessage.setLayoutManager(linearLayout);

    }
    private void getDataFromChat() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userCurrentID = firebaseUser.getUid();
        Intent intent = getIntent();
        userID = intent.getStringExtra("uid");
        imgUrl = intent.getStringExtra("imgUrl");
        name = intent.getStringExtra("name");
        txtName.setText(name);
        Picasso.with(getApplicationContext()).load(imgUrl).into(imgFace);
        readMessage(userCurrentID,userID,imgUrl);
        seenMessage(userID);
    }
    private void addEvents() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess = edtMessage.getText().toString();
                if(TextUtils.isEmpty(mess))
                {
                    Toast.makeText(ChiTietTroChuyenActivity.this, "Bạn phải nhập tin nhắn trước khi gửi !", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    sendMessage(userCurrentID, userID, mess, typeDataSend, 0);
                }
                edtMessage.setText("");
            }
        });
        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromFolder();
            }
        });
    }

    private void sendMessage(String sender,String receiver,String message,String type,int isseen) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            message ms = new message(sender,receiver,message,type,isseen);
            databaseReference.child("DanhSachChat").child(sender).child(receiver).push().setValue(ms);
            databaseReference.child("Chats").push().setValue(ms, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError==null)
                    {
                        Toast.makeText(ChiTietTroChuyenActivity.this, "Gửi tinh nhắn thành công !", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ChiTietTroChuyenActivity.this, "Không thể gửi tin nhắn !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
    private void readMessage(final String myID,final String userID,final String ImgUrl)
    {
        listMessage = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMessage.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    message MS = dataSnapshot1.getValue(message.class);
                    if(MS.getReceiver().equals(myID) && MS.getSender().equals(userID)
                            || MS.getReceiver().equals(userID) && MS.getSender().equals(myID))
                    {
                        listMessage.add(MS);
                    }
                }
                messageAdapter = new MessageAdapter(ChiTietTroChuyenActivity.this,listMessage,ImgUrl);
                rcvMessage.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void seenMessage(final String uid)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //    userCurrentID = firebaseUser.getUid();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    message mes = dataSnapshot1.getValue(message.class);
                    if(mes.getReceiver().equals(firebaseUser.getUid()) && mes.getSender().equals(uid))
                    {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen",1);
                        dataSnapshot1.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getImageFromFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

//    private void getImageFromCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,REQUEST_CODE);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null)
//        {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//           // holder.imgFace.setImageBitmap(bitmap);
//            uploadImage();
//        }
        if(requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Uri imgUrl = null;
            imgUrl = data.getData();
            DatabaseReference user_message_push = databaseReference.child("Chats").push();
            final String key_push = user_message_push.getKey();
            mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference mountainsRef = mStorageRef.child("Chats").child(key_push +".png");

// Create a reference to 'images/mountains.jpg'
            StorageReference mountainImagesRef = mStorageRef.child("images/" +key_push+ ".png\"");

// While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
            mountainsRef.putFile(imgUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                      if(task.isSuccessful())
                      {
                              String dowload_url = task.getResult().getDownloadUrl().toString();
                              sendMessage(firebaseUser.getUid(),userID,dowload_url,"image",0);
                      }
                }
            });
            //uploadImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadImage() {

    }

public void status(final String Status)
{
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    userCurrentID = firebaseUser.getUid();
    Log.e("userID",userCurrentID);
    databaseReference = FirebaseDatabase.getInstance().getReference("DanhSachUser").child(userCurrentID);
    HashMap<String,Object> hashMap = new HashMap<>();
    hashMap.put("status",Status);
    databaseReference.updateChildren(hashMap);
}

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(eventListener);
        status("offline");
    }
}