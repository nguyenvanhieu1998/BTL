package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.Class.message;
import nguyenvanhieu.fithou.hotrovayvon1.Controller.ChiTietTroChuyenActivity;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<member> listMember;
    String lastMess;
    public UserAdapter(Context context,List<member> list)
    {
        this.context = context;
        this.listMember = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final member mb = listMember.get(i);
        viewHolder.txtUserName.setText(mb.getName());
      //  viewHolder.txtLastMessage.setText(mb.getAccountType());
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
          lastMessage(mb.getUid(),viewHolder.txtLastMessage);
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
        public TextView txtUserName,txtLastMessage;
        public CircleImageView imgUserName,imgStatusOnline,imgStatusOffline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLastMessage = (TextView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUserChat_txtLastMessage);
            txtUserName = (TextView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUserChat_txtUserName);
            imgUserName = (CircleImageView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUserChat_imgUser);
            imgStatusOnline = (CircleImageView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUserChat_imgOnline);
            imgStatusOffline = (CircleImageView) itemView.findViewById(R.id.id_FragmentTroChuyen_DSUserChat_imgOffline);
        }
    }
    private void lastMessage(final String userID, final TextView last_msg)
    {
        lastMess = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                 {
                     message MS = dataSnapshot1.getValue(message.class);
                     if( MS.getReceiver().equals(firebaseUser.getUid()) && MS.getSender().equals(userID) ||
                             MS.getReceiver().equals(userID) && MS.getSender().equals(firebaseUser.getUid()))
                     {
                         lastMess  = MS.getSendMessage();
                     }
                 }
                 switch (lastMess)
                 {
                         case "default" :
                             last_msg.setText("");
                             break;
                         default:
                             last_msg.setText(lastMess);
                             break;
                 }
                lastMess = "default";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

