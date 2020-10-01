package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import nguyenvanhieu.fithou.hotrovayvon1.Adapter.MyPostAdapter;
import nguyenvanhieu.fithou.hotrovayvon1.Adapter.PostAdapter;
import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class BaiVietCuaToiActivity extends AppCompatActivity {
    Toolbar toolbar;
    //RecyclerView rcvListMyPost;
    MyPostAdapter myPostAdapter;
    ArrayList<baiDang> baiDangArrayList;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String userCurrentID;
    ListView listView;
    PostAdapter postAdapter;
    TextView txtThongBao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_viet_cua_toi);
        initActionBar();
        addControls();
        initListView();
    }
    private void addControls() {
        listView = (ListView) findViewById(R.id.id_ActivityBaiVietCuaToi_ListMyPost);
        txtThongBao = (TextView) findViewById(R.id.id_ActivityBaiVietCuaToi_txtThongBao);
        // rcvListMyPost = (RecyclerView) findViewById(R.id.id_ActivityBaiVietCuaToi_rcvListMyPost);
    }
    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityBaiVietCuaToi_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bài viết của tôi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initListView() {
        baiDangArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userCurrentID = firebaseUser.getUid();
        Log.e("userCurrentID", userCurrentID);
        databaseReference.child("DanhSachPost").child(userCurrentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                baiDangArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    baiDang bd = dataSnapshot1.getValue(baiDang.class);
                    baiDangArrayList.add(bd);
                }
                if(baiDangArrayList.size()>0)
                {
                    txtThongBao.setVisibility(View.GONE);

                }
                else
                {
                    txtThongBao.setVisibility(View.VISIBLE);
                }
                Log.e("size : ","" + baiDangArrayList.size());
                postAdapter = new PostAdapter(R.layout.mypost_item,BaiVietCuaToiActivity.this,baiDangArrayList);
                listView.setAdapter(postAdapter);
                // myPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
//    private void initListview() {
//        baiDangArrayList = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        userCurrentID = firebaseUser.getUid();
//        Log.e("userCurrentID", userCurrentID);
//        databaseReference.child("DanhSachPost").child(userCurrentID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                baiDangArrayList.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    baiDang bd = dataSnapshot1.getValue(baiDang.class);
//                    baiDangArrayList.add(bd);
//                }
//                myPostAdapter = new MyPostAdapter(BaiVietCuaToiActivity.this,baiDangArrayList);
//                rcvListMyPost.setAdapter(myPostAdapter);
//               // myPostAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

}
