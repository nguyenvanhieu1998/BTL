package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import nguyenvanhieu.fithou.hotrovayvon1.Adapter.Custom_Dong_Info_ThanhVien_Adapter;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class DanhSachUserFilter extends AppCompatActivity {
    ListView lvUserFilter;
    ArrayList<member> arraylistMember;
    Custom_Dong_Info_ThanhVien_Adapter infoUserAdapter;
    Toolbar toolbar;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_user_filter);
        initDataToolbar();
        addControls();
        getDataFromTrangChu();
        addEvents();

    }

    private void getDataFromTrangChu() {
        Intent intent = getIntent();
        int i = intent.getIntExtra("key_itemMenu",0);
        switch (i)
        {
            case 3 :
                getSupportActionBar().setTitle("Danh sách Startup");
                viewListUserStartUp("Startup");
                break;
            case 4 :
                getSupportActionBar().setTitle("Danh sách Startup");
                viewListUserStartUp("Company");
                break;
            case 5 :
                getSupportActionBar().setTitle("Danh sách Startup");
                viewListUserStartUp("Person");
                break;
        }
    }

    private void viewListUserStartUp(final String accountType) {
        arraylistMember = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arraylistMember.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    member MB = dataSnapshot1.getValue(member.class);
                    //  assert MB != null;
                    //  assert firebaseUser != null;
                    if(MB.getAccountType().equals(accountType))
                    {
                        arraylistMember.add(MB);
                    }
                }
                infoUserAdapter = new Custom_Dong_Info_ThanhVien_Adapter(R.layout.custom_dong_info_thanhvien,DanhSachUserFilter.this,arraylistMember);
                lvUserFilter.setAdapter(infoUserAdapter);
               // userHorizAdapter = new UserHorizAdapter(getContext(),listMember);
             //   rcvUser.setAdapter(userHorizAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        lvUserFilter = (ListView) findViewById(R.id.id_ActivityDSUserFilter_lvUser);
    }

    private void initDataToolbar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityDSUserFilter_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Thông tin");
    }
    public void status(final String Status)
    {
        String userCurrentID;
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
