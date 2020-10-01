package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.Controller.ChiTietTroChuyenActivity;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class UserHorizAdapter extends RecyclerView.Adapter<UserHorizAdapter.ViewHolder> {
    private Context context;
    private List<member> listMember;
    public UserHorizAdapter(Context context,List<member> list)
    {
        this.context = context;
        this.listMember = list;
    }
    @NonNull
    @Override
    public UserHorizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_horiz,viewGroup,false);
        return new UserHorizAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHorizAdapter.ViewHolder viewHolder, int i) {
        final member mb = listMember.get(i);
     //   viewHolder.txtUserName.setText(mb.getName());
        Picasso.with(context).load(mb.getPhotoURL()).into(viewHolder.imgUserName);
        if(mb.getStatus().equals("offline"))
        {
            viewHolder.imgStatusOffline.setVisibility(View.VISIBLE);
            viewHolder.imgStatusOnline.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.imgStatusOffline.setVisibility(View.GONE);
            viewHolder.imgStatusOnline.setVisibility(View.VISIBLE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChiTietTroChuyenActivity.class);
                intent.putExtra("uid",mb.getUid());
                intent.putExtra("imgUrl",mb.getPhotoURL());
                intent.putExtra("name",mb.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Nếu không trả về số member thì sẽ không hiện ra dữ liệu trên Activity mặc dù k có lỗi.
        //Nếu để mặc định return 0 thì sẽ k hiện ra dữ liệu
        return listMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtUserName;
        public CircleImageView imgUserName,imgStatusOnline,imgStatusOffline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
         //   txtUserName = (TextView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUser_txtName);
            imgUserName = (CircleImageView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUser_imgUser);
            imgStatusOnline = (CircleImageView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUser_imgOnline);
            imgStatusOffline = (CircleImageView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUser_imgOffline);
        }
    }
}

