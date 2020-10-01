package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import nguyenvanhieu.fithou.hotrovayvon1.Class.message;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT= 1;
    private Context context;
    View view;
    private List<message> listMessage;
    String imgUrl;
    FirebaseUser firebaseUser;
    public MessageAdapter(Context context,List<message> list,String IMGURL)
    {
        this.context = context;
        this.listMessage = list;
        this.imgUrl = IMGURL;
    }
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == MSG_TYPE_LEFT)
        {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
      //  return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
            message ms = listMessage.get(i);
            Picasso.with(context).load(imgUrl).into(viewHolder.imgUser);
            if(ms.getType().equals("text"))
            {
                viewHolder.txtMessage.setText(ms.getSendMessage());
                viewHolder.imgMessage.setVisibility(View.GONE);
                if(i == listMessage.size()-1)
                {
                    if(ms.getIsSeen()==0)
                    {
                        viewHolder.txtSeen.setText("Đã gửi");
                    }
                    else
                    {
                        viewHolder.txtSeen.setText("Đã xem");
                    }
                }
                else
                {
                    viewHolder.txtSeen.setVisibility(View.GONE);
                }
            }
            else
            {
                viewHolder.txtMessage.setVisibility(View.GONE);
                Picasso.with(context).load(ms.getSendMessage()).into(viewHolder.imgMessage);
                if(i == listMessage.size()-1)
                {
                    if(ms.getIsSeen()==0)
                    {
                        viewHolder.txtSeen.setText("Đã gửi");
                    }
                    else
                    {
                        viewHolder.txtSeen.setText("Đã xem");
                    }
                }
                else
                {
                    viewHolder.txtSeen.setVisibility(View.GONE);
                }
            }
    }

    @Override
    public int getItemCount() {
        //Nếu không trả về số member thì sẽ không hiện ra dữ liệu trên Activity mặc dù k có lỗi.
        //Nếu để mặc định return 0 thì sẽ k hiện ra dữ liệu
        return listMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtMessage,txtSeen;
        public ImageView imgUser,imgMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMessage = (ImageView) itemView.findViewById(R.id.id_chat_imgMessage);
            txtMessage = (TextView) itemView.findViewById(R.id.id_chat_txtMessage);
            imgUser = (ImageView) itemView.findViewById(R.id.id_chat_imgUser);
            txtSeen = (TextView) itemView.findViewById(R.id.id_chat_txtSeen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(listMessage.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}