package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class Custom_Dong_BaiDang_Adapter extends BaseAdapter {
    private int layout;
    private Context context;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    List<baiDang> baiDangList;
    baiDang bd;
    Dialog dialog;
    Button dialog_btnCancel,dialog_btnUpdate;
    EditText dialog_edtContent,dialog_edtMoney,dialog_edtLaiSuat,dialog_edtThoiHan;
    String dateWrite;
    Calendar calen;
    RadioGroup dialog_rdgHinhThuc;
    RadioButton dialog_rdbChoVay,dialog_rdbCanVay;
    public Custom_Dong_BaiDang_Adapter(int layout, Context context, List<baiDang> baiDangList) {
        this.layout = layout;
        this.context = context;
        this.baiDangList = baiDangList;
    }

    @Override
    public int getCount() {
        return baiDangList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void addControls()
    {
        calen = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
        dateWrite = "" + simpleDateFormat.format(calen.getTime());
        dialog_btnUpdate = (Button) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_btnCapNhat);
        dialog_btnCancel = (Button) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_btnHuy);
        dialog_edtContent = (EditText) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_edtNoiDung);
        dialog_edtMoney = (EditText) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_edtSoTien);
        dialog_edtLaiSuat = (EditText) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_edtLaiSuat);
        dialog_edtThoiHan = (EditText) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_edtThoiHan);
        dialog_rdgHinhThuc = (RadioGroup) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_rdgHinhThuc);
        dialog_rdbChoVay = (RadioButton) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_rdbChoVay);
        dialog_rdbCanVay = (RadioButton) dialog.findViewById(R.id.id_DialogCapNhatBaiDang_rdbCanVay);

    }
    public class viewHolder
    {
        ImageView imgDelete,imgEdit;
        CircleImageView imgFace;
        TextView txtName,txtContent,txtDate;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final viewHolder holder;
        if(convertView==null)
        {
                holder = new viewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout,null);
                holder.imgFace = (CircleImageView) convertView.findViewById(R.id.id_imgAnh_dongBaiDang);
                holder.imgDelete = (ImageView) convertView.findViewById(R.id.id_imgDelete_dongBaiDang);
                holder.txtName = (TextView) convertView.findViewById(R.id.id_txtName_dongBaiDang);
                holder.txtContent = (TextView) convertView.findViewById(R.id.id_txtNoiDung_dongBaiDang);
                holder.txtDate = (TextView) convertView.findViewById(R.id.id_txtThoiGian_dongBaiDang);
                holder.imgEdit = (ImageView) convertView.findViewById(R.id.id_dongBaiDang_imgEdit);
                convertView.setTag(holder);
        }
        else
        {
                holder = (viewHolder) convertView.getTag();
        }
        bd = baiDangList.get(position);
                databaseReference.child("DanhSachUser").child(bd.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        baiDang bd2 = baiDangList.get(position);
                        member mb = dataSnapshot.getValue(member.class);
                        Picasso.with(context).load(mb.getPhotoURL()).into(holder.imgFace);
                        String contentPost = "Nội dung : "  + bd2.getContent() + "\n" + "Hình thức : " + bd2.getHinhThuc() + "\n" + "Số tiền : " + bd2.getMoney() + "\n"
                                + "Lãi suất : " + bd2.getLaiSuat() + "\n" + "Thời hạn : " + bd2.getThoiHan() ;
                        holder.txtName.setText(mb.getName());
                        holder.txtContent.setText(contentPost);
                        holder.txtDate.setText((CharSequence) bd2.getDateWrite());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        if(bd.getUid().equals(firebaseUser.getUid()))
        {
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgEdit.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgDelete.setVisibility(View.GONE);
            holder.imgEdit.setVisibility(View.GONE);
        }
                holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         final baiDang  bai = baiDangList.get(position);
                        dialog = new Dialog(context);
                        dialog.setContentView(R.layout.custom_dialog_capnhat_baidang);
                        addControls();
                        dialog_edtContent.setText(bai.getContent());
                        dialog_edtMoney.setText(bai.getMoney());
                        dialog_edtLaiSuat.setText(bai.getLaiSuat());
                        dialog_edtThoiHan.setText(bai.getThoiHan());
                        if(bai.getHinhThuc().equals("Cho vay"))
                        {
                            dialog_rdbChoVay.setChecked(true);
                            dialog_rdbCanVay.setChecked(false);
                        }
                        else
                        {
                            dialog_rdbChoVay.setChecked(false);
                            dialog_rdbCanVay.setChecked(true);
                        }
                            dialog_btnUpdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String content = dialog_edtContent.getText().toString().trim();
                                    final String money = dialog_edtMoney.getText().toString().trim();
                                    final String laiSuat = dialog_edtLaiSuat.getText().toString().trim();
                                    final String thoiHan = dialog_edtThoiHan.getText().toString().trim();
                                    if(content.isEmpty() || money.isEmpty() || laiSuat.isEmpty() || thoiHan.isEmpty())
                                    {
                                        Toast.makeText(context, "Bạn phải nhập đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        databaseReference.child("DanhSachPost").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                                {
                                                    baiDang BD = dataSnapshot1.getValue(baiDang.class);
                                                    if(BD.getId().equals(bai.getId()))
                                                    {
                                                        HashMap<String,Object> hashMap = new HashMap<>();
                                                        hashMap.put("check",1);
                                                        hashMap.put("notified",0);
                                                        hashMap.put("content",content);
                                                        hashMap.put("money",money);
                                                        hashMap.put("laiSuat",laiSuat);
                                                        hashMap.put("thoiHan",thoiHan);
                                                        Log.e("ID","s1 : " + BD.getId() + " s2 :" + bai.getId());
                                                        Log.e("bai Dang","content :" + content + " money :" + money);
                                                        hashMap.put("dateWrite",dateWrite);
                                                        dataSnapshot1.getRef().updateChildren(hashMap);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        Toast.makeText(context, "Bạn đã chỉnh sửa bài viết.Chờ phê duyệt !", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                }
                            });

                        dialog_btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                    baiDang BAIDANG = baiDangList.get(position);
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        //Thiết lập tiêu đề cho Dialog
                        dialog.setTitle("Xác nhận");
                        //Thiết lập nội dung cho Dialog
                        dialog.setMessage("Bạn có chắc chắn muốn xóa bài viết này không ?");
                        //Thiết lập icon cho Dialog
                        dialog.setIcon(R.drawable.ic_info_black_24dp);
                        //Thiết lập 1 button cho Dialog.setPositiveButton() để tạo 1 nút CÓ sự kiện còn setNegativeButton() ể tạo 1 nút không CÓ sự kiện
                        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            //Thực hiện khi button Có được Click
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.child("DanhSachPost").child(firebaseUser.getUid()).child(BAIDANG.getId()).removeValue();
                                Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Không xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Show dialog
                        dialog.show();
                      //  databaseReference = FirebaseDatabase.getInstance().getReference();
                      //  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                       // Toast.makeText(context, "Bạn có quyền xóa !", Toast.LENGTH_SHORT).show();
            }
        });
                 return convertView;
    }
}
