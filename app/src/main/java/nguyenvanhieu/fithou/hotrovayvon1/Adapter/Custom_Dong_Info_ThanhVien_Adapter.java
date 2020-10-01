package nguyenvanhieu.fithou.hotrovayvon1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nguyenvanhieu.fithou.hotrovayvon1.Class.member;
import nguyenvanhieu.fithou.hotrovayvon1.R;

public class Custom_Dong_Info_ThanhVien_Adapter extends BaseAdapter {
    private int layout;
    private Context context;
    private  List<member> memberList;

    public Custom_Dong_Info_ThanhVien_Adapter(int layout, Context context, List<member> memberList) {
        this.layout = layout;
        this.context = context;
        this.memberList = memberList;
    }

    @Override
    public int getCount() {

        return memberList.size();
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
        CircleImageView imgAnhholder;
        TextView txtNameHolder,txtAccountTypeHolder;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        if(convertView==null)
        {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_dong_info_thanhvien,parent,false);
            holder.imgAnhholder = (CircleImageView) convertView.findViewById(R.id.id_ActivityDSUserFilter_imgThanhVien);
            holder.txtNameHolder = (TextView) convertView.findViewById(R.id.id_ActivityDSUserFilter_txtName);
            holder.txtAccountTypeHolder = (TextView) convertView.findViewById(R.id.id_ActivityDSUserFilter_txtAccountType);
            convertView.setTag(holder);
        }
        else
        {
            holder = (viewHolder) convertView.getTag();
        }
        member mb = memberList.get(position);
        Picasso.with(context).load(mb.getPhotoURL()).into(holder.imgAnhholder);
        holder.txtNameHolder.setText(mb.getName());
        holder.txtAccountTypeHolder.setText(mb.getAccountType());
        return convertView;
    }
}
