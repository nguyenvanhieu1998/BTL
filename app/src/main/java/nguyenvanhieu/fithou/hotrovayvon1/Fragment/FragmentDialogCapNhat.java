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

import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class FragmentDialogCapNhat extends DialogFragment implements View.OnClickListener{
    int kq ;
    Dialog dialog;
    Button dialog_btnUpdate;
    Button dialog_btnCancel;
    EditText dialog_edtName,dialog_edtPhone,dialog_edtAddress,dialog_edtEmail;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userCurrentID;
    String name,phone,address,email;
    RadioGroup dialog_rdgAccounType;
    RadioButton dialog_rdbtnPerson,dialog_rdbtnCompany,dialog_rdbtnStartup;
    String dataAccountType;
    String photo;
    FirebaseUser userCurrent;
    member mb = null;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //Khai báo 1 đối tượng Dialog khoogn sử dụng giao diện mặc định của hệ thống.Tham số truyền vào là context
        dialog = new Dialog(getActivity());
        //Thiết lập giao diện cho đối tượng Dialog qua phương thức setContentView().Tham số truyền vào ResourceLayout
        dialog.setContentView(R.layout.custom_dialog_capnhat);
        //Khi click vào màn hình thì dialog sẽ k bị tắt trừ khi bấm nút hủy
        //dialog.setTitle("Đăng tin");
        addControlsDialog();
        addEvents();
        return dialog;
    }

    private void addEvents() {
        dialog_btnUpdate.setOnClickListener(this);
        dialog_btnCancel.setOnClickListener(this);

    }

    private void addControlsDialog() {
        dialog_btnUpdate = (Button) dialog.findViewById(R.id.id_DialogCapNhat_btnCapNhat);
        dialog_btnCancel = (Button) dialog.findViewById(R.id.id_DialogCapNhat_btnHuy);
        dialog_edtName = (EditText) dialog.findViewById(R.id.id_DialogCapNhat_edtName);
        dialog_edtPhone = (EditText) dialog.findViewById(R.id.id_DialogCapNhat_edtPhone);
        dialog_edtAddress = (EditText) dialog.findViewById(R.id.id_DialogCapNhat_edtAddress);
        dialog_rdgAccounType = (RadioGroup) dialog.findViewById(R.id.id_DialogCapNhat_rdgAccountType);
        dialog_rdbtnCompany = (RadioButton) dialog.findViewById(R.id.id_DialogCapNhat_rdCompany);
        dialog_rdbtnPerson = (RadioButton) dialog.findViewById(R.id.id_DialogCapNhat_rdPerson);
        dialog_rdbtnStartup = (RadioButton) dialog.findViewById(R.id.id_DialogCapNhat_rdStartup);
        dialog_edtEmail = (EditText) dialog.findViewById(R.id.id_DialogCapNhat_edtEmail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_DialogCapNhat_btnCapNhat:
                updateDataUserCurent();
              //  Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_DialogCapNhat_btnHuy:
                dialog.dismiss();
              //  photoUrl = getPhotoUrl();
               // getPhotoUrl();
                break;
        }
    }
    public void updateDataUserCurent()
    {
        name = dialog_edtName.getText().toString().trim();
        phone = dialog_edtPhone.getText().toString().trim();
        address = dialog_edtAddress.getText().toString().trim();
        email = dialog_edtEmail.getText().toString().trim();
        dataAccountType = getDataAccountType();
        if(name.equals("")||phone.equals("")||address.equals("")||email.equals("")||dataAccountType==null)
        {
            Toast.makeText(getActivity(), "Bạn phải nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
        else
        {
            setInformationUserCurrent();
            Toast.makeText(getActivity(), "Cập nhật thành công ! ", Toast.LENGTH_SHORT).show();
        }
    }
//    private void setInformationUserCurrent() {
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseAuth = FirebaseAuth.getInstance();
//        userCurrent = firebaseAuth.getCurrentUser();
//        userCurrentID = userCurrent.getUid();
//        //Log.d("photoURL : ",photoUrl);
//        mb = new member(name,dataAccountType,email,phone,address,photoUrl);
//
//
//
//    }

    private void setInformationUserCurrent() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        userCurrent = firebaseAuth.getCurrentUser();
        userCurrentID = userCurrent.getUid();
        databaseReference.child("DanhSachUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    photo = dataSnapshot.child(userCurrentID).child("photoURL").getValue().toString();
                    mb = new member(name,dataAccountType,email,phone,address,userCurrentID,photo,"online","user");
                   databaseReference.child("DanhSachUser").child(userCurrentID).setValue(mb, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError == null)
                        {
                           dialog.dismiss();

                        }
                        else
                        {
                            dialog.dismiss();


                        }
                    }

                });
                  //  Toast.makeText(getActivity(), "url : " + photo, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // return photo;
    }

    public String getDataAccountType()
    {
        //final String dataAccountType ;
        dialog_rdgAccounType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId)
                {
                    case R.id.id_DialogCapNhat_rdCompany:
                        dataAccountType = dialog_rdbtnCompany.getText().toString();
                        break;
                    case R.id.id_DialogCapNhat_rdPerson:
                        dataAccountType = dialog_rdbtnPerson.getText().toString();
                        break;
                    case R.id.id_DialogCapNhat_rdStartup:
                        dataAccountType = dialog_rdbtnStartup.getText().toString();
                        break;

                }

            }
        });
        return dataAccountType;

    }
}
