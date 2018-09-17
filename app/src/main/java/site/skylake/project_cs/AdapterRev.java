package site.skylake.project_cs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRev extends RecyclerView.Adapter<AdapterRev.viewHolderrev> {

    private Context mContext;
    private ArrayList<Itemrev> Listrev;
    private onItemClickListener listener;


    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public AdapterRev (Context context, ArrayList<Itemrev> listrev){
        mContext = context;
        Listrev = listrev;
    }


    @NonNull
    @Override
    public viewHolderrev onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.itemrev, parent, false);
        return new viewHolderrev(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderrev holder, int position) {
        Itemrev currentItem = Listrev.get(position);
        String Name = currentItem.getMname();
        String Id = currentItem.getMid();

        holder.namerev.setText(Name);

    }

    @Override
    public int getItemCount() {
        return Listrev.size();
    }

    public class viewHolderrev extends RecyclerView.ViewHolder{

        public TextView namerev;


        public viewHolderrev(View itemView) {
            super(itemView);

            namerev = itemView.findViewById(R.id.name_rev);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
