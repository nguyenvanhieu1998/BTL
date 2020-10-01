package nguyenvanhieu.fithou.hotrovayvon1.Controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nguyenvanhieu.fithou.hotrovayvon1.R;
import nguyenvanhieu.fithou.hotrovayvon1.Service.NotificationService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txtChangePass;
    CheckBox cbRemember;
    EditText edtEmail,edtPass;
    Button btnLogin,btnRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String email,password;
    SharedPreferences shap;
    Intent NotiService;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        shap = getSharedPreferences("dataLogin",MODE_PRIVATE);
        addControls();
        addEvents();
        getDataSharedPreferences();
    }
    private void addEvents() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtChangePass.setOnClickListener(this);
    }

    private void addControls() {

        txtChangePass = (TextView) findViewById(R.id.id_ActivityDangNhap_txtDoiMatKhau);
        cbRemember = (CheckBox) findViewById(R.id.id_ActivityDangNhap_cbNhoMatKhau);
        edtEmail = (EditText) findViewById(R.id.id_ActivityDangNhap_edtEmail);
        edtPass = (EditText) findViewById(R.id.id_ActivityDangNhap_edtPassword);
        btnLogin = (Button) findViewById(R.id.id_ActivityDangNhap_btnDangNhap);
        btnRegister = (Button) findViewById(R.id.id_ActivityDangNhap_btnDangKi);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.id_ActivityDangNhap_btnDangNhap:
                nextHomeActivity();
                break;
            case R.id.id_ActivityDangNhap_btnDangKi:
                startActivity( new Intent(MainActivity.this,DangKiActivity.class));
                break;
            case R.id.id_ActivityDangNhap_txtDoiMatKhau:
                startActivity(new Intent(MainActivity.this,DoiMatKhauActivity.class));
                break;
        }
    }

    private void nextHomeActivity() {
         email = edtEmail.getText().toString().trim();
         password = edtPass.getText().toString().trim();
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Hãy nhập đủ dữ liệu !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            testDataLogin();
        }
    }

    private void testDataLogin() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Đang đăng nhập ........");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // FirebaseUser user = mAuth.getCurrentUser();
                            if(cbRemember.isChecked())
                            {
                                //Khai báo 1 biến kiểu editor để chỉnh chỉnh sửa biến shap
                                SharedPreferences.Editor edit = shap.edit();
                                //gán user vào bộ lưu trữ shap đã khai báo ở trên
                                edit.putString("key_email",email);
                                //gán pass vào bộ lưu trữ shap đã khai báo ở trên
                                edit.putString("key_pass",password);
                                //gán trạng thái của checkbox vào bộ lưu trữ shap đã khai báo ở trên
                                edit.putBoolean("key_checkbox",true);
                                //commit() để xác nhận rồi đóng bộ chỉnh sửa
                                edit.commit();
                            }
                            else
                            {
                                //lại mở bộ soạn thảo để chỉnh sửa
                                SharedPreferences.Editor edit = shap.edit();
                                edit.remove("key_email");
                                edit.remove("key_pass");
                                edit.remove("key_checkbox");
                                edit.commit();
                            }
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                            finish();
//                            NotiService = new Intent(MainActivity.this, NotificationService.class);
//                            NotiService.putExtra("UserUID",user.getUid());
//                            startService(NotiService);
                            Log.e("user",user.getUid());
                            Toast.makeText(MainActivity.this, "Start service", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,TrangChuActivity.class);
                            intent.putExtra("key_userID",user.getUid());
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Email và password không đúng.Vui lòng thử lại !",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }
    public void getDataSharedPreferences()
    {
        edtEmail.setText(shap.getString("key_email",""));
        edtPass.setText(shap.getString("key_pass",""));
        cbRemember.setChecked(shap.getBoolean("key_checkbox",false));
    }


}
