package site.skylake.project_cs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterDetail extends RecyclerView.Adapter<AdapterDetail.AdapterDetailViewHolder> {
    private Context mcontext;
    private ArrayList<Itemdetail> mlistdetail;

    public AdapterDetail(Context context, ArrayList<Itemdetail> listdetail){
        mcontext = context;
        mlistdetail = listdetail;
    }

    @NonNull
    @Override
    public AdapterDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.itemdetail, parent, false);
        return new AdapterDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDetailViewHolder holder, int position) {
        Itemdetail currentdetail = mlistdetail.get(position);

        String img = currentdetail.getImg();
        String namebook = currentdetail.getNamebook();

        holder.textView.setText(namebook);
        Picasso.get().load(img).fit().centerInside().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mlistdetail.size();
    }

    public class AdapterDetailViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;

        public AdapterDetailViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_book_deteail);
            textView = itemView.findViewById(R.id.text_book_deteail);
        }
    }
}
