package nguyenvanhieu.fithou.hotrovayvon1.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class FragmentDialogDangTin extends DialogFragment implements View.OnClickListener{
    RadioGroup dialog_rdgHinhThuc;
    RadioButton dialog_rdbChoVay,dialog_rdbCanVay;
    Dialog dialog;
    Button dialog_btnWrite;
    Button dialog_btnCancel;
    EditText dialog_edtContent,dialog_edtMoney,dialog_edtLaiSuat,dialog_edtThoiHan;
    String content,money,laiSuat,thoiHan;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userCurrentID,dateWrite,nameUserCurrent,photo;
    FirebaseUser userCurrent;
    baiDang bd;
    Calendar calen;
    float tien;
    String hinhThuc;
    String hinhthuc2;
    String key_push_post ;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Khai báo 1 đối tượng Dialog khoogn sử dụng giao diện mặc định của hệ thống.Tham số truyền vào là context
        dialog = new Dialog(getActivity());
        //Thiết lập giao diện cho đối tượng Dialog qua phương thức setContentView().Tham số truyền vào ResourceLayout
        dialog.setContentView(R.layout.custom_dialog_dangtin);
        //Khi click vào màn hình thì dialog sẽ k bị tắt trừ khi bấm nút hủy
        //dialog.setTitle("Đăng tin");
        addControlsDialog();
        addEvents();
        return dialog;
    }

    private void addEvents() {
        dialog_btnWrite.setOnClickListener(this);
        dialog_btnCancel.setOnClickListener(this);
    }

    public void addControlsDialog()
    {
         dialog_rdgHinhThuc = (RadioGroup) dialog.findViewById(R.id.id_DialogDangTin_rdgHinhThuc);
         dialog_rdbCanVay = (RadioButton) dialog.findViewById(R.id.id_DialogDangTin_rdbCanVay);
         dialog_rdbChoVay = (RadioButton) dialog.findViewById(R.id.id_DialogDangTin_rdbChoVay);
         dialog_btnWrite = (Button) dialog.findViewById(R.id.id_DialogDangTin_btnDang);
         dialog_btnCancel = (Button) dialog.findViewById(R.id.id_DialogDangTin_btnHuy);
         dialog_edtContent = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtNoiDung);
         dialog_edtMoney = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtSoTien);
         dialog_edtLaiSuat = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtLaiSuat);
         dialog_edtThoiHan = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtThoiHan);
    }
    public String getDataAccountType()
    {
        //final String dataAccountType ;
        dialog_rdgHinhThuc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.id_DialogDangTin_rdbCanVay:
                        hinhThuc = dialog_rdbCanVay.getText().toString();
                        break;
                    case R.id.id_DialogDangTin_rdbChoVay:
                        hinhThuc = dialog_rdbCanVay.getText().toString();
                        break;
                }

            }
        });
        return hinhThuc;

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_DialogDangTin_btnDang:
                addPost();
                break;
            case R.id.id_DialogDangTin_btnHuy:
                dialog.dismiss();
                break;
        }
    }

    private void addPost() {
        content = dialog_edtContent.getText().toString().trim();
        money = dialog_edtMoney.getText().toString().trim();
        laiSuat = dialog_edtLaiSuat.getText().toString().trim();
        thoiHan = dialog_edtThoiHan.getText().toString().trim();
        hinhthuc2 = getDataAccountType();
        if(content.equals("")||money.equals("")||laiSuat.equals("")||thoiHan.equals("") || hinhthuc2==null)
        {
            Toast.makeText(getActivity(), "Bạn phải nhập đầy đủ dữ liệu !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            createNewObjectPost();
        }
    }
    private void createNewObjectPost() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userCurrent = firebaseAuth.getCurrentUser();
        userCurrentID = userCurrent.getUid();
        calen = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        dateWrite = "" + simpleDateFormat.format(calen.getTime());
        key_push_post =  databaseReference.child("DanhSachPost").child(userCurrentID).push().getKey();
        //key_push_post =  databaseReference.child("DanhSachPost").child(userCurrentID).push().toString();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 //String permission = dataSnapshot.child(userCurrentID).child("permission").getValue().toString();
                 nameUserCurrent =dataSnapshot.child(userCurrentID).child("name").getValue().toString();
                 photo = dataSnapshot.child(userCurrentID).child("photoURL").getValue().toString();
                 bd = new baiDang(key_push_post,nameUserCurrent,photo,hinhthuc2,content,money,laiSuat,thoiHan,dateWrite,userCurrentID,1,0);
                 addPostToFirebase();

                // Bitmap bm = getBitmapFromURL(photo);
                //   holder.imgFace.setImageURI(Uri.parse(photo));
               // Picasso.with(getActivity()).load(photo).into(holder.imgFace);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Toast.makeText(getActivity(), "Bạn đã đăng tin.Chờ phê duyệt !", Toast.LENGTH_SHORT).show();
    }
    private void addPostToFirebase() {
        databaseReference.child("DanhSachPost").child(userCurrentID).child(key_push_post).setValue(bd, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null)                        {

                    dialog.dismiss();
                    // Toast.makeText(this, "Bạn đã đăng ting.Chờ phê duyệt !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dialog.dismiss();
                    // Toast.makeText(getActivity(), "Đăng tin thất bại !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
//package nguyenvanhieu.fithou.hotrovayvon_application.Fragment;
//
//import android.app.Dialog;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//
//import nguyenvanhieu.fithou.hotrovayvon_application.Class.baiDang;
//import nguyenvanhieu.fithou.hotrovayvon_application.Class.member;
//import nguyenvanhieu.fithou.hotrovayvon_application.R;
//
//public class FragmentDialogDangTin extends DialogFragment implements View.OnClickListener{
//    Dialog dialog;
//    Button dialog_btnWrite;
//    Button dialog_btnCancel;
//    EditText dialog_edtContent,dialog_edtMoney,dialog_edtLaiSuat,dialog_edtThoiHan;
//    String content,money,laiSuat,thoiHan;
//    DatabaseReference databaseReference;
//    FirebaseAuth firebaseAuth;
//    String userCurrentID,dateWrite,nameUserCurrent,photo;
//    FirebaseUser userCurrent;
//    baiDang bd;
//    Calendar calen;
//    float tien;
//    String key_push_post ;
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        //Khai báo 1 đối tượng Dialog khoogn sử dụng giao diện mặc định của hệ thống.Tham số truyền vào là context
//        dialog = new Dialog(getActivity());
//        //Thiết lập giao diện cho đối tượng Dialog qua phương thức setContentView().Tham số truyền vào ResourceLayout
//        dialog.setContentView(R.layout.custom_dialog_dangtin);
//        //Khi click vào màn hình thì dialog sẽ k bị tắt trừ khi bấm nút hủy
//        //dialog.setTitle("Đăng tin");
//        addControlsDialog();
//        addEvents();
//        return dialog;
//    }
//
//    private void addEvents() {
//        dialog_btnWrite.setOnClickListener(this);
//        dialog_btnCancel.setOnClickListener(this);
//    }
//
//    public void addControlsDialog()
//    {
//
//        dialog_btnWrite = (Button) dialog.findViewById(R.id.id_DialogDangTin_btnDang);
//        dialog_btnCancel = (Button) dialog.findViewById(R.id.id_DialogDangTin_btnHuy);
//        dialog_edtContent = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtNoiDung);
//        dialog_edtMoney = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtSoTien);
//        dialog_edtLaiSuat = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtLaiSuat);
//        dialog_edtThoiHan = (EditText) dialog.findViewById(R.id.id_DialogDangTin_edtThoiHan);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.id_DialogDangTin_btnDang:
//                addPost();
//                break;
//            case R.id.id_DialogDangTin_btnHuy:
//                dialog.dismiss();
//                break;
//        }
//    }
//
//    private void addPost() {
//        content = dialog_edtContent.getText().toString().trim();
//        money = dialog_edtMoney.getText().toString().trim();
//        laiSuat = dialog_edtLaiSuat.getText().toString().trim();
//        thoiHan = dialog_edtThoiHan.getText().toString().trim();
//        if(content.equals("")||money.equals("")||laiSuat.equals("")||thoiHan.equals(""))
//        {
//            Toast.makeText(getActivity(), "Bạn phải nhập đầy đủ dữ liệu !", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            createNewObjectPost();
//        }
//    }
//
//    private void addPostToFirebase() {
//        databaseReference.child("DanhSachPost").child(userCurrentID).child("key_push_post").setValue(bd, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if(databaseError == null)
//                {
//
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), "Đăng tin thành công", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(), "Đăng tin thất bại !", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        //  Toast.makeText(getActivity(), "url : " + photo, Toast.LENGTH_SHORT).show();
//    }
//
//    private void createNewObjectPost() {
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseAuth = FirebaseAuth.getInstance();
//        userCurrent = firebaseAuth.getCurrentUser();
//        userCurrentID = userCurrent.getUid();
//        calen = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
//        dateWrite = "" + simpleDateFormat.format(calen.getTime());
//        key_push_post =   databaseReference.child("DanhSachPost").child(userCurrentID).push().toString();
//        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                nameUserCurrent = dataSnapshot.child(userCurrentID).child("name").getValue().toString();
//                photo = dataSnapshot.child(userCurrentID).child("photoURL").getValue().toString();
//                bd = new baiDang(key_push_post,nameUserCurrent,photo,content,money,laiSuat,thoiHan,dateWrite,userCurrentID,1);
//                addPostToFirebase();
//                // Bitmap bm = getBitmapFromURL(photo);
//                //   holder.imgFace.setImageURI(Uri.parse(photo));
//                // Picasso.with(getActivity()).load(photo).into(holder.imgFace);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
//}

