package Face.Adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.hyc.hyc_final.R;
import Face.JavaBean.myUserDetail;

/**
 * Created by hyc on 2016/3/30.
 */
public class myUserDetailViewAdapter extends RecyclerView.Adapter<myUserDetailViewAdapter.UserViewHolder> {

    private myUserDetail myUserDetail;
    private Context context;

    public myUserDetailViewAdapter(myUserDetail myUserDetail, Context context) {
        this.myUserDetail = myUserDetail;
        this.context = context;
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView value;

        public UserViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.userDetailTitle);
            value = (TextView) itemView.findViewById(R.id.userDetailValue);
        }


    } //绑定

    @Override

    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)  {
        View v= LayoutInflater.from(context).inflate(R.layout.user_info_item,viewGroup,false);
        UserViewHolder nhv=new UserViewHolder(v);
        return nhv;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
            switch (position) {
                case 0:
                    holder.title.setText("用户编号");
                    holder.value.setText(myUserDetail.getUserId());
                    break;
                case 1:
                    holder.title.setText("用户名");
                    holder.value.setText(myUserDetail.getUserName());
                    break;
                case 2:
                    holder.title.setText("邮箱");
                    holder.value.setText(myUserDetail.getEmail());
                    break;
                case 3:
                    holder.title.setText("用户级别");
                    if (myUserDetail.getT_Status().equals("1")) {
                        holder.value.setText("监考老师");
                    } else if (myUserDetail.getT_Status().equals("2")) {
                        holder.value.setText("考生");
                    }
                    break;
                default:
                    break;
            }


    }
    @Override
    public int getItemCount() {
            return 4;
    }

}
