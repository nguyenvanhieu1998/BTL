package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class ChiTietBaiDangActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtContent,txtDate,txtName,txtAccountType;
    ImageView imgFace,imgCallPhone,imgChat,imgMesseage;
    Intent intent = null;
    Bundle bundle = null;
    String uid ;
    String accountType,name,imageUrl;
    baiDang getBaiDang;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser userCurrent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_dang);
        initActionBar();
        addControls();
        setDataFromHome();
     //   getEvents();
    }
    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityChiTietBaiDang_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chi Tiết bài viết");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(ChiTietTroChuyenActivity.this,TrangChuActivity.class));
            }
        });
    }
//    private void getEvents() {
//        imgCallPhone.setOnClickListener(this);
//        imgChat.setOnClickListener(this);
//        imgMesseage.setOnClickListener(this);
//    }

    private void addControls() {
        imgFace = (ImageView) findViewById(R.id.id_ActivityChiTietBaiDang_imgThanhVien);
        imgCallPhone = (ImageView) findViewById(R.id.id_ActivityChiTietBaiDang_imgCall);
        imgChat = (ImageView) findViewById(R.id.id_ActivityChiTietBaiDang_imgChat);
        imgMesseage = (ImageView) findViewById(R.id.id_ActivityChiTietBaiDang_imgMesseage);
        txtName = (TextView) findViewById(R.id.id_ActivityChiTietBaiDang_txtName);
        txtAccountType = (TextView) findViewById(R.id.id_ActivityChiTietBaiDang_txtAccountType);
        txtContent = (TextView) findViewById(R.id.id_ActivityChiTietBaiDang_txtNoiDung);
        txtDate = (TextView) findViewById(R.id.id_ActivityChiTietBaiDang_txtThoiGianDang);
    }

    private void setDataFromHome() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        bundle = intent.getBundleExtra("name_post");
        if(bundle !=null)
        {
           getDataFromHome();
        }
         else
        {
            Toast.makeText(ChiTietBaiDangActivity.this, "Không có dữ liệu !", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataFromHome() {
        getBaiDang = (baiDang) bundle.getSerializable("key_post");
        uid = getBaiDang.getUid();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if(dataSnapshot1.getKey().equals(uid))
                    {
                        accountType = dataSnapshot1.child("accountType").getValue().toString();
                        name = dataSnapshot1.child("name").getValue().toString();
                        imageUrl = dataSnapshot1.child("photoURL").getValue().toString();
                        txtName.setText(name);
                        txtAccountType.setText(accountType);
                        final String sđt = dataSnapshot1.child("phone").getValue().toString();
                       txtContent.setText("1) Nội dung : "+"\n" + "- Hình thức : " + getBaiDang.getHinhThuc() + "\n" + "- Nội dung : " + getBaiDang.getContent() + "\n" + "- Số tiền : " + getBaiDang.getMoney()
                       + "\n" + "- Lãi suất : " + getBaiDang.getLaiSuat() + "\n" + "- Thời hạn : " + getBaiDang.getThoiHan());
                       txtDate.setText("2) Thời gian : " + getBaiDang.getDateWrite());
                        Picasso.with(ChiTietBaiDangActivity.this).load(imageUrl).into(imgFace);
                        imgCallPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextActivityCallPhone(sđt);
                            }
                        });
                        imgMesseage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextActivityMesseage(sđt);
                            }
                        });
                        imgChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nextActivityChat();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void nextActivityMesseage(String SDT) {
        intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.putExtra("sms_body","Chào bạn.....");
        intent.setData(Uri.parse("sms:" + SDT));
        startActivity(intent);
    }

    private void nextActivityChat() {
        intent = new Intent(ChiTietBaiDangActivity.this,ChiTietTroChuyenActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("uid",uid);
        intent.putExtra("imgUrl",imageUrl);
        startActivity(intent);
    }

    private void nextActivityCallPhone(String SDT) {
        intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:" + SDT));
        startActivity(intent);
    }
    public void status(final String Status)
    {
        String userCurrentID;
        userCurrent = FirebaseAuth.getInstance().getCurrentUser();
        userCurrentID = userCurrent.getUid();
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
        // databaseReference.removeEventListener(eventListener);
        status("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }
}
