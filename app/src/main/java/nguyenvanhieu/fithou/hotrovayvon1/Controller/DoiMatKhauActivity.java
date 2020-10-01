package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nguyenvanhieu.fithou.hotrovayvon1.R;

public class DoiMatKhauActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText edtEmail,edtOldPass,edtNewPass;
    Button btnChange,btnCancel;
    String email,oldPass,newPass;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        initActionBar();
        addControls();
        addEvents();
    }
    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityDoiMatKhau_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đổi mật khẩu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(ChiTietTroChuyenActivity.this,TrangChuActivity.class));
            }
        });
    }
    private void addEvents() {
        btnChange.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void addControls() {
        edtEmail = (EditText) findViewById(R.id.id_ActivityDoiMatKhau_edtEmail);
        edtOldPass = (EditText) findViewById(R.id.id_ActivityDoiMatKhau_edtOldPass);
        edtNewPass = (EditText) findViewById(R.id.id_ActivityDoiMatKhau_edtNewPass);
        btnChange = (Button) findViewById(R.id.id_ActivityDoiMatKhau_btnChange);
        btnCancel = (Button) findViewById(R.id.id_ActivityDoiMatKhau_btnCancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_ActivityDoiMatKhau_btnChange:
                changeDataLogin();
                break;
            case R.id.id_ActivityDoiMatKhau_btnCancel:
                finish();
                startActivity(new Intent(DoiMatKhauActivity.this,MainActivity.class));
                break;
        }
    }

    private void changeDataLogin() {
         mAuth = FirebaseAuth.getInstance();
         email = edtEmail.getText().toString().trim();
         oldPass = edtOldPass.getText().toString().trim();
         newPass = edtNewPass.getText().toString().trim();
        if(email.equals("")||oldPass.equals("")||newPass.equals(""))
        {
            Toast.makeText(this, "Bạn phải nhập đầy đủ dữ liệu!", Toast.LENGTH_SHORT).show();
            //    Toast.makeText(this, testAccountType, Toast.LENGTH_SHORT).show();
            // return;
        }
        else
        {
            if(oldPass.equals(newPass))
            {
                Toast.makeText(this, "Mật khẩu cũ phải khác mật khẩu mới", Toast.LENGTH_SHORT).show();
            }
            else
                {
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    String userEmail = user.getEmail();
//                    changeUserFirebase(userEmail,newPass,user);
                    mAuth.signInWithEmailAndPassword(email, oldPass)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                         FirebaseUser user = mAuth.getCurrentUser();
                                         String userEmail = user.getEmail();
                                         changeUserFirebase(userEmail,newPass,user);
                                       // Toast.makeText(DoiMatKhauActivity.this, "Login Successfully !", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DoiMatKhauActivity.this, "Email và password không tồn tại.Vui lòng thử lại !",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        }
    }

    private void changeUserFirebase(String EMAIL, String PASS, final FirebaseUser USER) {
        progressDialog = new ProgressDialog(DoiMatKhauActivity.this);
        progressDialog.setMessage("Thay đổi mật khẩu .......");
        progressDialog.show();
        AuthCredential credential = EmailAuthProvider.getCredential(email,oldPass);
        USER.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    USER.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DoiMatKhauActivity.this, "Thay đổi mật khẩu thành công !", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(DoiMatKhauActivity.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(DoiMatKhauActivity.this, "Thay đổi mật khẩu thất bại.Vui lòng thử lại !", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                  //  Toast.makeText(DoiMatKhauActivity.this, "Change password not successfully !", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(DoiMatKhauActivity.this, "Email và password không tồn tại.Vui lòng thử lại !",
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.cancel();
            }
        });

    }

}
