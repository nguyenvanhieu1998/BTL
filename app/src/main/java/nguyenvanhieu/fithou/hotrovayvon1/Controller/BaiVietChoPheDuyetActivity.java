package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import nguyenvanhieu.fithou.hotrovayvon1.Adapter.BaiVietChoPheDuyetAdapter;
import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class BaiVietChoPheDuyetActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView lvPostDuyet,lvPostKhongDuyet;
    BaiVietChoPheDuyetAdapter checkPostApdater;
    ArrayList<baiDang> arrayListBaiDang;
    Dialog dialog;
    Button dialog_btnDuyet,dialog_btnHuy;
    TextView dialog_txtName,dialog_txtContent,
    dialog_txtMoney,dialog_txtLaiSuat,dialog_txtThoiHan,dialog_txtDate;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String userCurrentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_viet_cho_phe_duyet);
        addControls();
        addControlsDialog();
        getPostViewListView();
        addEvents();
    }

    private void getPostViewListView() {
        //kiểm tra dữ liệu bên navigation gửi sang
        Intent intent = getIntent();
        int key = intent.getIntExtra("key_itemMenu",0);
        //nếu là 1 thì hiện danh sách chờ phê duyêt
        if(key==1)
        {
            getSupportActionBar().setTitle("Bài viết chờ phê duyệt");
            getPostFocusCheck();
        }
        if(key==2)
        {
            getSupportActionBar().setTitle("Bài viết không phê duyệt");
            getPostCancelCheck();
        }
    }
    private void getPostCancelCheck()
    {
        arrayListBaiDang = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListBaiDang.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        baiDang bd = dataSnapshot2.getValue(baiDang.class);
                        if(bd.getCheck()==0)
                        {
                            arrayListBaiDang.add(bd);
                        }
                    }
                }
                checkPostApdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        checkPostApdater = new BaiVietChoPheDuyetAdapter(R.layout.baivietchopheduyet_item,BaiVietChoPheDuyetActivity.this,arrayListBaiDang);
        lvPostKhongDuyet.setAdapter(checkPostApdater);
    }
    private void getPostFocusCheck()
    {
        arrayListBaiDang = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListBaiDang.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        baiDang bd = dataSnapshot2.getValue(baiDang.class);
                        if(bd.getCheck()==1)
                        {
                            arrayListBaiDang.add(bd);
                        }
                    }
                }
                checkPostApdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        checkPostApdater = new BaiVietChoPheDuyetAdapter(R.layout.baivietchopheduyet_item,BaiVietChoPheDuyetActivity.this,arrayListBaiDang);
        lvPostDuyet.setAdapter(checkPostApdater);
    }
    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvPostDuyet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final baiDang bd = arrayListBaiDang.get(position);
                dialog_txtName.setText("Người đăng : " + bd.getName());
                dialog_txtContent.setText("Nội dung : " + bd.getContent());
                dialog_txtMoney.setText("Số tiền : " + bd.getMoney());
                dialog_txtLaiSuat.setText("Lãi suất : " + bd.getLaiSuat());
                dialog_txtThoiHan.setText("Thời hạn : " + bd.getThoiHan());
                dialog_txtDate.setText("Ngày đăng " + bd.getDateWrite());
                dialog_btnDuyet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BaiVietChoPheDuyetActivity.this, "" + bd.getId(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog_btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       //Khi hủy không duyệt bài viết do bài viết k hợp lệ
                        cancelPost(bd.getId());
                    }
                });
                dialog_btnDuyet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPost(bd.getId());
                    }
                });
                dialog.show();
            }
        });
    }

    private void viewPost(final String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListBaiDang.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        baiDang BD = dataSnapshot2.getValue(baiDang.class);
                        if(BD.getId().equals(id))
                        {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("check",2);
                            dataSnapshot2.getRef().updateChildren(hashMap);
                        }
                    }
                }
                dialog.dismiss();
                getPostFocusCheck();
                //checkPostApdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cancelPost(final String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListBaiDang.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        baiDang BD = dataSnapshot2.getValue(baiDang.class);
                        if(BD.getId().equals(id))
                        {
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("check",0);
                            dataSnapshot2.getRef().updateChildren(hashMap);
                        }
                    }
                }
                dialog.dismiss();
                getPostFocusCheck();
                //checkPostApdater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addControlsDialog() {
        dialog_btnDuyet = (Button) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_btnPheDuyet);
        dialog_btnHuy = (Button) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_btnHuyBo);
        dialog_txtName = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtName);
       // dialog_txtAccountType = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtAccountType);
        dialog_txtContent = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtContent);
        dialog_txtMoney = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtMoney);
        dialog_txtLaiSuat = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtLaiSuat);
        dialog_txtThoiHan = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtThoiHan);
        dialog_txtDate = (TextView) dialog.findViewById(R.id.id_dialogBaiVietChoPheDuyet_txtThoiGian);
    }

    private void addControls() {
        lvPostKhongDuyet = (ListView) findViewById(R.id.id_ActivityChoPheDuyet_lvPostKhongDuyet);
        lvPostDuyet = (ListView) findViewById(R.id.id_ActivityChoPheDuyet_lvPostDuyet);
        toolbar = (Toolbar) findViewById(R.id.id_ActivityTrangChu_ActionBar);
        //Khai báo 1 đối tượng Dialog khoogn sử dụng giao diện mặc định của hệ thống.Tham số truyền vào là context
        dialog = new Dialog(BaiVietChoPheDuyetActivity.this);
        //Thiết lập giao diện cho đối tượng Dialog qua phương thức setContentView().Tham số truyền vào ResourceLayout
        dialog.setContentView(R.layout.dialog_baivietchopheduyet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        // databaseReference.removeEventListener(eventListener);
        status("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }
}
