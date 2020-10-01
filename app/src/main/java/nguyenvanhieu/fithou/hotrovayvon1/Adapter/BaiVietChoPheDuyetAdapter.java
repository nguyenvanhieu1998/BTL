package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class BaiVietChoPheDuyetAdapter extends BaseAdapter {
    private int layout;
    private Context context;
    DatabaseReference databaseReference;
   // FirebaseUser firebaseUser;
    List<baiDang> baiDangList;
    baiDang bd;

    public BaiVietChoPheDuyetAdapter(int layout, Context context, List<baiDang> baiDangList) {
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
    public class viewHolder
    {
        ImageView imgFace;
        TextView txtName,txtContent,txtDate;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder holder;
        if(convertView==null)
        {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.imgFace = (ImageView) convertView.findViewById(R.id.id_dongBaiDangChoPheDuyet_imgAnh);
            holder.txtName = (TextView) convertView.findViewById(R.id.id_dongBaiDangChoPheDuyet_txtName);
            holder.txtContent = (TextView) convertView.findViewById(R.id.id_dongBaiDangChoPheDuyet_txtNoiDung);
            holder.txtDate = (TextView) convertView.findViewById(R.id.id_dongBaiDangChoPheDuyet_txtThoiGian);
            // holder.imgEdit = (ImageView) convertView.findViewById(R.id.id_dongBaiDang_imgEdit);
            convertView.setTag(holder);
        }
        else
        {
            holder = (viewHolder) convertView.getTag();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        bd = baiDangList.get(position);
        databaseReference.child("DanhSachUser").child(bd.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                baiDang bd2 = baiDangList.get(position);
                member mb = dataSnapshot.getValue(member.class);
                Picasso.with(context).load(mb.getPhotoURL()).into(holder.imgFace);
                String contentPost = "Nội dung : " + bd.getContent() + "\n" + "Hình thức : " + bd.getHinhThuc() + "\n" + "Số tiền : " + bd.getMoney() + "\n"
                        + "Lãi suất : " + bd.getLaiSuat() + "\n" + "Thời hạn : " + bd.getThoiHan() ;
                holder.txtName.setText(mb.getName());
                holder.txtContent.setText(contentPost);
                holder.txtDate.setText((CharSequence) bd.getDateWrite());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // holder.imgFace.setImageURI(Uri.parse(bd.getImageMemberWrite()));

        return convertView;
    }
}
