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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nguyenvanhieu.fithou.hotrovayvon1.R;

public class DangKiActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    EditText edtEmail,edtPass,edtRePass;
    Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        mAuth = FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser()!=null)
//        {
//            finish();
//            startActivity(new Intent(DangKiActivity.this,CapNhatDangKiActivity.class));
//        }
        initActionBar();
        addControls();
        addEvents();

    }

    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.id_ActivityDangKi_ActionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đăng kí");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addEvents() {
       btnRegister.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(ChiTietTroChuyenActivity.this,TrangChuActivity.class));
            }
        });
    }

    private void addControls() {
        edtEmail = (EditText) findViewById(R.id.id_ActivityDangKi_edtEmail);
        edtPass = (EditText) findViewById(R.id.id_ActivityDangKi_edtPassword);
        edtRePass = (EditText) findViewById(R.id.id_ActivityDangKi_edtRePassword);
        btnRegister = (Button) findViewById(R.id.id_ActivityDangKi_btnDangKi);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.id_ActivityDangKi_btnDangKi:
                register();
                break;
        }
    }

    private void register() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String rePass = edtRePass.getText().toString().trim();
        if(email.equals("")||pass.equals("")||rePass.equals(""))
        {
              Toast.makeText(this, "Bạn phải nhập đầy đủ dữ liệu!", Toast.LENGTH_SHORT).show();
        //    Toast.makeText(this, testAccountType, Toast.LENGTH_SHORT).show();
           // return;
        }
        else
        {
            if(pass.equals(rePass))
            {
                registerUserFirebase(email,pass);
            }
            else
            {
                Toast.makeText(this, "Repassword phải giống Password", Toast.LENGTH_SHORT).show();
              //  return;
            }
        }
    }

    private void registerUserFirebase(String email,String password) {
      //  String email = edtEmail.getText().toString().trim();
        //String password = edtPass.getText().toString().trim();
        progressDialog = new ProgressDialog(DangKiActivity.this);
        progressDialog.setMessage("Đăng kí.......");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(DangKiActivity.this, "Đăng kí thành công !", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(DangKiActivity.this,CapNhatDangKiActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                          //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DangKiActivity.this, "Đăng kí thất bại.Vui lòng thử lại !",
                                    Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.cancel();

                        // ...
                    }
                });
    }
}
