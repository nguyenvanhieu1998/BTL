package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Class.baiDang;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class PostAdapter extends BaseAdapter {
    private int layout;
    DatabaseReference databaseReference;
    private Context context;
    List<baiDang> baiDangList;

    public PostAdapter(int layout, Context context, List<baiDang> baiDangList) {
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
        CircleImageView imgFace;
        TextView txtName,txtContent,txtDate;
        FrameLayout duocDuyet,khongDuyet,choDuyet;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final viewHolder holder;
        if(convertView==null)
        {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
         //   holder.imgFace = (CircleImageView) convertView.findViewById(R.id.id_imgAnh_dongBaiDang);
          //  holder.imgDelete = (ImageView) convertView.findViewById(R.id.id_imgDelete_dongBaiDang);
//            holder.txtName = (TextView) convertView.findViewById(R.id.id_txtName_dongBaiDang);
//            holder.txtContent = (TextView) convertView.findViewById(R.id.id_txtNoiDung_dongBaiDang);
//            holder.txtDate = (TextView) convertView.findViewById(R.id.id_txtThoiGian_dongBaiDang);
            // holder.imgEdit = (ImageView) convertView.findViewById(R.id.id_dongBaiDang_imgEdit);
            holder.imgFace = (CircleImageView) convertView.findViewById(R.id.id_myPostItem_imgFace);
            //imgDelete = (ImageView) convertView.findViewById(R.id.id_imgDelete_dongBaiDang);
            holder.txtName = (TextView) convertView.findViewById(R.id.id_myPostItem_txtName);
            holder.txtContent = (TextView) convertView.findViewById(R.id.id_myPostItem_txtNoiDung);
            holder.txtDate = (TextView) convertView.findViewById(R.id.id_myPostItem_txtThoiGian);
            holder.duocDuyet = (FrameLayout) convertView.findViewById(R.id.id_myPostItem_daDuyet);
            holder.khongDuyet = (FrameLayout) convertView.findViewById(R.id.id_myPostItem_khongDuyet);
            holder.choDuyet = (FrameLayout) convertView.findViewById(R.id.id_myPostItem_choDuyet);
            convertView.setTag(holder);
        }
        else
        {
            holder = (viewHolder) convertView.getTag();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final baiDang bd = baiDangList.get(position);
        databaseReference.child("DanhSachUser").child(bd.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                baiDang bd2 = baiDangList.get(position);
                member mb = dataSnapshot.getValue(member.class);
                Picasso.with(context).load(mb.getPhotoURL()).into(holder.imgFace);
                String contentPost = "Nội dung : " + bd2.getContent() + "\n" + "Số tiền : " + bd2.getMoney() + "\n"
                        + "Lãi suất : " + bd2.getLaiSuat() + "\n" + "Thời hạn : " + bd2.getThoiHan() ;
                holder.txtName.setText(mb.getName());
                holder.txtContent.setText(contentPost);
                holder.txtDate.setText((CharSequence) bd2.getDateWrite());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(bd.getCheck()==0)
        {
            holder.choDuyet.setVisibility(View.GONE);
            holder.duocDuyet.setVisibility(View.GONE);
            holder.khongDuyet.setVisibility(View.VISIBLE);
        }
        if(bd.getCheck()==1)
        {
            holder.choDuyet.setVisibility(View.VISIBLE);
            holder.duocDuyet.setVisibility(View.GONE);
            holder.khongDuyet.setVisibility(View.GONE);
        }
        if(bd.getCheck()==2)
        {
            holder.choDuyet.setVisibility(View.GONE);
            holder.duocDuyet.setVisibility(View.VISIBLE);
            holder.khongDuyet.setVisibility(View.GONE);
        }
        return convertView;
    }
}
