package nguyenvanhieu.fithou.hotrovayvon1.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Adapter.UserAdapter;
import nguyenvanhieu.fithou.hotrovayvon1.Adapter.UserHorizAdapter;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.Class.message;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class FragmentTroChuyen extends Fragment {
    //DatabaseReference databaseReference;
    //FirebaseAuth firebaseAuth;
   // FirebaseUser userCurrent;
    View convertView;
   // viewPaperAdapter viewAdapter;
    //SearchView servChat;
   // ListView lvChat;
   // ArrayList<chat> chatArrayList;
    String userCurrentID;
   // CircleImageView circleImageFace ;
    RecyclerView rcvUser,rcvUserChat;
    UserAdapter userAdapter,userFilterAdapter;
    UserHorizAdapter userHorizAdapter;
    ArrayList<member> listMember,listMemberChat,listMemberFilter;
    ArrayList<String> arrIDUser,arrIDUser2;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ViewPager viewPager;
    TabLayout tabLayoutMain;
    CircleImageView imgFace;
    TextView txtName;
    EditText edtTimKiem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_trochuyen,container,false);
        addControls();
        initData();
     //   getListUser();
        getListUserChat();

        return convertView;
    }
    public void initData()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userCurrentID = FirebaseAuth.getInstance().getUid();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameUserCurrent = dataSnapshot.child(userCurrentID).child("name").getValue().toString();
                String photo = dataSnapshot.child(userCurrentID).child("photoURL").getValue().toString();
                txtName.setText(nameUserCurrent);
                Picasso.with(getActivity()).load(photo).into(imgFace);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addControls() {
        edtTimKiem = (EditText) convertView.findViewById(R.id.id_FragmentTroChuyen_edtTimKiem);
        imgFace = (CircleImageView) convertView.findViewById(R.id.id_FragmentTroChuyen_FrameInfo_imgFace) ;
        txtName = (TextView) convertView.findViewById(R.id.id_FragmentTroChuyen_FrameInfo_txtName) ;
        rcvUserChat =(RecyclerView) convertView.findViewById(R.id.id_FragmentTroChuyen_FrameDSUser_rcvListUserChat);
        rcvUser =(RecyclerView) convertView.findViewById(R.id.id_FragmentTroChuyen_FrameDSUser_rcvListUser);
        rcvUser.setHasFixedSize(true);
        rcvUserChat.setHasFixedSize(true);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        rcvUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        edtTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search2();
            }
        });
        rcvUserChat.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void getListUserChat() {
        arrIDUser = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userCurrentID = firebaseUser.getUid();
        databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             //   listMemberChat.clear();
                arrIDUser.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    message ms = dataSnapshot1.getValue(message.class);
                    if(ms.getSender().equals(userCurrentID))
                    {
                        arrIDUser.add(ms.getReceiver());
                    }
                    if(ms.getReceiver().equals(userCurrentID))
                    {
                        arrIDUser.add(ms.getSender());
                    }
                }
                readUserChat();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void readUserChat() {
        listMemberChat = new ArrayList<>();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMemberChat.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    member MB = dataSnapshot1.getValue(member.class);
                    for (String id : arrIDUser)
                    {
                        if(MB.getUid().equals(id))
                        {
                            if(listMemberChat.size()!=0)
                            {
                                for(member m : listMemberChat)
                                {
                                    if(!MB.getUid().equals(m.getUid()))
                                    {
                                        listMemberChat.add(MB);
                                    }
                                }
                            }
                            else
                            {
                                listMemberChat.add(MB);
                            }
                        }
                    }
                }
                //userAdapter = new UserAdapter(getContext(),listMemberChat);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userAdapter = new UserAdapter(getContext(),listMemberChat);
        rcvUserChat.setAdapter(userAdapter);
    }

//    private void getListUser() {
//        listMember = new ArrayList<>();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                listMember.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    member MB = dataSnapshot1.getValue(member.class);
//                    //  assert MB != null;
//                    //  assert firebaseUser != null;
//                    if(!MB.getUid().equals(firebaseUser.getUid()))
//                    {
//                        listMember.add(MB);
//                    }
//                }
//                userHorizAdapter = new UserHorizAdapter(getContext(),listMember);
//                rcvUser.setAdapter(userHorizAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
private void getListUserChat2() {
    arrIDUser2 = new ArrayList<>();
    databaseReference = FirebaseDatabase.getInstance().getReference();
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    userCurrentID = firebaseUser.getUid();
    databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //   listMemberChat.clear();
            arrIDUser2.clear();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
            {
                message ms = dataSnapshot1.getValue(message.class);
                if(ms.getSender().equals(userCurrentID))
                {
                    arrIDUser2.add(ms.getReceiver());
                }
                if(ms.getReceiver().equals(userCurrentID))
                {
                    arrIDUser2.add(ms.getSender());
                }
            }
            readUserChat2();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
    private void readUserChat2() {
        listMemberFilter = new ArrayList<>();
        final String getStringSearch = edtTimKiem.getText().toString().toLowerCase().trim();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMemberFilter.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    member MB = dataSnapshot1.getValue(member.class);
                    for (String id : arrIDUser2)
                    {
                        if(MB.getUid().equals(id))
                        {
                            if(listMemberFilter.size()!=0)
                            {
                                for(member m : listMemberFilter)
                                {
                                    if(!MB.getUid().equals(m.getUid()))
                                    {
                                        String ten = MB.getName();
                                        ten = ten.toLowerCase();
                                        if(ten.contains(getStringSearch)){
                                            listMemberFilter.add(MB);
                                        }
                                        Log.e("Search", "name 1 :" + ten + "name 2 :" + getStringSearch);
                                        //listMemberFilter.add(MB);
                                    }
                                }
                            }
//                                        else
//                                        {
//                                            listMemberChat.add(MB);
//                                        }

                        }
                    }
                }
                userFilterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userFilterAdapter = new UserAdapter(getContext(),listMemberFilter);
        rcvUserChat.setAdapter(userFilterAdapter);
    }
    public void searchListUser()
    {
        arrIDUser2 = new ArrayList<>();
        listMemberFilter = new ArrayList<>();
        final String getStringSearch = edtTimKiem.getText().toString().toLowerCase().trim();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userCurrentID = firebaseUser.getUid();
        databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //   listMemberChat.clear();
                arrIDUser2.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    message ms = dataSnapshot1.getValue(message.class);
                    if(ms.getSender().equals(userCurrentID))
                    {
                        arrIDUser2.add(ms.getReceiver());
                    }
                    if(ms.getReceiver().equals(userCurrentID))
                    {
                        arrIDUser2.add(ms.getSender());
                    }
                }
                databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listMemberFilter.clear();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                        {
                            member mb = dataSnapshot1.getValue(member.class);
                            for (String id : arrIDUser2 )
                            {
                                if(id.equals(mb.getUid()))
                                {
                                    if(listMemberFilter.size()!=0)
                                    {
                                        for(member m : listMemberFilter)
                                        {
                                            if(!mb.getUid().equals(m.getUid()))
                                            {
                                                String ten = mb.getName();
                                                ten = ten.toLowerCase();
                                                if(ten.contains(getStringSearch)){
                                                    listMemberFilter.add(mb);
                                                }
                                                Log.e("Search", "name 1 :" + ten + "name 2 :" + getStringSearch);
                                                //listMemberFilter.add(MB);
                                            }
                                        }
                                    }
                                    else
                                        {
                                            String ten = mb.getName();
                                            ten = ten.toLowerCase();
                                            if(ten.contains(getStringSearch)){
                                                listMemberFilter.add(mb);
                                            }
                                            Log.e("Search", "name 1 :" + ten + "name 2 :" + getStringSearch);
                                        }
                                }
                            }


                        }
                        userFilterAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                userFilterAdapter = new UserAdapter(getContext(),listMemberFilter);
                rcvUserChat.setAdapter(userFilterAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void Search2()
    {
        if(edtTimKiem.getText().toString().isEmpty())
        {
           // userAdapter = new UserAdapter(getContext(),listMemberChat);
            // baiDangAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),baiDangArrayList);
//            userAdapter.notifyDataSetChanged();
//            rcvUserChat.setAdapter(userAdapter);
            getListUserChat();
        }
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String getStringSearch2 = edtTimKiem.getText().toString().toLowerCase().trim();
                if(!getStringSearch2.isEmpty())
                {
                   //getListUserChat2();
                    searchListUser();
                }
                else
                {
                    rcvUserChat.setAdapter(userAdapter);
                    userAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
