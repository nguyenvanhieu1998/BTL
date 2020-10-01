package nguyenvanhieu.fithou.hotrovayvon1.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import nguyenvanhieu.fithou.hotrovayvon1.Adapter.Custom_Dong_BaiDang_Adapter;
import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.Controller.ChiTietBaiDangActivity;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class FragmentTrangChu extends Fragment {
    View convertView;
    RadioGroup rdgHinhThuc;
    RadioButton rdbChoVay,rdbCanVay,rdbTatCa;
    ImageView imgFace;
    TextView txtName;
    Button btnAdd;
    EditText edtSearch;
    ListView lvListBaiDang;
    ArrayList<baiDang> baiDangArrayList;
    Custom_Dong_BaiDang_Adapter baiDangAdapter,baiDangFilterAdapter,baiDangChoVayAdapter,baiDangCanVayAdapter;
    Calendar calen = Calendar.getInstance();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userCurrentID;
    FirebaseUser userCurrent ;
    ArrayList<baiDang> listPostFilter;
   // Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_trangchu,container,false);
        addControls();
        addEvents();
        initListview();
        Search2();
        initImageFaceUser();
        return convertView;
    }
    private void addControls() {
        rdgHinhThuc = (RadioGroup) convertView.findViewById(R.id.id_FragmentTrangChu_rdgHinhThuc);
        rdbChoVay = (RadioButton) convertView.findViewById(R.id.id_FragmentTrangChu_rdbChoVay);
        rdbCanVay = (RadioButton) convertView.findViewById(R.id.id_FragmentTrangChu_rdbCanVay);
        rdbTatCa = (RadioButton) convertView.findViewById(R.id.id_FragmentTrangChu_rdbTatCa);
        imgFace = (ImageView) convertView.findViewById(R.id.id_FragmentTrangChu_imgHinhAnh);
        txtName = (TextView) convertView.findViewById(R.id.id_FragmentTrangChu_txtName);
        btnAdd = (Button) convertView.findViewById(R.id.id_FragmentTrangChu_btnAdd);
        edtSearch = (EditText) convertView.findViewById(R.id.id_FragmentTrangChu_edtTimKiem);
        lvListBaiDang = (ListView) convertView.findViewById(R.id.id_FragmentTrangChu_listbiewBaiDang);

    }
    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogDangTin fragmentDialog = new FragmentDialogDangTin();
                fragmentDialog.show(getFragmentManager(),"frgDialogDangTin");
            }
        });
        lvListBaiDang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baiDang bd = baiDangArrayList.get(position);
                Intent intent = new Intent(getActivity(), ChiTietBaiDangActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key_post", bd);
                intent.putExtra("name_post", bundle);
                startActivity(intent);
            }
        });
        rdgHinhThuc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.id_FragmentTrangChu_rdbChoVay :
                        initListviewChoVay();
                        Search(baiDangChoVayAdapter,"Cho vay");
                        break;
                    case R.id.id_FragmentTrangChu_rdbCanVay :
                        initListviewCanVay();
                        Search(baiDangCanVayAdapter,"Cần vay");
                        break;
                    case R.id.id_FragmentTrangChu_rdbTatCa :
                        initListview();
                        Search2();
                      //  Search(baiDangChoVayAdapter,"Cho vay");
                        break;

                }
            }
        });
    }

    private void initImageFaceUser() {
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photo = dataSnapshot.child(userCurrentID).child("photoURL").getValue().toString();
                String name = dataSnapshot.child(userCurrentID).child("name").getValue().toString();
                Picasso.with(getActivity()).load(photo).into(imgFace);
                txtName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initListview() {
        baiDangArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userCurrent = firebaseAuth.getCurrentUser();
        userCurrentID = userCurrent.getUid();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  baiDangArrayList.clear();
                  for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                  {
                      for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                      {
                          baiDang bd = dataSnapshot2.getValue(baiDang.class);
                          if(bd.getCheck()==2)
                          {
                              baiDangArrayList.add(bd);
                          }
                      }
                  }
                baiDangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        baiDangAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),baiDangArrayList);
        lvListBaiDang.setAdapter(baiDangAdapter);
    }
    private void initListviewChoVay() {
        baiDangArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userCurrent = firebaseAuth.getCurrentUser();
        userCurrentID = userCurrent.getUid();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                baiDangArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        baiDang bd = dataSnapshot2.getValue(baiDang.class);
                        if(bd.getCheck()==2 && bd.getHinhThuc().equals("Cho vay"))
                        {
                            baiDangArrayList.add(bd);
                        }
                    }
                }
                baiDangChoVayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        baiDangChoVayAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),baiDangArrayList);
        lvListBaiDang.setAdapter(baiDangChoVayAdapter);
    }
    private void initListviewCanVay() {
        baiDangArrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userCurrent = firebaseAuth.getCurrentUser();
        userCurrentID = userCurrent.getUid();
        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                baiDangArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                    {
                        baiDang bd = dataSnapshot2.getValue(baiDang.class);
                        if(bd.getCheck()==2 && bd.getHinhThuc().equals("Cần vay"))
                        {
                            baiDangArrayList.add(bd);
                        }
                    }
                }
                baiDangCanVayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        baiDangCanVayAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),baiDangArrayList);
        lvListBaiDang.setAdapter(baiDangCanVayAdapter);
    }
    private void Search(final Custom_Dong_BaiDang_Adapter adapter, final String hinhThuc)
    {

         if(edtSearch.getText().toString().isEmpty())
         {
            // baiDangAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),baiDangArrayList);
             lvListBaiDang.setAdapter(adapter);
             adapter.notifyDataSetChanged();
         }
         edtSearch.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                // Log.e("Search", "chuoi :" + s);
                // Toast.makeText(getActivity(), "" + edtSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                // Search();
                 listPostFilter = new ArrayList<>();
                 baiDangFilterAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),listPostFilter);
                 final String getStringSearch = edtSearch.getText().toString().toLowerCase().trim();
                 if(!getStringSearch.isEmpty())
                 {
                 databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         listPostFilter.clear();
                         for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                         {
                             for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                             {
                                 baiDang bd = dataSnapshot2.getValue(baiDang.class);
                                 if(bd.getCheck()==2 && bd.getHinhThuc().equals(hinhThuc))
                                 {
                                     String ten = bd.getName();
                                     ten = ten.toLowerCase();
                                     Log.e("Search", "chuoi1 :" + getStringSearch + "chuoi2 :" + ten);
                                     if(ten.contains(getStringSearch)){
                                         listPostFilter.add(bd);
                                     }
                                 }

                             }
                         }
                         baiDangFilterAdapter.notifyDataSetChanged();
                         lvListBaiDang.setAdapter(baiDangFilterAdapter);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }
                 else
                 {
                     lvListBaiDang.setAdapter(adapter);
                     adapter.notifyDataSetChanged();
                 }
             }
         });
    }
    private void Search2()
    {

        if(edtSearch.getText().toString().isEmpty())
        {
            // baiDangAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),baiDangArrayList);
            lvListBaiDang.setAdapter(baiDangAdapter);
            baiDangAdapter.notifyDataSetChanged();
        }
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Log.e("Search", "chuoi :" + s);
                // Toast.makeText(getActivity(), "" + edtSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                // Search();
                listPostFilter = new ArrayList<>();
                baiDangFilterAdapter = new Custom_Dong_BaiDang_Adapter(R.layout.custom_dong_baidang,getActivity(),listPostFilter);
                final String getStringSearch = edtSearch.getText().toString().toLowerCase().trim();
                if(!getStringSearch.isEmpty())
                {
                    databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listPostFilter.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren())
                                {
                                    baiDang bd = dataSnapshot2.getValue(baiDang.class);
                                    if(bd.getCheck()==2)
                                    {
                                        String ten = bd.getName();
                                        ten = ten.toLowerCase();
                                        Log.e("Search", "chuoi1 :" + getStringSearch + "chuoi2 :" + ten);
                                        if(ten.contains(getStringSearch)){
                                            listPostFilter.add(bd);
                                        }
                                    }

                                }
                            }
                            baiDangFilterAdapter.notifyDataSetChanged();
                            lvListBaiDang.setAdapter(baiDangFilterAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    lvListBaiDang.setAdapter(baiDangAdapter);
                    baiDangAdapter.notifyDataSetChanged();
                }
            }
        });
    }
//    public void searchPostFilterNameUser(final String s)
//    {
//
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        // Query query = databaseReference.child("DanhSachPost").orderByChild("name").startAt(s).endAt(s + "\uf8ff");
//        databaseReference.child("DanhSachPost").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    Query query = databaseReference.child("DanhSachPost").child(dataSnapshot1.getKey()).orderByChild("name").startAt(s).endAt(s + "\uf8ff");
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            baiDangArrayList.clear();
//                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                            {
//                                baiDang mb = dataSnapshot1.getValue(baiDang.class);
//                                if(!mb.getUid().equals(userCurrentID))
//                                {
//                                    baiDangArrayList.add(mb);
//                                }
//                            }
//                            baiDangAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
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


