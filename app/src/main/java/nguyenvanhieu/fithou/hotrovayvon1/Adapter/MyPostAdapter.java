package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    private Context context;
    DatabaseReference databaseReference;
    baiDang bd;
  //  View view;
    private List<baiDang> listBaiDang;
    public MyPostAdapter(Context context,List<baiDang> list)
    {
        this.context = context;
        this.listBaiDang = list;
    }
    @NonNull
    @Override
    public MyPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.mypost_item,viewGroup,false);
            return new MyPostAdapter.ViewHolder(view);
        //  return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostAdapter.ViewHolder viewHolder, int i) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
         bd = listBaiDang.get(i);
        databaseReference.child("DanhSachUser").child(bd.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member mb = dataSnapshot.getValue(member.class);
                Picasso.with(context).load(mb.getPhotoURL()).into(viewHolder.imgFace);
                String contentPost = "Nội dung : " + bd.getContent() + "\n" + "Số tiền : " + bd.getMoney() + "\n"
                        + "Lãi suất : " + bd.getLaiSuat() + "\n" + "Thời hạn : " + bd.getThoiHan() ;
                viewHolder.txtName.setText(mb.getName());
                viewHolder.txtContent.setText(contentPost);
                viewHolder.txtDate.setText((CharSequence) bd.getDateWrite());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(bd.getCheck()==0)
        {
            viewHolder.choDuyet.setVisibility(View.GONE);
            viewHolder.duocDuyet.setVisibility(View.GONE);
            viewHolder.khongDuyet.setVisibility(View.VISIBLE);
        }
        if(bd.getCheck()==1)
        {
            viewHolder.choDuyet.setVisibility(View.VISIBLE);
            viewHolder.duocDuyet.setVisibility(View.GONE);
            viewHolder.khongDuyet.setVisibility(View.GONE);
        }
        if(bd.getCheck()==2)
        {
            viewHolder.choDuyet.setVisibility(View.GONE);
            viewHolder.duocDuyet.setVisibility(View.VISIBLE);
            viewHolder.khongDuyet.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        //Nếu không trả về số member thì sẽ không hiện ra dữ liệu trên Activity mặc dù k có lỗi.
        //Nếu để mặc định return 0 thì sẽ k hiện ra dữ liệu
        return listBaiDang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView imgFace;
        TextView txtName,txtContent,txtDate;
        FrameLayout duocDuyet,khongDuyet,choDuyet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFace = (CircleImageView) itemView.findViewById(R.id.id_myPostItem_imgFace);
          //imgDelete = (ImageView) convertView.findViewById(R.id.id_imgDelete_dongBaiDang);
            txtName = (TextView) itemView.findViewById(R.id.id_myPostItem_txtName);
            txtContent = (TextView) itemView.findViewById(R.id.id_myPostItem_txtNoiDung);
            txtDate = (TextView) itemView.findViewById(R.id.id_myPostItem_txtThoiGian);
            duocDuyet = (FrameLayout) itemView.findViewById(R.id.id_myPostItem_daDuyet);
            khongDuyet = (FrameLayout) itemView.findViewById(R.id.id_myPostItem_khongDuyet);
            choDuyet = (FrameLayout) itemView.findViewById(R.id.id_myPostItem_choDuyet);
        }
    }

}