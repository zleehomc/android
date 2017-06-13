package Face.Adpater;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hyc.hyc_final.R;

import java.util.List;

import Face.JavaBean.Menuinfo;

/**
 * Created by hyc on 2016/3/30.
 */
public class MenuRecyclerViewAdapter extends RecyclerView.Adapter<MenuRecyclerViewAdapter.MenuViewHolder> {
    private List<Menuinfo> menuinfos;
    private Context context;

    private OnItemClickListener onItemClickListener;
    public MenuRecyclerViewAdapter(List<Menuinfo> menuinfos,Context context) {
        this.menuinfos = menuinfos;
        this.context=context;
    }


    static class MenuViewHolder extends RecyclerView.ViewHolder{

        TextView menu_name;
        ImageView menu_photo;
        CardView cardView;

        public MenuViewHolder(final View itemView) {
            super(itemView);
            menu_name =(TextView)itemView.findViewById(R.id.menu_textview);
            menu_photo=(ImageView)itemView.findViewById(R.id.menu_imageview);
            //设置TextView背景为半透明
            //news_title.setBackgroundColor(Color.argb(20, 0, 0, 0));

        }


    } //绑定
    @Override

    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.menu_item,viewGroup,false);
        MenuViewHolder nhv=new MenuViewHolder(v);
        return nhv;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder,final int position) {
        holder.menu_photo.setImageResource(menuinfos.get(position).getPhotoId());
        holder.menu_name.setText(menuinfos.get(position).getMenu_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
            return menuinfos.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(View parent, int position);
    }
}
