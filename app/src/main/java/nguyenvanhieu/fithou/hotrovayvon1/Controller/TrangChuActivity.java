package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.Fragment.FragmentThongTin;
import nguyenvanhieu.fithou.hotrovayvon1.Fragment.FragmentTrangChu;
import nguyenvanhieu.fithou.hotrovayvon1.Fragment.FragmentTroChuyen;
import nguyenvanhieu.fithou.hotrovayvon1.R;
//import nguyenvanhieu.fithou.hotrovayvon1.Service.NotificationService;

public class TrangChuActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomView;
    FrameLayout frameLayoutContainer;
    FragmentTransaction fragmentTransaction;
    FragmentThongTin fragmentThongTin;
    FragmentTrangChu fragmentTrangChu;
    FragmentTroChuyen fragmentTroChuyen;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String userCurrentID;
    TextView txtName;
    CircleImageView imgAdmin;
    DrawerLayout drawerLayout;
    Intent NotiService;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String admin = "admin";
    int check = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
     //   actionBar = getSupportActionBar();
        addControls();
        addEvents();
        initData();
        initDataFormAdmin();
        addEventsFormAdmin();
      //  kiemTra();
        //getUserID();
    }
    void MainStartService(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //NotiService = new Intent(TrangChuActivity.this, NotificationService.class);
        NotiService.putExtra("UserUID", firebaseUser.getUid());
        startService(NotiService);
    }
    void MainStopService(){
        stopService(NotiService);
    }
    private void addEventsFormAdmin() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //menuItem.setCheckable(true);
                drawerLayout.closeDrawers();
                Intent intent = null;
              //  Toast.makeText(TrangChuActivity.this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                final int menuItemID = menuItem.getItemId();
                switch (menuItemID)
                {
                    case R.id.id_menu_baiVietChoDuyet :
                          check = 1;
                        break;
                    case R.id.id_menu_baiVietKhongDuyet:
                        check = 2;
                        break;
                    case R.id.id_menu_DSStartUp :
                        check = 3;
                        break;
                    case R.id.id_menu_DSCompany:
                        check = 4;
                        break;
                    case R.id.id_menu_DSPerson:
                        check = 5;
                        break;
                }
                if(check==1 || check==2)
                {
                    intent= new Intent(TrangChuActivity.this,BaiVietChoPheDuyetActivity.class);
                    intent.putExtra("key_itemMenu",check);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_intent_trai_sang_phai,R.anim.anim_intent_exit);
                }
                else
                {
                    intent= new Intent(TrangChuActivity.this,DanhSachUserFilter.class);
                    intent.putExtra("key_itemMenu",check);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_intent_trai_sang_phai,R.anim.anim_intent_exit);
                }
                return true;
            }
        });

    }
//    private void kiemTra()
//    {
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        userCurrentID = firebaseUser.getUid();
//        Log.e("userCurrentID", userCurrentID);
//        databaseReference.child("DanhSachPost").child(userCurrentID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//             //   baiDangArrayList.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    baiDang bd = dataSnapshot1.getValue(baiDang.class);
//                    if(bd.getCheck()==2)
//                    {
//                        taoNotification(bd.getId());
//                    }
//                  //  baiDangArrayList.add(bd);
//                }
//              //  Log.e("size : ","" + baiDangArrayList.size());
//               // postAdapter = new PostAdapter(R.layout.mypost_item,BaiVietCuaToiActivity.this,baiDangArrayList);
//              //  listView.setAdapter(postAdapter);
//                // myPostAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//    private void taoNotification(String message) {
//        int notifiID;
//        //Tạo builder để tạo 1 notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(TrangChuActivity.this)
//                .setContentTitle("Thông báo")
//                .setSmallIcon(android.R.drawable.ic_dialog_email)
//                .setContentText("Bài viết " + message + " đã được duyệt");
//        //Tạo Pending Intent để khi click vào notification đó sẽ chuyển qua màn hình khác.
//        Intent intent = new Intent(TrangChuActivity.this,BaiVietCuaToiActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        //Tạo uri lấy ra âm thông báo mặc định của máy
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        //Có thể thiết lập bằng 1 âm báo mình làm.
//      //  Uri  newSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.deanhbenem);
//        //thiết lập âm báo cho notififcation builder
//       // builder.setSound(newSound);
//        notifiID = 113;
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(notifiID,builder.build());
//    }

    private void initDataFormAdmin() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        navigationView = (NavigationView) findViewById(R.id.id_NaviView);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        checkAdmin();

    }

    private void checkAdmin() {
         databaseReference = FirebaseDatabase.getInstance().getReference();
         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         databaseReference.child("DanhSachUser").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                    member mb = dataSnapshot.getValue(member.class);
                    if(mb.getPermission().equals(admin))
                    {
                        txtName.setText(mb.getName());
                        Picasso.with(TrangChuActivity.this).load(mb.getPhotoURL()).into(imgAdmin);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }

    private void initData() {
       // MainStartService();
        getSupportActionBar().setTitle("Thông tin");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_ActivityTrangChu_FrameContainer,new FragmentThongTin(),"frgThongTin")
                .addToBackStack(null)
                .commit();
    }

    private void addEvents() {
       bottomView.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void addControls() {
        txtName = (TextView) findViewById(R.id.id_Navigation_Header_txtName);
        imgAdmin = (CircleImageView) findViewById(R.id.id_Navigation_Header_imgFace);
        toolbar = (Toolbar) findViewById(R.id.id_ActivityTrangChu_ActionBar);
        setSupportActionBar(toolbar);
        bottomView = (BottomNavigationView) findViewById(R.id.id_ActivityTrangChu_bottomNavi);
        frameLayoutContainer = (FrameLayout) findViewById(R.id.id_ActivityTrangChu_FrameContainer);
    }
    public BottomNavigationView.OnNavigationItemSelectedListener navListener;

    {
        navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.id_bottomThongTin:
                        getSupportActionBar().setTitle("Thông tin");
                        selectedFragment = new FragmentThongTin();
                        loadFragment(selectedFragment, "frgThongTin");
                        return true;
                    case R.id.id_bottomTrangChu:
                        getSupportActionBar().setTitle("Trang chủ");
                        selectedFragment = new FragmentTrangChu();
                        loadFragment(selectedFragment, "frgTrangChu");
                        return true;
                    case R.id.id_bottomTroChuyen:
                        getSupportActionBar().setTitle("Trò Chuyện");
                        selectedFragment = new FragmentTroChuyen();
                        loadFragment(selectedFragment, "frgTroChuyen");
                        return true;
                }
                return true;
            }
        };
    }

    private void loadFragment(Fragment a, String nameFragment) {
       getSupportFragmentManager().beginTransaction()
               .replace(R.id.id_ActivityTrangChu_FrameContainer,a,nameFragment)
               .addToBackStack(null)
               .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.id_menu_logout:
              //  FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TrangChuActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    //    @Override
//    protected void onStart() {
//        super.onStart();
//        status("online");
//
//    }

//    @Override
//    protected void onStop() {
//        //databaseReference.removeEventListener(eventListener);
//        super.onStop();
//        status("offline");
//    }
}
